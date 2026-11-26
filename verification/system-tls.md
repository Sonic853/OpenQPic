# Android 系统 TLS

本文记录首次加入系统 TLS 的构建和实测。后续按接收方协议选择出站 HTTP/HTTPS 的行为见[快传实现](../docs/localsend-http/IMPLEMENTATION.md)。

快传和回环视频都使用系统 JSSE/Conscrypt，不打包 OpenSSL、mbedTLS 或私钥。每次安装为两条链路分别在 AndroidKeyStore 中生成 P-256 签名密钥和自签名证书。仅启用设备支持的 TLS 1.2 / 1.3，不修改全局证书信任、默认套接字工厂或主机名校验。

- **局域网快传**：默认 HTTPS，使用双向证书。注册、发送和取消按实际证书的 SHA-256 指纹识别对端；有指纹的连接在发送 HTTP 数据前校验。手动输入地址时，第一次注册是发现操作，不是预先配对；会核对证书的自签名、有效期和注册响应指纹。局域网组播公告不加密。原有文件确认、媒体内容校验、后台会话和 token 仍适用。菜单关闭「使用 HTTPS 加密」才切换到 HTTP；不会因 TLS 失败降级。
- **回环视频**：只绑定 `127.0.0.1`。内置播放器对活跃播放授权使用单独的 HTTPS 端口，`MediaDataSource` 通过固定本机视频证书的连接按需读取。其他 `https://` 视频仍由平台按普通 HTTPS 处理。外部播放器优先接收 `content://`，必要时兼容旧回环 HTTP；无需安装 CA。
- **传输**：native 解析解密后的 HTTP 请求头；视频或文件内容通过系统 TLS 流分块读写，不整文件载入内存。快传与视频不共享证书、监听器或发现状态。

## 构建与体积

版本 `2026.11.25`，与[仅明文的 native 视频版本](native-video.md)比较：

| 制品 | 加入系统 TLS 后 | 增量 |
| --- | ---: | ---: |
| Debug APK | 2,328,256 B | +17,602 B |
| 未签名 Release APK | 2,155,171 B | +13,668 B（13.35 KiB） |
| Debug AAB | 1,493,857 B | +17,192 B |

JDK 25 和独立目录中的 JDK 21 均离线通过 `:app:verifyLocalSendDebug :app:assembleRelease`。保持 `minSdk 23`、`targetSdk 32`、原 918 个资源 ID 和两个独立 JNI 库；[三 ABI 及 Java 桥摘要](../app/native-inputs.json)已固定。APK/AAB 无 HttpCore 或 `androidx.core` 类型，未打包 TLS 库、密钥文件或许可文本。Debug/Release 核对原本地图库声明：621 个类型、2,251 个字段、4,123 个方法。

三个 `libqpiclocalsend.so` 均有 21 个 JNI 导出，无 TLS 动态库依赖。arm64 只依赖系统 `libc.so`，两个 `LOAD` 段均按 64 KiB 对齐。加上系统 TLS 后，未签名 Release APK 仍比原 HttpCore 版小 77,984 B。

## Android 6.0 ARM 模拟器实测

以下网络测试使用同一份 Debug APK，SHA-256：`3e4223d5b8a24b4f29a93f782480db3e822a0a3ed8c11e33b638c7e1d688e198`。Release APK（未签名）：`816f702b96eeee834e8b03de7dd23b5f9a94ab46ff0417fa74b2bf6c6e74c6da`。

- [官方核心双向 HTTPS](system-tls-localsend.json)：TLS 1.2 下并行接收 PNG、WAV，跳过文本，识别伪装媒体；返回后仍能向官方接收端发送完整的 262,912 字节文件。错误公告指纹在发送注册请求前被拒绝；缺少客户端证书、谎报自身指纹、拿另一张证书使用已有会话也被拒绝。已批准的上传在 Home 后完成，从历史和通知都能单独取消。进程重启后仍能用原来的指纹连接。
- 手动连接：新官方 HTTPS 对端没有发送组播公告，通过「通过 IP 连接」输入地址和端口后，出现该对端在注册响应中返回的名称。通过快传菜单切换到 HTTP 后，官方双向收发、可见性、内容校验和后台取消回归也通过；随后通过同一菜单恢复 HTTPS。
- [五设备 HTTPS 队列](system-tls-multisend.json)：同时 3 个报价；取消一台排队设备、一台运行中设备，一台拒收，另外两台在 Activity 关闭后均收到两份与原件 SHA-256 一致的文件。
- 回环 HTTPS：内置播放器通过固定证书的连接解码 718,919 字节的 H.264 MP4，屏幕像素验证了蓝色视频帧。GET/HEAD、闭合/开放/后缀 Range、416、随机授权、文件切片、同目录字幕与管道均通过。独立 Android 应用不安装证书即可读 HTTP 回退的指定字节；内置播放器拒绝无关的自签名 HTTPS 服务。该系统协商的是 TLS 1.2；未作 TLS 1.3 或 arm64 实机互通结论。
- [系统分享](../tools/gallery_share_smoke.py)验证了从图库顶部「其它…」打开 Android 分享面板，面板及直达应用收到的内容和读授权正确，裁剪后分享的是渲染结果。[图库刷新](../tools/received_gallery_smoke.py)确认同一 Activity 内接收目录计数从 1 到 2 再到 3，相册和「活动」页显示新缩略图，未传完的文件不可见。

## 复验

在独立 API 23 ARM 模拟器上，使用 `tools/localsend_smoke.py --tls` 和 `OpenQPicLocalSend/tests/official_peer.py` 构建的官方对端测试双向 HTTPS。`--openssl` 仅用于主机生成测试客户端的临时证书。明文兼容回归需在快传菜单关闭 HTTPS。`tools/multisend_smoke.py --tls` 通过 5 个独立身份验证多设备队列。

内置视频使用 `tools/video_proxy_smoke.py --tls --serial <模拟器> --sdk <SDK> --mp4 <视频>`，可为纯蓝 H.264 测试片加 `--blue-frame`，从屏幕像素验证真实解码。省略 `--tls`、保留 `--sdk` 可用独立应用验证第三方 HTTP 回退；`--openssl <路径>` 另外测试普通 HTTPS 播放拒绝未受信任的证书。
