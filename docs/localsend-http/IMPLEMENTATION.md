# LocalSend 快传实现

默认 APK 已从 QPIC 私有协议切换到 **LocalSend v2.2 / HTTP / IPv4**。来源：

| 输入 | 固定版本 / 位置 |
|---|---|
| 图像 JNI | `OpenQPicJni` `761302f416d2166b26d7699ace352d6c2b18bff8`，34 个 `JniUtils` JNI 导出 |
| 独立快传库及控制层 | `OpenQPicLocalSend`：`src/transport.c`、`java/org/openqpic/localsend/`；构建所用的基线与源码摘要见[制品记录](../../verification/localsend-product.json) |
| Android 集成 | [`TransferActivity`](../../app/src/main/java/com/alensw/transfer/TransferActivity.java)、[`TransferSessionService`](../../app/src/main/java/com/alensw/transfer/TransferSessionService.java)、[`ReceivePage`](../../app/src/main/java/com/alensw/transfer/ReceivePage.java)、[`TransferMedia`](../../app/src/main/java/com/alensw/transfer/TransferMedia.java) |
| 随包输入 | [各 ABI、桥接源码及许可的 SHA-256](../../app/native-inputs.json) |
| 互通对端 | 官方 `localsend/localsend` `d8702511dfb074d795452a9204ba3c9473f4f01f` 的 Rust HTTP 客户端和服务端 |

## 界面与可发现性

保留 `com.alensw.transfer.TransferActivity`、图库多选、系统 `SEND/SEND_MULTIPLE`、`ClipData` 和桌面入口。发送页按指纹去重，HTTP 设备在前，再按名称稳定排序；行内显示设备类型或型号、地址和端口。仅心跳更新时间时不刷新列表。45 秒未见的 HTTP 设备回探，90 秒未确认的记录移除。不能发送的 HTTPS 设备显示原因。手动连接 IPv4 和端口的按钮始终可见。

单击设备直接发送；点「多选」可勾选多台，再用文件栏的「发送（台数）」一次提交。选中状态按设备指纹保存，列表重排不会勾错对象。选图返回后会等待已选设备重新出现，用其当前地址发送。传输列表按设备显示结果，包括排队中的任务。

`TransferSessionService` 拥有会话和 URI 授权。仅在快传界面恢复时选择局域地址、持有 `MulticastLock`、启动 TCP 后加入 `224.0.0.167:53317`。页面暂停即退出组播、关闭注册连接，不再应答 `v1/info`、`v2/info`、`register` 或新 `prepare-upload`。已有任务保留原地址和端口，只受理匹配的上传和取消；没有任务则关闭 TCP 监听。设置刷新、切页、Activity 销毁与切换网卡均不调用任务取消。原网络真的失去可达性时可能发生 I/O 失败，协议不提供跨 IP 断点续传。

收到合格报价时显示独立的接收页（[`ls_receive.xml`](../../app/src/main/res/layout/ls_receive.xml)），可核对来源、保存目录、选中数量/大小、跳过数量。返回列表或 Home 不等于拒收，待决定项与勾选状态由服务持有。后台不会弹出新页面；通知可返回任务页。

旧 `com.alensw.transfer.a.*`、PeerHub 和反向拉取实现已从应用源中删除，包括原先实际经 javac 入包的四个类。新的 `/upload` 只接收推流。视频转交使用单独的、未导出的回环服务，保留播放器 `StreamingActivity` 别名。

## 文件与协议

