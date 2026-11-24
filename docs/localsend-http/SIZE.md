# WiFi 快传体积：测到的字节与尚待实现的部分

下面的 **B 是字节**，KiB = 1,024 B，MiB = 1,048,576 B。测的是发布文件，不是安装后占用或传输时的内存。网络传送原文件字节，不需要重新编码相册里的图片或视频。

## 同一 Android 工程的对照构建

从 OpenQPic `e4c35cb` 的 Git 快照复制出临时工程，使用相同 SDK 36、Build Tools 36.0.0、资源、Manifest 和构建配置。debug 由 AGP 调试签名，release 同时记录 unsigned 和用**同一把临时 RSA 密钥**签名后的 APK；每个签名 release 都通过 `apksigner verify`。删除组刻意不再包含旧代码，保留外部入口以控制变量：**只称重，不是能工作的快传/视频版本**。没有把源码目录大小、`.o`、带符号的 NDK 输出或 AAB 当作手机 APK 大小。

| 实际构建的内容 | debug APK，B | unsigned release，B | 签名 release，B |
|---|---:|---:|---:|
| 原 OpenQPic，包括旧快传及两份原图像库 | 2,005,685 | 1,879,608 | 1,937,165 |
| 移去旧 transfer 包的 85 类，图像库不变 | 1,978,944 | 1,855,640 | 1,904,397 |
| 上一行 + 两份独立的真实 HTTP 解析器 ELF（还没有新快传） | 2,006,820 | 1,883,328 | 1,953,697 |
| 移去旧 85 类 + OpenQPicJni 重建图像库，`armeabi+x86`，**无新网络代码** | 2,473,431 | 2,350,136 | 2,400,013 |
| 同上，只有新移植 `arm64-v8a` 图像库 | 2,191,003 | 2,067,796 | 2,100,937 |
| 同上，`armeabi+x86+arm64-v8a` 三份重建图像库 | 2,764,095 | 2,640,708 | 2,695,001 |

- 原始发布的 `OpenPic_stable.apk` 是 **1,910,620 B**，但它的 DEX 布局、包装/签名不同；上表与预测都使用**当前 OpenQPic 工程**做基线。
- 移除旧命名空间后，产生的 release DEX 定义数从 `2,076+256` 变为 `1,995+252`。两份 DEX 的**未压缩**总量少 55,444 B；两份 **ZIP 压缩条目**合计只少 **23,968 B**；在这套签名/对齐配置下，签名 release APK 恰好少 **32,768 B**。实际还要补回 UI 和视频转交；不能把 32 KiB 当成新功能稳得的净空间。
- `armeabi/x86` 原图像 `.so` 为 **35,004 / 43,152 B**，重编图像 `.so` 为 **198,252 / 374,400 B**，两 ABI 合计相差 **494,496 B**，与 LocalSend 协议代码无关。此工程 APK 中 ELF 是 ZIP STORE；相反 DEX 是 DEFLATE。独立解析器实际加了 **27,424 B ELF**，unsigned APK 多 **27,688 B**，签名 APK 多 **49,300 B**，说明签名/对齐和条目开销必须以最终包计算。

## 单份 `.so`：真实编译的参照物

