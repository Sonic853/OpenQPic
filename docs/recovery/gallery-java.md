# 从产品 Smali 到可编译的 Java

行为基线是 `aa0043e` 的 **targetSdk 32 产品**，其中已经删除旧网盘、统计和快传协议，包含日期版本、GitHub 更新、存储兼容和后台传输。调试基线 APK 的 SHA-256 为 `15eb10ad578f08253938c2d198268b2405a4cf8bf59324c50cbf2a344fcdb321`。历史原版的反编译阅读稿不作为构建输入。

## 从哪里编辑

| 代码 | 位置及来源 |
|---|---|
| 图库、编辑、缩略图、播放、设置、媒体模型 | `app/src/recovered/java/com/alensw/`。依据产品的 DEX/Smali 恢复；使用 dex2jar、Vineflower、JADX 和 CFR 交叉结构化，再按原指令修正控制流和 Java 重载。`gallery/` 管理扫描、相册和加载，`image/` 负责解码和位图，`ui/page/` 包含相册、搜索和全屏看图页面。 |
| 抽屉、分页与文件授权 | [AndroidX 替换关系和版本](android-support.md)。快图箭头与遮罩在应用包内，原 60 个 Support 类型已替换。 |
| Maven 库 | [运行时制品清单](../../app/runtime-libraries.sha256)，包括完整 Core 和 DocumentFile；不保留源码副本。 |
| HTTP 回环视频 | `app/src/main/java/com/alensw/video/` 调用配套的 `org.openqpic.localsend.LoopbackHttp` 与 `libqpiclocalsend.so`。不再使用 [HttpCore](httpcore.md)。 |
| 系统分享、存储兼容、快传、JNI 桥、更新 | `app/src/main/java/`。`GalleryShareMenu` 作为原菜单的 Java 实现；裁剪界面等图片渲染完毕才分享结果 URI。 |

`app/src/main/smali` 原有 1,471 份文件；其中 216 个类型在迁移前已有 Java 实现，另外 1,255 个由单独的 Smali DEX 提供。连同一直用 Java 编译的 `JniUtils`，迁移时共保留了 **1,472 个类型**。之后移除了 [587 个未用库类型](../../app/removed-unused-library-types.txt)、HttpCore 的 254 个原类型和[五个服务器适配器](../../app/replaced-video-adapters.txt)，AndroidX 又替换了 60 个旧 Support 类型。[原始类型清单](../../app/recovered-types.txt)记录了应用类名的变化：

| 旧名 | 源码及 DEX 新名 | 原因 |
|---|---|---|
| `com.alensw.ui.c.do` | `com.alensw.ui.page.DateChangedCallback` | 媒体日期变更回调；`do` 是 Java 关键字。 |
| `com.alensw.PicFolder.r` | `com.alensw.PicFolder.FaceDetectionTask` | 在 Windows 的文件系统上会和 Android 生成的 `R.class` 冲突。 |
| `com.alensw.PicFolder.ag` | `com.alensw.PicFolder.GalleryDrawerToggle` | 用 AndroidX 抽屉回调同步图库页面和工具栏。 |
| `com.alensw.b.h.b` | `com.alensw.image.BitmapUtils` | 位图解码、缩放和缩略图。 |
| `com.alensw.a.al` | `com.alensw.gallery.NativeFolderScanner` | 调用 JNI 扫描目录及媒体。 |
| `com.alensw.ui.c.dp` | `com.alensw.ui.page.LocalViewerPage` | 浏览本地目录中的单张图片。 |

恢复源码中原先的 515 个短文件名已按实现和调用关系命名；这些是维护用名称，不代表原作者的符号。查旧 Smali 时，用 DEX 描述符查上面的类型清单，第一列是旧名，第二列是当前名。[页面栈](../../app/src/recovered/java/com/alensw/ui/page/GalleryPageActivity.java)继续用旧版本的六个页面标识读写保存状态：当 Android 交回旧 `Bundle` 时，可以恢复原页面；若系统在替换 APK 时移除了任务，下次启动仍按原有逻辑打开图库。Manifest 组件、Provider authority、Parcelable 媒体模型和 JNI 入口仍沿用其公开名称。

构建走 AGP 的 `javac / Maven 依赖 → D8 → APK/AAB`。保留清单中的 566 个图库类型有 5 个来自 DocumentFile，其余由本地 Java 编译。从前移除的 Core 源码不会参与构建；现在只使用官方 Maven 制品中的实现。

恢复时也对照了调用的具体签名。例如，设置相册标题的 `b(CharSequence)` 和查询字符串的 `b(String)` 不能互换；选目录的 `a(File)` 不同于打开相册的 `a(ScannedFolder)`。裁剪宽高比及快速滚动条的滑块长度保留原来的浮点除法。图库模型还核对了游标关闭、SQLite 事务和写入队列的锁范围。宿主 JVM 上，52 组模型行为、83,168 条 SVG 数字扫描轨迹、2,000 条 SVG 路径和 576 条图像/GIF 轨迹与 DEX 转译对照一致。这些是受控差分，不替代 Android 实机或模拟器测试。

## 如何核对

- `:app:verifyLocalSendDebug` 将固定保留类型逐一与 **javac 的 `.class`、Maven 库的 `.class` 和最终 DEX** 对照，也检查已移除类型没有定义或引用；另检查 APK/AAB 一致性、运行库归属、原有资源 ID、简中名称、SDK、签名和六份 JNI。
- `python tools/compare_java_recovery.py <旧.apk> <新.apk> --local-only` 可与 `aa0043e` 恢复基线逐项比较本地类声明。它也会列出后续有意改写的 GIF 解码器、看图手势及 AndroidX 控件的签名差异；当前制品的验收使用上面的 Gradle 任务。
- 普通构建和验证不需要反编译器、旧 APK 或相邻源码仓库。`tools/gallery_share_smoke.py` 与 `tools/*send_smoke.py` 可另外在专用设备验证系统面板、跨应用读取和实际传输。

## 初次 Java 迁移的制品与分享

后续的未用库移除见[单独的验证记录](../../verification/library-cleanup.md)。

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