- `GET info`、`POST register/prepare-upload/upload/cancel` 均使用 `/api/localsend/v2/`；另提供 `/api/localsend/v1/info` 供早期设备探测。默认 HTTPS 对端需先关闭加密才能双向互传。
- 一次最多 256 个文件。用户接受后只为勾选的媒体发放独立随机 token，并核对来源 IP、会话和文件 ID。接收兼容定长与 chunked、并发上传、声明的 SHA-256 和同 token 的 422 整文件重试。支持待确认取消、拒收、忙碌和可选 PIN。
- 接收只接受非空图片、视频、音频。报价按名称与 MIME 筛选；通用 MIME 可根据媒体扩展名判定。全为不合格文件返回 415，混合报价仅展示合格项。落盘发布前，图片检查系统解码格式、尺寸并试解码缩略图；音视频使用 `MediaExtractor` 检查相应轨道和样本。伪装文件返回 415，暂存内容回滚，不中断同一会话的其他文件。实际可读格式受 Android 版本和系统解码支持影响。出站分享不受此接收策略限制。
- native 直接流式读写借用的 FD，Java 保持所有权。发送端处理 `content://` 的 asset offset、长度和不可 seek 的 provider；必要时暂存后再报价。接收端只保存安全的末级文件名，不覆盖同名文件。Android 10+ 使用 MediaStore 待发布行；较早版本在目的目录暂存、校验后提交并通知媒体扫描。
- 图库的「相册」和本地目录使用自己的文件缓存。[`ReceivedMediaUpdates`](../../app/src/main/java/com/alensw/ui/c/ReceivedMediaUpdates.java) 在图片或视频成功发布后合并同目录的更新，让可见页面按原有筛选和排序重读；若页面已暂停，返回时补刷。系统媒体集合在旧系统扫描完成、或 Android 10+ 清除 `IS_PENDING` 后收到通知。「活动」（Moments）页沿用其 MediaStore 观察器。未传完、取消或校验失败的暂存文件不会触发发布。
- 活跃任务由 `dataSync` 前台服务托管。通知提供进度与任务入口，单任务可直接取消，多任务在列表逐项取消。任务结束释放前台通知及传输锁。没有用 UI 或空闲计时器取消已批准的传输；手动取消会关闭对应流、回滚未提交文件，已提交文件保留。
- 多设备发送在同一个 HTTP 端点上为每个接收方创建独立会话，分别等待确认/PIN、上传对方接受的文件、处理重试或取消。每个端点最多并行 3 台，其余按提交顺序排队；即使关闭发送页，队列也继续。每批文件只打开或暂存一次，native `pread64` 让不同设备和重试各自从正确的 asset 偏移读取。最后一个使用者退出后才关闭共享源。

## 已构建制品

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
python tools\multisend_smoke.py --serial <专用模拟器> --peer ..\OpenQPicLocalSend\build\official\target\debug\examples\openqpic_peer.exe --report app\build\reports\multisend-smoke.json
python tools\video_proxy_smoke.py --serial <专用模拟器> --mp4 <小型测试视频>
```

运行环境为独立的、root 的英文 API 23 ARM 模拟器，`eth0` 有局域地址；用测试端口的 adb 转发跨越模拟器 NAT。原本禁用联网的旧系统镜像需要让 `224.0.0.167/32` 可从 `eth0` 到达。脚本通过真实按钮确认，APK 无测试后门。

[模拟器端到端报告](../../verification/localsend-smoke.json)对应上面的 debug APK：

- 打开图库无监听；进入快传捕获真实组播及 HTTP 回注册。测试验证列表去重、排序；离开后无新发现或报价，无任务时也无 TCP 监听。
- 官方 → OpenQPic：同一报价含 PNG、WAV、TXT，仅两项真实媒体获得 token，两个文件并发上传，核对实际保存的 SHA-256。另外验证真实 MP4、伪造媒体、空文件、忙碌、待确认取消、无效 token、422 重试与重放拒绝。
- 收到一部分数据后按 Home，当前文件与之后开启的新文件连接均可在后台传完；重新打开列表能手动取消任务。OpenQPic → 官方：在对端尚未答复发送报价时按返回，关闭分享 Activity 后仍能完成 262,912 字节传输，接收端逐字节相等。
- [多设备发送回归](../../verification/multisend-smoke.json)：勾选 5 个独立的官方 HTTP 接收端，同时有 3 台等待确认。分别手动取消一台排队设备和一台正在等待确认的设备；另一台拒收后，后台自动启动下一台。两个成功的接收端各收到相同的两份文件，SHA-256 与各自原件一致。
- [图库刷新回归](../../verification/received-gallery-smoke.json)在文件只传了前 256 字节时返回原图库，再完成上传。从实际绘制的网格确认新相册和两种不同颜色的缩略图出现，目录计数由 1 变 2；停留在快传页收完 MP4 再返回，计数变为 3。另在「活动」页停留至另一张图片收完，确认新的封面自动出现。
- 独立的视频回归使用真实 MediaStore 视频 FD：回环 GET 200、指定范围与后缀范围 206、越界 416，图库播放器可打开，期间不存在快传监听。

模拟器执行覆盖 ARM 32 位。x86、arm64 的结果是打包和 ELF 验证；本次未做实体 Wi-Fi 双机、Android 10+ MediaStore 实机或超过 4 GiB 文件的长传测试。
