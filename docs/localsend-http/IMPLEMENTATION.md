# LocalSend 快传实现

默认 APK 使用 **LocalSend v2.2 / HTTPS / IPv4** 接收，可在快传菜单显式切换到 HTTP 兼容模式。出站请求按对端的接收协议连接。来源：

| 输入 | 固定版本 / 位置 |
|---|---|
| 图像 JNI | `OpenQPicJni` `761302f416d2166b26d7699ace352d6c2b18bff8`，34 个 `JniUtils` JNI 导出 |
| 独立快传库及控制层 | `OpenQPicLocalSend`：`src/transport.c`、`java/org/openqpic/localsend/`；当前制品及库输入见[系统 TLS 实测](../../verification/system-tls.md) |
| Android 集成 | [`TransferActivity`](../../app/src/main/java/com/alensw/transfer/TransferActivity.java)、[`TransferSessionService`](../../app/src/main/java/com/alensw/transfer/TransferSessionService.java)、[`ReceivePage`](../../app/src/main/java/com/alensw/transfer/ReceivePage.java)、[`TransferMedia`](../../app/src/main/java/com/alensw/transfer/TransferMedia.java) |
| 固定输入 | [各 ABI、桥接源码及仓库许可文本的 SHA-256](../../app/native-inputs.json)；[picohttpparser 许可](../../licenses/picohttpparser.txt)不打包 |
| 互通对端 | 官方 `localsend/localsend` `d8702511dfb074d795452a9204ba3c9473f4f01f` 的 Rust HTTP 客户端和服务端 |

## 界面与可发现性

保留 `com.alensw.transfer.TransferActivity`、图库多选、系统 `SEND/SEND_MULTIPLE`、`ClipData` 和桌面入口。发送页按指纹去重、按名称排序，行内显示协议、设备类型或型号、地址和端口。45 秒未见的设备按其协议回探，90 秒未确认的记录移除。手动连接可用 `http://` 或 `https://` 指定对端协议；只填地址时使用本机接收模式。

单击设备直接发送；点「多选」可勾选多台，再用文件栏的「发送（台数）」一次提交。选中状态按设备指纹保存，列表重排不会勾错对象。选图返回后会等待已选设备重新出现，用其当前地址发送。传输列表按设备显示结果，包括排队中的任务。

`TransferSessionService` 拥有会话和 URI 授权。仅在快传界面恢复时选择局域地址、持有 `MulticastLock`、启动 TCP 后加入 `224.0.0.167:53317`。页面暂停即退出组播、关闭注册连接，不再应答 `v1/info`、`v2/info`、`register` 或新 `prepare-upload`。已有任务保留原地址和端口，只受理匹配的上传和取消；没有任务则关闭 TCP 监听。设置刷新、切页、Activity 销毁与切换网卡均不调用任务取消。原网络真的失去可达性时可能发生 I/O 失败，协议不提供跨 IP 断点续传。

收到合格报价时显示独立的接收页（[`ls_receive.xml`](../../app/src/main/res/layout/ls_receive.xml)），可核对来源、保存目录、选中数量/大小、跳过数量。返回列表或 Home 不等于拒收，待决定项与勾选状态由服务持有。后台不会弹出新页面；通知可返回任务页。

旧 `com.alensw.transfer.a.*`、PeerHub 和反向拉取实现已从应用源中删除，包括原先实际经 javac 入包的四个类。新的 `/upload` 只接收推流。播放视频优先使用原 URI 和系统读授权；没有应用能直接打开 `content://` 视频时，使用独立的、未导出的回环 Range 服务。播放器保留 `StreamingActivity` 别名。

## 文件与协议

