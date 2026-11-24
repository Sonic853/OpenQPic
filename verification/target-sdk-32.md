# targetSdk 32 兼容记录

`2026.11.24`：Debug APK、Debug AAB 的 base manifest 和未签名 Release APK 均为 **`minSdkVersion=23`、`targetSdkVersion=32`**。Gradle 验收直接检查两种打包格式的 `uses-sdk`。各制品摘要、三个 ABI 的两套 JNI 及签名校验见[制品记录](localsend-product.json)。

## 与目标版本相关的改动

- Android 6–10 按运行时授权读取、写入共享存储；Android 10 保留该系统支持的旧存储模式。Android 11 起沿用「所有文件访问权限」设置入口，使用公开的 `StorageVolume` 路径和 MediaStore 分组参数。授权返回图库时，重新读取存储卷并作废授权前的空目录缓存。
- 分享、编辑、查看媒体等自定义菜单声明对应的包可见性查询。多选结果通过 `content://` 和 `ClipData` 授予所选文件的读取权。Android 8 起，本地目录的变更订阅和通知使用同一应用内 provider URI。
- 本地视频优先通过有读取授权的 URI 交给播放器。保留原有 HTTP 流媒体入口；视频代理只监听 `127.0.0.1`。快传仍按已有的 `dataSync` 前台服务和不可变通知 PendingIntent 管理任务。

## Android 6.0 ARM 模拟器

- 存储授权和写入：在 API 23 Ranchu 上撤销测试签名 Release 的读写授权后冷启动。点击系统授权对话框，图库不必重启就显示预先放入的图片；在图库中选择并删除该图片，确认文件已从共享存储移除。旧 Classic 镜像的 `vold` 未能为运行中的进程重新挂载存储，因此首次授权以 Ranchu 的结果为准。
- [跨应用分享](../tools/gallery_share_smoke.py)：无存储权限、不同签名的测试应用在图库的单选和多选分享菜单中可见。它实际打开单选的一个 URI 和多选的两个 URI，三个读取结果都与源文件的 SHA-256 一致。
- [接收后图库刷新](received-gallery-smoke.json)：同一个图库界面出现新相册，目录计数从 1 到 3；未收完的图片不显示，返回图库能看到后台收完的视频，Moments 的缩略图也会更新。
- [官方 LocalSend 对端](localsend-smoke.json)：选图和 Home 后停止发现；已批准的接收、后续文件和返回后的发送继续完成，通知取消有效，伪装成媒体的文件在发布前被拒绝。
- [五台设备发送](multisend-smoke.json)：三台并行等待确认；分别取消一个排队任务和一个进行中任务，一台拒收；关闭界面后剩余两台各自收到两个完整文件，SHA-256 均匹配。
- `video_proxy_smoke.py` 使用真实 MP4：回环代理全量读取返回 200、指定和后缀 Range 返回 206、越界返回 416，字节内容匹配。另在测试签名的 Release 上用 H.264 视频分别走 MediaStore URI 和回环 HTTP 播放，两次都在播放器截图中看到了预期的蓝色画面。

## Android 14 的旧版提示

Android 14 AOSP 的 [`AppWarnings.showDeprecatedTargetDialogIfNeeded`](https://android.googlesource.com/platform/frameworks/base/+/refs/tags/android-14.0.0_r1/services/core/java/com/android/server/wm/AppWarnings.java) 只在应用目标版本小于系统阈值时显示旧版启动对话框。官方 API 34 AOSP 镜像的 `ro.build.version.min_supported_target_sdk=28`，本次产物的 32 高于该阈值。

这是平台规则和实际 Manifest 的核对。本次 Windows 测试机未安装模拟器加速驱动；官方 API 34 镜像在软件仿真下未启动至可用桌面，因此没有 Android 14 的启动、授权、后台传输或 Play Protect 安装界面实测结果。复验应使用支持 `arm64-v8a` 或 32 位 `x86` 的测试设备；本次下载的纯 AOSP x86_64 镜像只声明 `x86_64`，没有此应用的匹配 ABI。

在 Android 14 上复验时，可在共享相册预放一张照片，全新安装后从桌面打开图库，观察旧版启动提示和「所有文件访问权限」设置；授权返回后检查照片是否可见。后台快传可用一个较大视频验证：批准后按 Home，传完再返回，核对文件和相册刷新。

## 复验跨应用分享

在已授予图库存储访问权限的专用模拟器上，安装 Debug APK 后运行。`--sdk` 指向构建本工程所用的 SDK（`local.properties` 的 `sdk.dir`，含 Platform 36 和 Build Tools 36.0.0）：

```powershell
python tools/gallery_share_smoke.py --serial <模拟器> --sdk <SDK目录>
```

脚本使用 Pillow 生成两张不同的图片，安装临时签名、无存储权限的接收应用，并从图库实际点选发送。单张和多张都要求接收方能打开所有 URI，且 SHA-256 与原图一致；结束后移除测试文件和接收应用。
