# HttpCore 4.4.16 与 DocumentFile 1.1.0 升级验证

对照[首次 Maven 迁移](maven-libraries.md)的制品。此阶段的安装包包含 Maven Central 的 HttpCore `4.4.16`（253 个类型）和 Google Maven 的 DocumentFile `1.1.0`（5 个类型）；两库均未恢复为仓库内的 Java 副本。后续[回环视频切换到 `libqpiclocalsend.so`](native-video.md)，不再打包 HttpCore。

## 构建与制品

- JDK 25 禁用构建缓存，运行 `:app:clean :app:verifyLocalSendDebug :app:assembleRelease` 通过。JDK 21 在空目录离线重建，85 个任务全部实际执行。两次构建的 Release `lintVital` 均通过。
- Debug APK/AAB 的 DEX 一致。在这两个制品和未签名 Release APK 中，DocumentFile 对 `isDocumentUri`、`getDocumentId`、`getTreeDocumentId`、`buildDocumentUriUsingTree` 的调用均指向系统 `DocumentsContract`。没有 `androidx.core` 类型定义或引用，也未打包注解库或 Kotlin 运行库。
- HttpCore 上游移除 5 个原类型、增加 4 个类型；应用自身的 **626 个原类型、2,260 个字段、4,152 个方法**全部保留。排除第三方库、D8 合成类和调试记录后，应用 DEX 的 692 个类型与升级前对应内容一致。
- Manifest、资源和六份 JNI 的 ZIP 条目与升级前一致；`minSdk 23`、`targetSdk 32`、918 个原资源 ID 不变。三个制品均未携带库许可证或 HttpCore 的附加元数据。

## Android 6.0 ARM 模拟器

设备测试使用 Debug APK（SHA-256 `2d5a8ce838d042cf06795fc66558fd045361372f1b1156c53accfb23b84c9d8c`）。

- [回环视频测试](../tools/video_proxy_smoke.py)：真实 MP4 的完整、区间、后缀、越界请求分别返回 `200`、`206`、`206`、`416`；服务仍仅监听 `127.0.0.1`，播放器可打开。
- **系统目录授权：** 从图库打开 `ACTION_OPEN_DOCUMENT_TREE`，在系统选择器中实际授予测试目录写权限。返回图库时，DocumentFile 成功在该目录创建 `!qpicfake.jpg`。