- `GET info`、`POST register/prepare-upload/upload/cancel` 均使用 `/api/localsend/v2/`；另提供 `/api/localsend/v1/info` 供早期设备探测。HTTPS 采用系统 TLS 1.2/1.3 和双向证书，按握手证书的 SHA-256 指纹核验身份；手动输入新地址时，首次注册核对自签名、有效期和响应指纹。HTTP 和 HTTPS 共用同一个快传身份；在 HTTP 连接中，指纹仅用于识别设备。
- 公告和报价里的 `protocol` 描述本机监听，注册、发送和主动取消分别连接目标公布的协议。两端监听协议可以不同，同一批可以混选 HTTP 和 HTTPS 设备。证书或握手失败不重试 HTTP；当前发现期间，已验证的 HTTPS 地址和身份不会被未经验证的 HTTP 公告替换。若对端确实切换到 HTTP，可离开快传后重新进入以重新发现。
- 一次最多 256 个文件。用户接受后只为勾选的媒体发放独立随机 token，并核对来源 IP、会话和文件 ID。接收兼容定长与 chunked、并发上传、声明的 SHA-256 和同 token 的 422 整文件重试。支持待确认取消、拒收、忙碌和可选 PIN。
- 接收只接受非空图片、视频、音频。报价按名称与 MIME 筛选；通用 MIME 可根据媒体扩展名判定。若扩展名和声明的 MIME 都是支持的图片格式，两者可以不同。全为不合格文件返回 415，混合报价仅展示合格项。
- 图片发布前以实际解码结果为准：格式须受支持、尺寸有效，且能解码出缩略图。例如 `photo.jpg` 的内容为 PNG 时，声明 JPEG 或 PNG 均可按原名保存，不转码。Android 10 及以上的系统媒体库可能仍按保留的扩展名标注类型。音视频使用 `MediaExtractor` 检查相应轨道和样本。伪装文件返回 415，暂存内容回滚，不中断同一会话的其他文件。实际可读格式受 Android 版本和系统解码支持影响。出站分享不受此接收策略限制。
- HTTP 使用 native FD 读写；HTTPS 在系统 TLS 流中分块传输，按偏移读取源 FD。收到文件后，仍在校验完成时才发布。发送端处理 `content://` 的 asset offset、长度和不可 seek 的 provider；必要时暂存后再报价。接收端只保存安全的末级文件名，不覆盖同名文件。
- 图库的「相册」和本地目录使用自己的文件缓存。[`ReceivedMediaUpdates`](../../app/src/main/java/com/alensw/ui/page/ReceivedMediaUpdates.java) 在图片或视频成功发布后合并同目录的更新，让可见页面按原有筛选和排序重读；若页面已暂停，返回时补刷。系统媒体集合在旧系统扫描完成、或 Android 10+ 清除 `IS_PENDING` 后收到通知。「活动」（Moments）页沿用其 MediaStore 观察器。未传完、取消或校验失败的暂存文件不会触发发布。
- 活跃任务由 `dataSync` 前台服务托管。通知提供进度与任务入口，单任务可直接取消，多任务在列表逐项取消。任务结束释放前台通知及传输锁。没有用 UI 或空闲计时器取消已批准的传输；手动取消会关闭对应流、回滚未提交文件，已提交文件保留。
- 多设备发送为每个接收方创建独立会话，分别等待确认/PIN、上传对方接受的文件、处理重试或取消。每个端点最多并行 3 台，其余按提交顺序排队；关闭发送页后队列继续。每批文件只打开或暂存一次，HTTPS 的 `Os.pread` 和 HTTP 的 native `pread64` 均按文件偏移读取，不移动共享 FD 的游标。最后一个使用者退出后才关闭源文件。

## 最初明文实现的制品

| ABI | `libqpicjni156.so` | `libqpiclocalsend.so` |
|---|---:|---:|
| `armeabi` | 198,252 B | 26,080 B |
| `x86` | 374,400 B | 21,920 B |
| `arm64-v8a` | 290,432 B | 20,704 B |

APK/AAB 的字节数、签名、DEX 类数、依赖提交及摘要见[完整制品记录](../../verification/localsend-product.json)。保留原 918 个资源 ID；arm64 两个 ELF 均检查了 16 KiB LOAD 对齐。

## 复验

