# 回环视频复用 `libqpiclocalsend.so`

在 `OpenQPicLocalSend` 的 `Wire` 中为视频增加一个可读取 `Range` 的请求入口。视频使用独立的 `127.0.0.1` 临时端口，保留原图库 URI 授权、随机播放路径和空闲关闭；不启动快传会话、组播或发现。此迁移阶段两条路径均为明文 HTTP；后续的 HTTPS 实现见 [Android 系统 TLS](system-tls.md)。

可寻址的 `AssetFileDescriptor` 由 native 使用 64 位偏移发送，遵守资产声明的起始位置和长度；不可寻址的 FD 保留 Java 顺序读取。回环端点支持 `GET` 和 `HEAD`，一次连接只处理一个请求。

## 构建与体积

对照使用 HttpCore 4.4.16 的制品（两边均为 JDK 25、`2026.11.25`）：

| 制品 | 切换前 | 切换后 | 减少 |
| --- | ---: | ---: | ---: |
| Debug APK | 2,432,744 B | 2,310,654 B | 122,090 B |
| 未签名 Release APK | 2,233,155 B | 2,141,503 B | 91,652 B |
| Debug AAB | 1,589,630 B | 1,476,665 B | 112,965 B |

JDK 25 的 `:app:clean :app:verifyLocalSendDebug :app:assembleRelease` 通过；JDK 21 在空目录禁用构建缓存离线重建也通过，85 个任务均实际执行。三份制品均不含 `org.apache.http` 或 `androidx.core` 类型定义和引用。`minSdk 23`、`targetSdk 32`、原 918 个资源 ID、Manifest、三份图像 JNI 不变；许可证未进包。原图库应用保留的 621 个本地 Java 类型、2,251 个字段及 4,123 个方法与原制品声明匹配。对照升级前 DEX，14 个 `LocalSend` 类排除调试信息后相同。

三种 ABI 的传输库已重新构建并核对 JNI 导出。`armeabi`、`x86` 的库大小不变；`arm64-v8a` 增加 1,504 B，使用本地 r16b 产物的两个 `LOAD` 均按 `0x10000` 对齐，满足 16 KiB ELF 对齐检查。

## Android 6.0 ARM 模拟器

测试使用 Debug APK（SHA-256 `974060267be338211503d143fdf173f07525c647743d08f2abbcb40b69d46a59`）。

- [视频测试](../tools/video_proxy_smoke.py)以真实 MediaStore MP4 验证完整 GET、HEAD、闭合／开放／后缀 Range、越界 416、无效授权和播放器启动。视频端点只监听回环，未启动快传端口或组播。
- 同一测试的 `--sdk` 模式使用独立文档提供者：核对非零起始偏移的资产、声明长度边界、含空格和加号的文件名、同目录字幕，以及不可 seek 的管道。管道可顺序读取或从零开始返回范围，不尝试定位到非零偏移。
- [官方 LocalSend 对端](../tools/localsend_smoke.py)收发通过。离开界面停止发现；已授权任务继续；图片、视频、音频有效，非媒体或伪装媒体不发布。返回快传历史和从后台通知取消均正常。
- 同一明文 APK 的五设备队列测试通过：最多 3 个并发报价，排队和运行中的取消互不影响；界面关闭后，另外两台对端仍收到完整文件。

在专用的 root 模拟器上安装 Debug APK 后，运行 `python tools/video_proxy_smoke.py --serial <序列号> --mp4 <测试视频> --sdk <SDK目录>` 可重跑视频用例；省略 `--sdk` 则只测试 MediaStore 路径。