用 NDK 在 API 23 目标上，独立编译[固定版本 picohttpparser](https://github.com/h2o/picohttpparser/tree/dd573d8fa9264c646a400e96ce9f07dea6f156e4) 的实际 `.c` 与 `.h`；`-Os`、`APP_STL=none`，读取 `libs/<abi>/` 下剥离后的共享库。验证请求/响应和 chunked 解码符号确实在 ELF 中。另以相同模块名和设置编译只有一个函数的空壳，避免把启动/段/对齐开销都算成 HTTP 代码。

| ABI | 空壳，B | 含 HTTP 解析器，B | 差值，B | OpenQPicJni 的既有重编图像库，B |
|---|---:|---:|---:|---:|
| `armeabi`，r16b | 13,696 | 17,840 | 4,144 | 198,252 |
| `x86`，r16b | 5,440 | 9,584 | 4,144 | 374,400 |
| `arm64-v8a`，r28c，16 KiB 段对齐 | 3,984 | 8,488 | 4,504 | 290,432 |

此解析器是**为独立实现选取的成本参照**，不是 LocalSend 上游使用的库。它没有设备发现、socket 调度、会话、UI、存储、授权和文件校验。一个 9 KiB 的解析器绝不等于一个 9 KiB 的完整快传插件。

## 首版实现预算（估算，不是成品实测）

按[设计](README.md)的 HTTP/IPv4 双向推送，使用系统 JSON、复用现有 UI 和存储能力，不带 TLS、网页、Flutter、Rust 或新的大型库。预算对象包含 JNI 事件、UDP/socket、HTTP 分帧和路由、FD 流、哈希/取消以及视频转交适配。若要引入通用网络框架、静态链接另一套 STL、重建全部界面或实现 TLS，应另算。

| 文件/方案 | 估算方法 | 发布目标 |
|---|---|---|
| 独立 `libqpictransfer.so`，每 ABI **完整库文件** | 该 ABI 的实测解析器 ELF + **12–40 KiB/ABI** 工程预算给其余 native 实现 | `armeabi` 约 **29–57 KiB**，`x86` **21–49 KiB**，可选 `arm64-v8a` **20–48 KiB** |
| 合入现有重编 `libqpicjni156.so`，每 ABI | 不再加第二份 ELF 壳。对既有重编库预留 **16–48 KiB/ABI** 的净增长（包括解析器） | 合并后 `.so`：`armeabi` 约 **210–242 KiB**，`x86` **382–414 KiB**，新 `arm64-v8a` 移植约 **300–332 KiB** |
| **保留原图像库 + 独立快传**，签名 release、`armeabi+x86` | 已有解析器对照 **1,953,697 B** + 两 ABI 各 `12–40 KiB` + 压缩后 Java/资源 `12–36 KiB` | 对齐前预算 **1.90–1.98 MiB**；按约 ±32 KiB 包装波动估为 **1.87–2.01 MiB** |
| **改用重编图像库并合入快传**，签名 release、`armeabi+x86` | 已有重编图像库对照 **2,400,013 B** + 两 ABI 各 `16–48 KiB` + 同一份 Java/资源 `12–36 KiB` | 对齐波动计入后约 **2.30–2.45 MiB** |
| **合入新移植的 arm64 图像库**，仅 `arm64-v8a` 的签名 release | 实测单 ABI 基底 **2,100,937 B** + 一份 native 增量 + 同一份 Java/资源 | 对齐波动计入后约 **2.00–2.12 MiB** |
| **合入重编图像库，三 ABI 通用签名 release** | 实测三 ABI 基底 **2,695,001 B** + 三份 native 增量 + 同一份 Java/资源 | 对齐波动计入后约 **2.60–2.78 MiB** |

以上工程预算不是统计置信区间或保证上限。native 预算给未实现代码预留约数个到十个本次 HTTP 解析器的净代码量；Java/资源预算以整套旧 transfer 代码的 23.4 KiB 压缩差值为参照。±32 KiB 是这组包装差异的规划余量，不是 ZIP 规则。签名、工具链和功能取舍都会影响最终大小。这也是建议将「完成的签名 release 和分 ABI 包」而非「源码行数」作为首版验收项的原因。

单 ABI 这里指实际在临时工程只打入这个 ABI 的 APK；三 ABI 指一个通用 APK。两者都不是对 Play 商店 split 下载量的预测。现有 OpenQPic 正式源集未启用 arm64；后两行只在定容副本变更 ABI 过滤并换入 OpenQPicJni 当前的源码重编库。

## 复算

原始机器记录：[size-study.json](size-study.json)。脚本仅在 `app/build` 的临时工程里更改输入，不安装、不启动产物，也不改 LocalSend 或 OpenQPicJni 源码。当前工作区可从 `OpenQPic/` 运行：

```powershell
python -B docs/localsend-http/measure.py `
  --sdk ..\OpenPic\.local\android-project\sdk `
  --gradle-cache ..\OpenPic\.local\android-project\gradle `
  --ndk16 ..\OpenPic\.local\qpic-toolchain\android-ndk-r16b `
  --ndk64 ..\OpenQPicJni\.local\toolchains\android-ndk-r28c
```

脚本按提交取 OpenQPic 的已跟踪文件，并读取 `OpenQPicJni/native-src/build/libs` 中已经编译的 ELF；每个包内的 ELF/DEX 摘要均记录。解析器两份源文件固定提交和 SHA-256，可用 `--pico-dir <目录>` 完全离线供给。重新生成的临时签名密钥不同，不能跨次比较签名块摘要；应比较同一次运行的各组大小、DEX/ELF 摘要和原 debug 控制组。
