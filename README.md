# OpenQPic

Android 相册，保留原图库、播放、分享和「WiFi 快传」界面。**LocalSend v2.2 默认以 Android 系统 TLS 接收。只有快传界面打开时才可发现和接收新请求；已批准的任务可在后台继续。**

## 使用快传

1. 两台设备连接同一局域网。快传按接收设备公布的协议发送，设备行显示 HTTPS 或 HTTP。菜单中的「接收时使用 HTTPS」默认开启；只在需要接收仅支持明文 HTTP 的发送端时关闭。证书或握手错误不会自动降级。
2. 从图库、桌面快捷方式或系统分享打开快传。「发送」页显示设备名称、类型或型号、协议和地址；重复公告不重复添加设备，长时间无响应的设备会移除。`+` 打开图库多选，也可点「通过 IP 连接」输入 `IPv4[:端口]`，需要指定对端协议时可加 `http://` 或 `https://`。
   点设备即可单独发送。要把同一批文件发给多台设备，点「多选」，勾选接收设备（可混选 HTTP 和 HTTPS），再点「发送（台数）」。最多同时向 3 台发送，其余自动排队。每台设备在传输列表中单独显示进度，可单独取消。
3. **只接收图片、视频、音频。** 独立接收提示页列出发送者、保存位置、可选媒体、选中大小和跳过数量。非媒体不会获得上传 token，伪装成媒体的内容不会发布。发送端仍可分享其他文件。默认保存到 `Download/QP Transfer`；未授权写入公共存储的旧系统显示实际应用目录，同名文件不覆盖。
4. 离开快传、打开选图、按 Home 或返回图库都不会取消任务。可从前台服务通知返回传输列表，手动取消对应任务；只有一个任务时也可从通知直接取消。接收提示页按返回是稍后处理，可从列表重新打开。
5. 收到图片或视频后，已打开的相册列表和接收目录会自动更新缩略图、数量；在快传页收完再返回图库也无需重新打开。

离开快传即退出组播，不回应设备信息、注册或新传输请求。有任务时仅保留该会话的上传和取消入口；最后一个任务结束且界面未打开时，关闭监听和服务通知。视频优先通过 `content://` 授权播放；使用回环代理时，内置播放器走固定本机证书的 HTTPS，第三方播放器仍可使用原有 HTTP 回退。代理只监听 `127.0.0.1`，与快传不共享端口、身份或发现状态。

## 本地图库与更新

在看图时或多选照片、视频后，点顶部分享图标先打开快图自己的菜单。点 **「其它…」** 进入 Android 系统分享面板，也可以直接点菜单中的应用或 WiFi 快传。两种入口都只授权读取所选媒体。

侧栏只列出相册、活动、相机、快传和设置。旧网盘登录、500px、云备份和友盟统计已移除。照片地点使用 Android 系统 Geocoder，不再把坐标发给旧版内置的百度或阿里接口。

「设置 → 关于 → 开源代码许可」打开本仓库的 [`licenses/`](licenses/) 目录。许可及版权声明只保存在仓库，不打包进 APK/AAB。

