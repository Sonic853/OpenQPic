# AndroidX 与原 Support 控件

应用从 Google Maven 引入四个 AndroidX 库。版本兼容 `compileSdk 36`、AGP `9.0.1` 和 `minSdk 23`。所有传递制品的坐标和 SHA-256 见 [`app/runtime-libraries.sha256`](../../app/runtime-libraries.sha256)。

| 组件 | 使用位置 |
|---|---|
| Core `1.17.0` | `androidx.core.content.FileProvider` 继续使用 `com.alensw.PicFolder.provider`、`@xml/provider_paths` 的 `external_files` 根和标准元数据键 `android.support.FILE_PROVIDER_PATHS`。图库分享回退用的是另一个 authority：`com.alensw.PicFolder.FileProvider`。 |
| DocumentFile `1.1.0` | 目录授权仍用 `DocumentFile.fromTreeUri`。发布版直接调用 Core 的 `DocumentsContractCompat`，不再做字节码重写。 |
| DrawerLayout `1.2.0` | `GalleryDrawerLayout` 保留快图系统栏边距和遮罩；拖动、锁定、无障碍和抽屉状态使用官方实现。`GalleryDrawerToggle` 同步标题、焦点及快图原来的箭头动画。面板在两种书写方向下都位于屏幕左侧。 |
| ViewPager `1.1.0` | `TransferActivity` 使用官方 `ViewPager` 和 `PagerAdapter` 承载发送和传输列表两页；标签、通知和 Activity 重建使用同一页码。 |

图库和播放器的滚动、回弹使用系统 `OverScroller` 与 `EdgeEffect`。原 `android.support` 源码的 **57 份 Java、60 个类型**均已替换。官方 Core 的 `classes.jar` 自带 21 个 `android.support.v4` 名称，包括 Binder 接口和 Parcelizer；它们是上游的跨进程兼容实现。校验逐类核对其归属。

AndroidX 使用自己的抽屉 `SavedState`。首次恢复旧版本留下的 Activity 时，图库保留页面栈、当前目录及根目录选择，跳过含旧 Parcelable 类名的窗口视图状态。之后的重建直接恢复 AndroidX 的抽屉状态；相册、全屏看图和选图模式各自按当前页面设置锁定。

## 资源与验收

编辑入口是 `app/src/main/res`。AAPT2 链接完整 AAR 资源并给应用资源最高覆盖优先级；主分支已有的 995 个资源 ID（含原版 918 个）保持不变。Java 使用同一张最终 ID 表，只有 `string/default`、`string/interface` 在 `R` 中使用合法别名。`:app:verifyLocalSendDebug` 比对 APK、AAB、Java 符号、Maven 类、JNI 和仓库专用许可证。

从 `44bdffb` 的 Debug APK（`2026.11.27`）比对 `2026.11.28`：995 个旧资源的各配置值只有版本字符串不同；233 份 `res/`、`assets/` 文件中，只有替换抽屉类名的 `res/layout/main.xml` 字节不同。Debug 和 Release 的 APK、AAB 均有 1,163 个资源 ID。Debug 保留官方 Core `classes.jar` 的全部 936 个类型。

Release 的 [R8 规则](../../app/proguard-rules.pro)保留图库、JNI 入口、DocumentFile、DrawerLayout 和 ViewPager，只裁剪未使用的库代码，不混淆、不内联、不裁资源。`2026.11.28` 的 Release 保留 86 个官方 Core 类型，包括 `FileProvider`、`DocumentsContractCompat` 和 `ViewCompat`。`FileProvider.getUriForFile` 的两个公开重载须显式保留；`:app:verifyLocalSendRelease` 从 DEX 核对签名。三个 ABI 的六份 `.so` 以 ZIP 压缩写入通用 APK，解压后的 SHA-256 不变。同版本未签名 APK 从 3.85 MiB 降到 1.81 MiB，AAB 从 2.97 MiB 降到 1.66 MiB。

裁剪后的 Release APK 使用测试密钥在 Android 6.0 ARM 上覆盖安装：旧图库页面状态、抽屉、快传分页、系统目录授权和两套 JNI 调用均通过。独立接收应用通过两个 FileProvider 的只读授权读回了相同内容；下述八组图库分享用例也在该 Release 上通过。正式签名的精简 APK 也已在 Android 6.0 上覆盖同版本未裁剪的正式包，图库成功启动，UID、首次安装时间和存储授权均保留。

Android 6.0 和 9 上均从 `44bdffb` 同签名覆盖安装，并恢复了由旧版实际保存的 Activity 状态：图库页面栈、相册路径及根目录选择保持一致。上滑详情从第 5 张切到第 6 张时标题由 `5/11` 更新为 `6/11`，相册重命名后返回列表显示新名称。

在 Android 6.0 ARM 模拟器上，[图库分享回归](../../tools/gallery_share_smoke.py)覆盖了看图页和多选页的「其它…」、系统分享面板、直接分享、裁剪后分享，以及快传待发送列表。无存储权限的接收应用以不同 UID 读到了正确的原件或裁剪结果，单图、视频与混合多选均核对了 MIME、只读授权及内容摘要。

Android 9（x86）实测了工具栏与左边缘开抽屉、返回关闭、重建时的标题与菜单，以及选图模式的锁定。快传的标签点击、横滑、通知使用的传输列表入口和两页在重建后的恢复均通过。两个 FileProvider 各自发出的 URI 均由另一 UID 以只读授权读回；通过系统目录选择器取得持久授权后，DocumentFile 的创建、读写和删除均成功。GIF 解码开关、APNG、动画 WebP 的文件及 `content://` 打开、切出再进入、透明 PNG 缩略图与预览缓存也已复测。

## 恢复历史与许可

- [迁移前的源码、原行为与原验证记录](https://github.com/Sonic853/OpenQPic/blob/44bdffb/docs/recovery/android-support.md)，以及[原 DEX 逐类型映射](android-support-types.tsv)。
- AndroidX 源码随相应版本的 `-sources.jar` 发布。Android Open Source Project 的版权声明与 [Apache License 2.0](../../licenses/android-support-apache-2.0.txt) 见[许可目录](../../licenses/)。
