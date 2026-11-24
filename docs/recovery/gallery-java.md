# 从产品 Smali 到可编译的 Java

行为基线是 `aa0043e` 的 **targetSdk 32 产品**，其中已经删除旧网盘、统计和快传协议，包含日期版本、GitHub 更新、存储兼容和后台传输。调试基线 APK 的 SHA-256 为 `15eb10ad578f08253938c2d198268b2405a4cf8bf59324c50cbf2a344fcdb321`。历史原版的反编译阅读稿不作为构建输入。

## 从哪里编辑

| 代码 | 位置及来源 |
|---|---|
| 图库、编辑、缩略图、播放、设置、媒体模型 | `app/src/recovered/java/com/`。依据当前产品的 DEX/Smali 恢复；使用 dex2jar、Vineflower、JADX 和 CFR 交叉结构化，再按原指令修正控制流和 Java 重载。现有类型及对外调用尽量沿用原来的名字。 |
| 随包 Support 库、AndroidX | [`android/`、`androidx/` 的逐类映射和版本说明](android-support.md)，78 个类型。 |
| Apache HTTP、Codec、Logging | [上游版本、许可证、逐类清单与比对](../../app/src/recovered/java/org/apache/RECOVERY.md)，828 个类型。 |
| 系统分享、存储兼容、快传、JNI 桥、更新 | `app/src/main/java/`。`GalleryShareMenu` 作为原菜单的 Java 实现；裁剪界面等图片渲染完毕才分享结果 URI。 |

`app/src/main/smali` 原有 1,471 份文件；其中 216 个类型在迁移前已有 Java 实现，另外 1,255 个由单独的 Smali DEX 提供。连同一直用 Java 编译的 `JniUtils`，共有 **1,472 个保留类型**。独立维护的[完整类型清单](../../app/recovered-types.txt)也记录以下两个必要的类名变化：

| 旧名 | 源码及 DEX 新名 | 原因 |
|---|---|---|
| `com.alensw.ui.c.do` | `com.alensw.ui.c.DateChangedCallback` | `do` 是 Java 关键字。 |
| `com.alensw.PicFolder.r` | `com.alensw.PicFolder.FaceDetectionTask` | 在 Windows 的文件系统上会和 Android 生成的 `R.class` 冲突。 |

构建走 AGP 标准的 `javac → D8 → APK/AAB`。`compileOnly/apache-optional-api.jar` 只有 Apache 可选桌面 API 的 23 份外部类型声明、无方法体，不含任何需恢复的类型；其生成工具在 Apache 来源说明中。这些类型不进入 APK。Android 平台和外部可选库与产品代码的边界，和 `aa0043e` 相同。

恢复源目录有 1,338 份 `.java`；内部类与匿名类可以和外层类在同一文件。加上产品 Java 和共享的版本类，javac 生成 1,523 个 `.class`。Debug APK 的 1,593 个 DEX 类型还包括 AGP 生成的 `R` 类和 D8 的合成类。

恢复时也对照了调用的具体签名。例如，设置相册标题的 `b(CharSequence)` 和查询字符串的 `b(String)` 不能互换；选目录的 `a(File)` 不同于打开相册的 `a(e)`。裁剪宽高比及快速滚动条的滑块长度保留原来的浮点除法。图库模型还核对了游标关闭、SQLite 事务和写入队列的锁范围。宿主 JVM 上，52 组模型行为、83,168 条 SVG 数字扫描轨迹、2,000 条 SVG 路径和 576 条图像/GIF 轨迹与 DEX 转译对照一致。这些是受控差分，不替代 Android 实机或模拟器测试。

## 如何核对

- `:app:verifyLocalSendDebug` 将固定保留类型逐一与 **javac 的 `.class` 和最终 DEX** 对照；另检查 APK/AAB 一致性、运行库归属、原有资源 ID、简中名称、SDK、签名和六份 JNI。
- 手头有迁移前的 APK 时，运行 `python tools/compare_java_recovery.py <旧.apk> <新.apk>`。比较每个保留类型的父类、接口、字段、方法签名及具体方法是否仍有 DEX 指令。它不是字节码一致性证明：现代 javac 和 D8 会重排分支、生成新的辅助成员；完善系统分享也有意改变了调用路径。
- 本次基线有 **4,553 个原字段、9,929 个原方法**，其中 **9,318 个有 DEX 指令**。SDK 36 的 D8 给线程池子类 `com.alensw.b.k.a` 增加了 `AutoCloseable` 和 `close()` 的低版本适配；此差异只在该类上识别。
- 普通构建和验证不需要反编译器、旧 APK 或相邻源码仓库。`tools/gallery_share_smoke.py` 与 `tools/*send_smoke.py` 可另外在专用设备验证系统面板、跨应用读取和实际传输。

## 实际制品与分享

[构建记录](../../verification/java-build.json)包含 Debug APK、Debug AAB、未签名 Release APK 的摘要。除主工作区的 JDK 25 外，还从空目录只复制仓库源文件，用 JDK 21 禁用构建缓存离线重建：83 个任务全部实际执行。两套 JDK 的 Debug 与 Release 均通过原字段、方法与代码存在性比对。Android Studio 的 V2 工程模型识别到 `app/src/main/java`、`app/src/recovered/java` 和 `buildSrc/src/main/java` 三个编译根。Release 的 `lintVital` 通过。

[Android 6.0 ARM 分享记录](../../verification/java-gallery-share.json)：从看图及多选页面点击顶部图标，图库菜单先出现；短菜单与超过六个直接目标的菜单均能通过「其它…」打开系统 `android/com.android.internal.app.ChooserActivity`。无存储权限、不同 UID 的接收应用实际读到了 `EXTRA_STREAM` 与 `ClipData` 对应的相同 URI，逐一核对了 SHA-256 和只读授权。

| 系统面板接收的数据 | Intent 类型 |
|---|---|
| 一张 PNG | `image/png` |
| PNG 和 JPEG | `image/*` |
| 一段 MP4 | `video/mp4` |
| JPEG 和 MP4 | `*/*` |

最终 APK 在简中系统上跑通八组接收验证。裁剪页分享一张 160×160 的原图时，接收应用读到的是新渲染的 72×48 JPEG，哈希与裁剪输出一致。直接点目标、从原来的「更多…」子菜单点目标以及直接点 WiFi 快传也分别验证。安装包中的简中应用与启动入口名称均为「快图」。

复验时先在专用模拟器安装 APK、授予存储读取权限并启用图库显示视频。主机需 JDK、Python/Pillow 和支持 `libx264` 的 FFmpeg。简中界面可运行：

```powershell
python tools/gallery_share_smoke.py --sdk <Android-SDK目录> --serial <模拟器序列号> --other-label "其它…" --report app/build/reports/gallery-share.json
```

[图库、视频和快传回归](../../verification/java-gallery-regression.json)：在已授权的 Android 6.0 ARM 模拟器上，收到图片时原相册从 1 张增至 2 张，后台收完视频后变为 3 项；未完成文件不显示，Moments 随发布刷新。官方 LocalSend 对端收发及五台设备队列通过，离开界面停止发现、已批准的任务继续，单项取消有效。真实 H.264 视频经 MediaStore 和回环 HTTP 两条路径均显示预期蓝色画面。

快传与播放回归之后修正了裁剪宽高比、快速滚动条和锁屏图案格的三个方法。报告分别标出受测和最终 APK；核对最终 DEX 的寄存器、指令、引用、异常表与方法标志，其余 10,496 个方法相同。本轮设备运行验证限于 Android 6.0 ARM 模拟器。
