# HttpCore 和 DocumentFile 首次迁移到 Maven

以[未用库清理后的 APK/AAB](library-cleanup.md)为对照。Gradle 从 Maven Central 下载 HttpCore `4.4.1`，从 Google Maven 下载 DocumentFile `1.0.1`，替代仓库中的 246 份 Java 源码，保留两库的全部 259 个原类型。DocumentFile 使用的注解库只在编译时可见；没有重新引入 `androidx.core`。

## 构建和制品

- JDK 25 下载依赖并通过 `:app:verifyLocalSendDebug :app:assembleRelease`。JDK 21 在空目录禁用构建缓存离线重建，83 个任务全部实际执行，Release 的 `lintVital` 通过。
- Debug APK/AAB 的 DEX 一致。与切换前相比，只新增 AGP 为无资源的 DocumentFile 生成的空 `R` 类；Release 还多出两个 D8 兼容辅助类。与原图库类型清单核对，885 个保留类型的 2,984 个字段、5,899 个方法仍在。
- Manifest、打包资源和六份 JNI 的 ZIP 条目逐项相同，仍为 `minSdk 23`、`targetSdk 32`，原有 918 个资源 ID 不变。未打包许可证、HttpCore 版本属性或 Maven 元数据。

## Android 6.0 ARM 模拟器

设备测试使用 Debug APK（SHA-256 `00925b0116e87a02726e698d7c17de276e336ca965035bfd13f8c6c21a9298e0`）。

- **DocumentFile：** 在「设置 → 相册」启用外部文件浏览器后，从图库的选目录页点「添加」，进入系统目录选择器。在内部存储中授权一个空的测试目录，返回图库后确认现有回调通过 `DocumentFile` 创建了 `!qpicfake.jpg`。
- **HttpCore：** [回环视频测试](../tools/video_proxy_smoke.py)使用 MediaStore 中的真实 MP4。服务仅监听 `127.0.0.1`，完整、区间、后缀和越界请求分别返回预期的 `200`、`206`、`206`、`416`，图库播放器可打开。