```powershell
# 在 OpenQPic 中，正常打包不需要相邻仓库；应用结构和两套 JNI 按新产品规则验收。
.\gradlew.bat :app:verifyLocalSendDebug :app:assembleRelease

# 官方测试对端：使用原上游 core 及其测试 helper，不启动用户的桌面 LocalSend 或改其配置。
python ..\OpenQPicLocalSend\tests\official_peer.py E:\Git\localsend
adb -s <专用模拟器> install -r -g app\build\outputs\apk\debug\app-debug.apk
python tools\received_gallery_smoke.py --serial <专用模拟器> --mp4 <小型测试视频> --report app\build\reports\received-gallery-smoke.json
python tools\localsend_smoke.py --serial <专用模拟器> --peer ..\OpenQPicLocalSend\build\official\target\debug\examples\openqpic_peer.exe --mp4 <小型测试视频> --report app\build\reports\localsend-smoke.json
python tools\localsend_smoke.py --tls --serial <专用模拟器> --peer <官方测试对端> --openssl <OpenSSL测试工具> --report app\build\reports\localsend-tls.json
# 在上面的收发命令中加 --mixed，可让对端监听相反的协议。
python tools\multisend_smoke.py --serial <专用模拟器> --peer ..\OpenQPicLocalSend\build\official\target\debug\examples\openqpic_peer.exe --report app\build\reports\multisend-smoke.json
python tools\multisend_smoke.py --tls --serial <专用模拟器> --peer <官方测试对端> --report app\build\reports\multisend-tls.json
python tools\multisend_smoke.py --mixed --serial <专用模拟器> --peer <官方测试对端> --report app\build\reports\multisend-mixed.json
python tools\video_proxy_smoke.py --serial <专用模拟器> --mp4 <小型测试视频>
python tools\video_proxy_smoke.py --tls --serial <专用模拟器> --mp4 <小型测试视频> --sdk <Android-SDK-路径>
python tools\github_update_smoke.py --serial <专用模拟器> --sdk <Android-SDK-路径>
```

运行环境为独立的、root 的英文 API 23 ARM 模拟器，`eth0` 有局域地址；用测试端口的 adb 转发跨越模拟器 NAT。`localsend_smoke.py` 用 `--tls` 时本机开启「接收时使用 HTTPS」，省略时关闭。脚本通过实际界面确认接收；测试用 OpenSSL 仅用于主机端的临时测试证书。

本机分别以 HTTPS 和 HTTP 监听，与相反模式的官方核心测试对端双向收发均通过。混合队列同时选中 5 台对端，两种协议各有一台在发送页关闭后收齐文件，SHA-256 均与原件一致；排队任务和运行中的任务都可单独取消。另验证了两种混合模式的手动 IP 注册与取消回调，以及 HTTPS 监听下拒绝使用错误客户端证书取消后台任务。

以下是[最初明文版的模拟器端到端报告](../../verification/localsend-smoke.json)：

- 打开图库无监听；进入快传捕获真实组播及回注册。测试验证列表去重、排序；离开后无新发现或报价，无任务时也无 TCP 监听。
- 官方 → OpenQPic：同一报价含 PNG、WAV、TXT，仅两项真实媒体获得 token，两个文件并发上传，核对实际保存的 SHA-256。另外验证真实 MP4、伪造媒体、空文件、忙碌、待确认取消、无效 token、422 重试与重放拒绝。
- 收到一部分数据后按 Home，当前文件与之后开启的新文件连接均可在后台传完；重新打开列表能手动取消任务。OpenQPic → 官方：在对端尚未答复发送报价时按返回，关闭分享 Activity 后仍能完成 262,912 字节传输，接收端逐字节相等。
- [多设备发送回归](../../verification/multisend-smoke.json)：勾选 5 个独立的官方 HTTP 接收端，同时有 3 台等待确认。分别手动取消一台排队设备和一台正在等待确认的设备；另一台拒收后，后台自动启动下一台。两个成功的接收端各收到相同的两份文件，SHA-256 与各自原件一致。
- [图库刷新回归](../../verification/received-gallery-smoke.json)在文件只传了前 256 字节时返回原图库，再完成上传。从实际绘制的网格确认新相册和两种不同颜色的缩略图出现，目录计数由 1 变 2；停留在快传页收完 MP4 再返回，计数变为 3。另在「活动」页停留至另一张图片收完，确认新的封面自动出现。
- 独立的视频回归使用真实 MediaStore 视频 FD：回环 GET 200、指定范围与后缀范围 206、越界 416，图库播放器可打开，期间不存在快传监听。

模拟器执行覆盖 ARM 32 位。x86、arm64 的结果是打包和 ELF 验证；本次未做实体 Wi-Fi 双机、Android 10+ MediaStore 实机或超过 4 GiB 文件的长传测试。