「设置 → 更新」从 [Sonic853/OpenQPic 的正式 GitHub Releases](https://github.com/Sonic853/OpenQPic/releases) 检查新版本。启动时检查默认关闭。发现新版后可打开该 Release 的 APK 下载；如果没有标准名称的附件，则打开对应发布页。

版本按北京时间 UTC+8 取 `yyyy.MM.dd`，同日再次发布为 `p1`、`p2`……。本地构建默认使用当天日期。PowerShell 示例：`.\gradlew.bat :app:assembleDebug '-PopenqpicVersion=2026.11.24p1'`。APK、AAB、更新设置和关于页使用同一个版本。`versionCode` 为 `10000000 + 距 2020-01-01 的天数 × 65536 + 当日补丁号`，可从同签名的 `9.7` 升级。

手动运行 [Android build](.github/workflows/android-build.yml) 默认只构建。勾选 `publish` 时，工作流串行查询所有标签和 Releases，先分配版本，再编译、签名并发布 `OpenQPic-<版本>.apk`。发布需配置 `ANDROID_KEYSTORE_BASE64`、`ANDROID_STORE_PASSWORD`、`ANDROID_KEY_ALIAS`、`ANDROID_KEY_PASSWORD`，并沿用已发布 APK 的签名。

## 构建

用 Android Studio 打开本仓库根目录。需要 JDK 21 或 25、Android SDK Platform 36 和 Build Tools 36.0.0。默认产物 `com.alensw.PicFolder` 最低支持 Android 6.0（`minSdk 23`），目标为 Android 12L（`targetSdk 32`）；已包含 `armeabi`、`x86` 和 `arm64-v8a` 三组匹配的 JNI 库。**普通构建不需要 NDK、恢复材料或相邻仓库**。

```powershell
.\gradlew.bat :app:assembleDebug          # app/build/outputs/apk/debug/app-debug.apk
.\gradlew.bat :app:bundleDebug            # app/build/outputs/bundle/debug/app-debug.aab
.\gradlew.bat :app:assembleRelease        # app/build/outputs/apk/release/app-release-unsigned.apk
.\gradlew.bat :app:verifyLocalSendDebug   # 额外需要 Python 3.10+
```

首次构建从 Maven Central 和 Google Maven 下载依赖；缓存后可追加 `--offline`。从旧版本切换且已有旧构建缓存时，先运行一次 `:app:clean`。本工作区可用 `$env:GRADLE_USER_HOME=(Resolve-Path '..\OpenPic\.local\android-project\gradle').Path` 复用缓存。

| 模块 | 来源 / 编辑位置 |
|---|---|
| 图库、播放器、定制 Support 控件 | `app/src/recovered/java/`，与 `app/src/main/java/` 一起由 javac 编译。见 [Java 恢复记录](docs/recovery/gallery-java.md)。 |
| Android 文档访问 | Gradle 从 Google Maven 引入 [AndroidX DocumentFile 1.1.0](docs/recovery/android-support.md)；不引入 `androidx.core`。 |
| 资源、系统分享 | `app/src/main/res/`、`app/resource-ids.txt`、`app/src/main/java/com/alensw/ui/activity/GalleryShareMenu.java`。 |
| Android 快传界面、授权与文件提交 | `app/src/main/java/com/alensw/transfer/`；复用 `transfer_send`、`list_view`、`transfer_list_item`、`ImageGridView` 和原双页导航。 |
| 图像 JNI | **OpenQPicJni** 的 `libqpicjni156.so`，含该仓库从 32 位源码移植的 arm64 版本。 |
| 发现、HTTPS/HTTP、FD 传输、会话与 JNI 桥 | 独立仓库 **OpenQPicLocalSend**。快传和回环视频都用 `libqpiclocalsend.so` 解析 HTTP；TLS 由 Android 提供。应用内 `org/openqpic/localsend` 是配套源码副本。 |

原生开发时，将 `OpenQPicJni`、`OpenQPicLocalSend` 与本仓库并排放置，分别按其 README 构建库，再运行 `python tools/sync_localsend.py`。同步后的文件摘要在 [`app/native-inputs.json`](app/native-inputs.json)。最终制品按[原始类型清单](app/recovered-types.txt)、[已移除的未用库](app/removed-unused-library-types.txt)及[新增产品类](app/product-java.txt)核对。

## 验证

`verifyLocalSendDebug` 检查实际 APK/AAB 的最低和目标 SDK、两套 JNI、DEX、原有 918 个资源 ID、签名与对齐。`tools/localsend_smoke.py --tls` 和 `tools/multisend_smoke.py --tls` 用官方 LocalSend 核心验证加密收发、指纹校验、取消和后台队列。见[系统 TLS 实测与体积](verification/system-tls.md)、[快传实现](docs/localsend-http/IMPLEMENTATION.md)及 [targetSdk 32 兼容记录](verification/target-sdk-32.md)。

[`tools/gallery_share_smoke.py`](tools/gallery_share_smoke.py) 用无存储权限的独立应用从系统面板和直达菜单读取单张、多张、视频及混合媒体并核对 SHA-256。[回环视频迁移验证](verification/native-video.md)记录脱离 HttpCore 后的制品与测试；[原版保真验证](STABLE-BASELINE.md)及[早期协议与定容分析](docs/localsend-http/README.md)是历史记录。
