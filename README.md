# OpenQPic

Android 相册，保留原图库、播放、分享和「WiFi 快传」界面。**LocalSend v2.2 明文快传仅在界面打开时可被发现和接收新请求；已经开始的传输可以在后台继续。**

## 使用快传

1. 两台设备连接同一局域网，在 LocalSend 对端关闭加密。
2. 从图库、桌面快捷方式或系统分享打开快传。「发送」页优先列出可发送的设备，显示名称、类型或型号及地址；重复公告不重复添加设备，长时间无响应的设备会移除。`+` 打开图库多选，也可点「通过 IP 连接」输入 `IPv4[:端口]`。
   点设备即可单独发送。要把同一批文件发给多台设备，点「多选」，勾选接收设备，再点「发送（台数）」。最多同时向 3 台发送，其余自动排队。每台设备在传输列表中单独显示进度，可单独取消。
3. **只接收图片、视频、音频。** 独立接收提示页列出发送者、保存位置、可选媒体、选中大小和跳过数量。非媒体不会获得上传 token，伪装成媒体的内容不会发布。发送端仍可分享其他文件。默认保存到 `Download/QP Transfer`；未授权写入公共存储的旧系统显示实际应用目录，同名文件不覆盖。
4. 离开快传、打开选图、按 Home 或返回图库都不会取消任务。可从前台服务通知返回传输列表，手动取消对应任务；只有一个任务时也可从通知直接取消。接收提示页按返回是稍后处理，可从列表重新打开。
5. 收到图片或视频后，已打开的相册列表和接收目录会自动更新缩略图、数量；在快传页收完再返回图库也无需重新打开。

离开快传即退出组播，不回应设备信息、注册或新传输请求。有任务时仅保留该会话的上传和取消入口；最后一个任务结束且界面未打开时，关闭监听和服务通知。传输使用原连接，实际断网、对端关闭或存储错误仍可能导致失败。视频可通过系统 `content://` 授权播放；需要 HTTP 的视频代理独立运行，仅监听 `127.0.0.1`。

## 本地图库与更新

侧栏只列出相册、活动、相机、快传和设置。旧网盘登录、500px、云备份和友盟统计已移除。照片地点使用 Android 系统 Geocoder，不再把坐标发给旧版内置的百度或阿里接口。

「设置 → 更新」从 [Sonic853/OpenQPic 的正式 GitHub Releases](https://github.com/Sonic853/OpenQPic/releases) 检查新版本。启动时检查默认关闭。发现新版后可打开该 Release 的 APK 下载；如果没有标准名称的附件，则打开对应发布页。

版本按北京时间 UTC+8 取 `yyyy.MM.dd`，同日再次发布为 `p1`、`p2`……。本地构建默认使用当天日期。PowerShell 示例：`.\gradlew.bat :app:assembleDebug '-PopenqpicVersion=2026.11.24p1'`。APK、AAB、更新设置和关于页使用同一个版本。`versionCode` 为 `10000000 + 距 2020-01-01 的天数 × 65536 + 当日补丁号`，可从同签名的 `9.7` 升级。

手动运行 [Android build](.github/workflows/android-build.yml) 默认只构建。勾选 `publish` 时，工作流串行查询所有标签和 Releases，先分配版本，再编译、签名并发布 `OpenQPic-<版本>.apk`。发布需配置 `ANDROID_KEYSTORE_BASE64`、`ANDROID_STORE_PASSWORD`、`ANDROID_KEY_ALIAS`、`ANDROID_KEY_PASSWORD`，并沿用已发布 APK 的签名。

## 构建

用 Android Studio 打开本仓库根目录。需要 JDK 21 或 25、Android SDK Platform 36 和 Build Tools 36.0.0。默认产物 `com.alensw.PicFolder`，min/target 23；已包含 `armeabi`、`x86` 和 `arm64-v8a` 三组匹配的 JNI 库。**普通构建不需要 NDK、恢复材料或相邻仓库**。

```powershell
.\gradlew.bat :app:assembleDebug          # app/build/outputs/apk/debug/app-debug.apk
.\gradlew.bat :app:bundleDebug            # app/build/outputs/bundle/debug/app-debug.aab
.\gradlew.bat :app:assembleRelease        # app/build/outputs/apk/release/app-release-unsigned.apk
.\gradlew.bat :app:verifyLocalSendDebug   # 额外需要 Python 3.10+
```

从原保真版本切换且已有旧构建缓存时，先运行一次 `:app:clean`。本工作区的离线依赖可设 `$env:GRADLE_USER_HOME=(Resolve-Path '..\OpenPic\.local\android-project\gradle').Path`，追加 `--offline`。

| 模块 | 来源 / 编辑位置 |
|---|---|
| 图库、原控件、资源 ID | `app/src/recovered/java`、`app/src/main/smali`、`app/src/main/res`；DEX 恢复与构建机制见[原版基线](STABLE-BASELINE.md)。 |
| Android 快传界面、授权与文件提交 | `app/src/main/java/com/alensw/transfer/`；复用 `transfer_send`、`list_view`、`transfer_list_item`、`ImageGridView` 和原双页导航。 |
| 图像 JNI | **OpenQPicJni** 的 `libqpicjni156.so`，含该仓库从 32 位源码移植的 arm64 版本。 |
| 发现、HTTP、FD 传输、会话与 JNI 桥 | 独立仓库 **OpenQPicLocalSend**。`libqpiclocalsend.so` 进入快传才加载；应用内 `org/openqpic/localsend` 是与该库配套的源码副本。 |

原生开发时，将 `OpenQPicJni`、`OpenQPicLocalSend` 与本仓库并排放置，分别按其 README 构建库，再运行 `python tools/sync_localsend.py`。同步后的文件摘要在 [`app/native-inputs.json`](app/native-inputs.json)。新增 Java 归属见 [`app/product-java.txt`](app/product-java.txt)；不再编译原 QPIC/PeerHub 或反向拉取代码。

## 验证

`verifyLocalSendDebug` 检查实际 APK/AAB 的两套 JNI、DEX、Manifest、原有 918 个资源 ID、签名与对齐。`tools/localsend_smoke.py` 和 `tools/multisend_smoke.py` 用官方 Rust HTTP 核心作对端，验证单机及多机收发、文件内容、取消和后台行为。详见[实现与实测](docs/localsend-http/IMPLEMENTATION.md)。

[原版保真验证](verification/README.md)及[早期协议与定容分析](docs/localsend-http/README.md)保留作历史对照；`verifyStableDebug` 仍按原版判定。
