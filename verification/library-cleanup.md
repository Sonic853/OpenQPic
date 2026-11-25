# 未用库清理验证

以 `295c22d` 构建的 APK 为清理前对照。逐类检查其 DEX 中的类型、字段、方法和类名字符串，保留代码没有指向这四组类型的引用。

| 移除的库 | Java 文件 | 原类型 |
|---|---:|---:|
| HttpClient 4.5 | 396 | 461 |
| Commons Codec 1.9 | 56 | 85 |
| Commons Logging 1.2 | 14 | 28 |
| `androidx.core` | 4 | 13 |
| 合计 | 470 | 587 |

这些类型由[移除清单](../app/removed-unused-library-types.txt)精确确定。该阶段 HttpCore、旧 Support 和 DocumentFile 仍从源码编译；之后才为 [HttpCore](../docs/recovery/httpcore.md) 和 [DocumentFile](../docs/recovery/android-support.md) 引入 Maven 依赖。旧 Apache 桌面 API 的编译声明 JAR 及生成脚本已移除。

## 构建与差分

JDK 25、SDK/Build Tools 36 本地禁用构建缓存运行 `:app:clean :app:verifyLocalSendDebug :app:assembleRelease` 通过。另在空目录用 JDK 21 离线重建，83 个任务全部执行并通过验证。SDK 仍为 `minSdk 23`、`targetSdk 32`，918 个原资源 ID 不变。Debug APK 与 Debug AAB 的 DEX 一致；这两个制品以及未签名 Release APK 均不含移除清单中的类型定义、引用或类名字符串，也未打包许可证文件。

| APK | 清理前 | 清理后 | 减少 |
|---|---:|---:|---:|
| Debug | 2,789,572 B / 1,594 类 | 2,437,953 B / 1,007 类 | 351,619 B |
| Release（未签名） | 2,483,191 B / 1,591 类 | 2,229,979 B / 1,004 类 | 253,212 B |

清理前后的 Manifest、资源和六份 JNI 按 ZIP 条目比对，内容不变。以符号取代表索引并忽略调试信息后，Debug 中保留类型的 DEX 声明和代码一致。Release 的 D8 将原本挂在 `ActivityCompat` 下的共用 API 辅助类改挂到 `GalleryStorage`，同时剪掉 9 个不再使用的辅助方法；统一辅助类的名称后，其余保留声明和代码一致。

与 Java 迁移前的 APK 比较，剩余 **885 个原类型、2,984 个原字段、5,899 个原方法**均保留。HttpCore 独立通过 javac → D8 的 254 类签名检查；旧 Support 和 DocumentFile 的 62 份 Java 无需外部类路径即可编译为 65 类。

## Android 6.0 ARM 模拟器

设备测试使用上表的 JDK 25 Debug APK（`2026.11.25`，SHA-256 `272b6079562bad9b8ccf851873059b38f662b41d37900fd78d6abbf73d61e97f`）。

- [分享测试](../tools/gallery_share_smoke.py)：图库顶部分享菜单的系统面板、直接目标和溢出菜单验收通过。八组跨应用接收验证包括 72×48 裁剪结果，另外核对了简中资源名称「快图」「其它…」和图库直达 WiFi 快传。
- [视频测试](../tools/video_proxy_smoke.py)：真实 MP4 从 MediaStore 进入视频服务。服务仅监听 `127.0.0.1`；完整 GET `200`、区间及后缀 Range `206`、越界 Range `416` 均匹配预期，图库播放器可正常打开。
- [图库刷新测试](../tools/received_gallery_smoke.py)：无需重开图库，接收目录从 1 项更新到 2、3 项；未完成的文件不显示，后台收完视频及 Moments 缩略图刷新通过。
- [LocalSend 官方对端测试](../tools/localsend_smoke.py)：图片、音频和视频接收通过，非媒体及伪装媒体未发布；离开界面停止发现，已获批准的收发继续，历史列表和通知均可取消对应传输。
- [五设备队列测试](../tools/multisend_smoke.py)：最多 3 个待接受请求并行，取消运行中或排队项互不影响；对端拒绝后可在界面关闭时启动下一项，两个正常对端收到完整文件。
