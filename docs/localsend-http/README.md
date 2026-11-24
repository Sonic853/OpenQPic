# OpenQPic「WiFi 快传」：LocalSend 明文 HTTP 互通方案

> 这是替换前的协议与体积分析。已交付实现采用独立仓库 `OpenQPicLocalSend` 和 `libqpiclocalsend.so`；当前行为、构建和互通验证见[实现记录](IMPLEMENTATION.md)。下文保留当时的建议、基线和未实现状态。

**建议实现 LocalSend v2.2 的双向 App 文件推送。** 保留图库菜单、桌面快捷方式和系统分享入口，替换旧 QPIC 发现、PeerHub 和反向拉取协议。可在 OpenQPicJni 中新增独立的小型 C/C++ 模块，由 OpenQPic 提供 Android 界面、文件授权和存储。普通设备互传不需要 Flutter、Rust、WebRTC、网页前端或信令服务器。

分析基线：本机 `E:\Git\localsend` **`d8702511`**（App `1.19.0+65`），OpenQPic **`e4c35cb`**，OpenQPicJni **`761302f`**。LocalSend 的 App 版本、协议版本字符串 **`"2.2"`** 和 URL 主版本 **`/v2/`** 是三件事。本报告是源码分析与构建定容；计量用 APK 不是已实现快传的产品。

**体积先看结论：** 对当前 `armeabi+x86` 完整包，保留原图像库、快传独立为 `.so` 的首版预算约 **1.9–2.0 MiB 签名 release APK**；如果同时把图像库也换成 OpenQPicJni 的重编版并合入快传，约 **2.30–2.45 MiB**。两者相差主要来自图像库。见[体积实测与预算](SIZE.md)及[机器可读原始结果](size-study.json)。

## 1. 哪些组合能通信

| 发送 → 接收 | 纯 HTTP OpenQPic 的能力 |
|---|---|
| OpenQPic → 关闭加密的 LocalSend；关闭加密的 LocalSend → OpenQPic | 首版双向验收目标；两端都应提供可达的 HTTP 服务和 v2 接口。 |
| OpenQPic → 默认 HTTPS 的 LocalSend | **不能上传**。对方监听 TLS，不会把明文连接自动当作 HTTP。UI 应标明原因，不要悄悄降级或把 HTTPS 设备列为可发送。 |
| 默认 HTTPS 的 LocalSend → OpenQPic | **可能单向发送**：官方发送端按**目标**通道的协议上传；若它已通过 OpenQPic 的 HTTP `register` 确认目标，即可向 OpenQPic 的 HTTP 服务推送。反方向和需要回拨该 HTTPS 服务的操作仍不成立。这是源码推论，不是已测的双向承诺。 |
| 浏览器打开分享链接，或没有组播的网络 | 浏览器取文件是另一套可选下载接口。无组播时两端仍可手动指定 `IP:端口`，通过 HTTP `register` 确认；不必实现整网段扫描。 |

官方 App 默认 `https=true`，同一个监听端口按配置运行 HTTP **或** HTTPS。它按来源 IP、声明端口及**对端** `protocol` 发起注册和文件请求。设备之前若以 HTTPS 被验证，当前发现缓存会剔除该 fingerprint 的 HTTP 通道，不能靠把字符串改成 `"http"` 来降级。HTTP 模式 fingerprint 只是设备标识：OpenQPic 可用首次生成、持久化的随机 ID；不要用 IP/BSSID 或在每次广播时重新生成，也不要凭这个字段自动收文件。官方 App 即便关闭加密仍可能填自己的证书摘要，在线路上同样只是自报字符串。

源码：[设置默认值](https://github.com/localsend/localsend/blob/d8702511/app/lib/provider/persistence_provider.dart#L495-L501)、[监听协议选择](https://github.com/localsend/localsend/blob/d8702511/packages/core/src/http/server/mod.rs#L487-L548)、[按目标协议发送](https://github.com/localsend/localsend/blob/d8702511/app/lib/provider/network/send_provider.dart#L232-L270)、[发现缓存](https://github.com/localsend/localsend/blob/d8702511/packages/core/src/discovery/store.rs#L243-L252)。

## 2. 从发现到传完一个文件

### 发现：UDP 只发公告，HTTP 才回注册

1. 先启动 TCP HTTP 监听，再加本机 Wi-Fi 接口的 IPv4 组 **`224.0.0.167:53317/UDP`**。HTTP 默认也是 `53317`，但 UDP 来源端口不等于对方 HTTP 端口，要使用公告里的 `port`。同机已有 LocalSend 时要处理 TCP 端口占用；不能公布一个未监听的端口。
2. 发送 UTF-8 JSON 数据报。官方兼容旧客户端时附带 `announce:true`，按 100、500、2000 ms **依次等待**再发三次。重复公告按 fingerprint 和地址/端口去重；忽略自己。换 Wi-Fi 后离组、重新选接口、监听、公告。IPv6 扩展组是 **`ff12::fd3a:e420`**，链路本地 IPv6 地址需保留 scope/interface index；可在 IPv4 首版后补。
3. 收到公告的设备向 **UDP 包的来源 IP + 公告的 HTTP 端口** 单播 `POST /api/localsend/v2/register`。请求体是**回注册方自己**的设备信息；响应是被注册方的信息。处理注册事件和按需回探须去重，避免双方不停互相注册。`GET /info` 适合诊断，不能替代能被对方确认的 `register`。

示例公告（IP 不放在 JSON 里）：

```json
{"alias":"OpenQPic","version":"2.2","deviceModel":"Android","deviceType":"mobile","fingerprint":"persistent-device-id","port":53317,"protocol":"http","download":false,"announce":true}
```

注册请求含 `alias, version, fingerprint, port, protocol`；`deviceModel, deviceType` 可省，`download` 缺省为 false。`protocol` 是小写字符串而非布尔值。注册响应含 `alias, version, fingerprint`，可带 `deviceModel, deviceType, download`；不要求响应再带 `port` 或 `protocol`。`download:false` 只表示不提供可选下载 API，**并不禁止接收推送**。

源码：[公告格式](https://github.com/localsend/localsend/blob/d8702511/packages/core/src/model/discovery.rs#L101-L141)、[组地址、时序及 announce 标志](https://github.com/localsend/localsend/blob/d8702511/packages/core/src/multicast/mod.rs#L24-L49)、[公告回拨](https://github.com/localsend/localsend/blob/d8702511/packages/core/src/discovery/mod.rs#L615-L645)、[注册 DTO](https://github.com/localsend/localsend/blob/d8702511/packages/core/src/http/dto_v2.rs#L12-L84)。

### 文件：发送方推送原始字节，不是 multipart

以下路径共用前缀 **`/api/localsend/v2`**。JSON 用 UTF-8、`application/json`；文件正文是原始字节流，不能要求固定 MIME 头、Base64 或表单封装。

| 方法 / 路径 | 接收方实际要做的事 |
|---|---|
| `GET /info` | 200，返回本机信息。为旧版探测可额外支持 `GET /api/localsend/v1/info`，返回同一信息。这不是 v1 文件传输支持。 |
| `POST /register` | 接受对方设备信息，记录其来源地址，200 返回本机身份。监听端可同时作为出站 HTTP 客户端。 |
| `POST /prepare-upload[?pin=…]` | 读 `{info,files}`。`files` 是 **ID → 文件描述的对象，不是数组**。保留一个待确认的接收会话，等待用户选择文件后再回答这次请求。200 返回 `{sessionId,files:{被选中的ID:独立token}}`。 |
| `POST /upload?sessionId=…&fileId=…&token=…` | 一文件一请求，只接受本会话、本来源和该文件的 token。流式写入、核对解码后的总字节数，若给出了 SHA-256 且启用校验则比对；写入和校验成功才回 **200**。 |
| `POST /cancel[?sessionId=…]` | 待确认时兼容同一来源尚无远端 sessionId 的取消；激活后按会话和对端匹配。处理发送方取消接收、接收方反向取消正在发送的会话、连接提前关闭，收回临时文件与会话；正常响应 200。 |

例如，发送者希望传一张 3 字节的示例文件，`prepare-upload` 内容是：

```json
{"info":{"alias":"Gallery","version":"2.2","fingerprint":"device-A","port":53317,"protocol":"http","deviceType":"mobile","download":false},"files":{"f1":{"id":"f1","fileName":"例子.jpg","size":3,"fileType":"image/jpeg"}}}
```

接受后回应 `{"sessionId":"S","files":{"f1":"T1"}}`，再向 `/upload?sessionId=S&fileId=f1&token=T1` POST 那 3 个字节。这里的 `S/T1` 仅为说明，实际接收端应生成不可预测的独立值。传完所有选中文件后本地判断完成；没有额外的 `/finish`，也不应从发送者提供的 URL 回拉。

**数据细节会决定真机能否互传：**

- `FileDto` 要求 `id,fileName,size,fileType`，`size` 是字节数、Rust `u64`，接收端应拒绝负数或无法表示的值；不能用 32 位计数累加。`fileType` 优先写 MIME，未知写 `application/octet-stream`。`sha256`、`preview`、`metadata.{modified,accessed}` 可省；时间戳是 RFC 3339 字符串。线上哈希字段是 **`sha256`**，不是 Dart 内部成员名 `hash`。
- **要同时接收 `Content-Length` 和 HTTP/1.1 `Transfer-Encoding: chunked`。** 官方桥的 `contentLength` 用于显示进度；上传核心将文件流包装成 `reqwest::Body::wrap_stream`，没有在该请求上显式设置 Content-Length。不能用“没有该头就拒绝”，也不能把 chunk 壳写进文件。拖放多文件时官方 App 默认同时上传 **2 个文件**；“只能有一个接收会话”不是“只能开一条 TCP 连接”。
- `prepare-upload` 的 `204` 表示无需传正文（如只阅读内联文本或未选文件），`403` 是拒收，两者要区分。当前 App 对单一短 UTF-8 文本可附 `preview`，内联阈值 **16 KiB**；普通大文本照文件走。文件列表为空是 `400`；对端正忙为 `409`；需要/输错 PIN 为 `401`，多次失败为 `429`。PIN 是查询参数，要 URL 编码。无效会话/文件 token 返回 `403`，校验不符 `422`，磁盘等失败 `500`；错误体用 `{"message":"…"}`。
- SHA-256 与是否加密是两件事。官方默认发送时不创建校验和、接收时验证已提供的校验和；不区分十六进制大小写。对 `422` 官方发送方最多尝试 **3 次（含首次）**，同会话、同文件、同 token；接收端应重新从零写，不能追加到失败文件。普通成功文件不再接受重放。未实现断点续传不妨碍这种整文件重试。
- 相册批量/文件夹发送可把相对目录放在 `fileName`，不要假设它永远是 basename。首版可明确选择仅保存末级文件名并防冲突；若承诺保留目录结构，则逐级在用户选定目录内创建，拒绝绝对路径与父目录段，测试 Unicode、重名及深目录。Android 侧对中文/emoji 用标准 UTF-8；JNI 的 modified UTF-8 不可直接当网络 UTF-8。
- HTTP 下文件、文件名、token 和可选 PIN 都是明文；PIN/指纹不提供机密性或可验证的设备身份。首版使用用户确认、短生命周期会话、限额和本地选定的目录。不把文件名当 Android 本地绝对路径；不跟随出站重定向或系统代理把局域网数据转走。

源码：[实际路由（含 v1/info 的例外）](https://github.com/localsend/localsend/blob/d8702511/packages/core/src/http/server/mod.rs#L642-L706)、[文件 DTO](https://github.com/localsend/localsend/blob/d8702511/packages/core/src/model/transfer.rs#L134-L155)、[Dart 到 wire 的字段转换](https://github.com/localsend/localsend/blob/d8702511/packages/localsend_isolates/lib/util/rust.dart#L57-L73)、[流式请求体](https://github.com/localsend/localsend/blob/d8702511/packages/core/src/http/client/mod.rs#L189-L206)、[确认、上传和取消](https://github.com/localsend/localsend/blob/d8702511/packages/core/src/http/server/v2.rs#L238-L560)、[文件长度与写入](https://github.com/localsend/localsend/blob/d8702511/packages/core/src/http/server/common/save.rs#L284-L331)、[并发和重试](https://github.com/localsend/localsend/blob/d8702511/packages/localsend_isolates/lib/src/isolate/child/upload_isolate.dart#L16-L23)、[文本预览](https://github.com/localsend/localsend/blob/d8702511/app/lib/util/message_preview.dart#L6-L25)。

### 首版可省略的协议

`POST /prepare-download`、`GET /download?sessionId=…&fileId=…` 是对方取文件/浏览器分享的另一条流程。如果将来支持返回下载清单和网页链接，再公布 `download:true`；浏览器上传页面又是单独的 Web UI。图库 App 之间发送/接收照片和视频都只需前面的 push 流。不需要 `/v2/show`、v3 握手、WebRTC、信令服务、整网段扫描或 v1 `send-request`。LocalSend `packages/core/Cargo.toml:55–63` 的 Cargo `http` feature 本身就引入 `crypto`、Rustls、reqwest 和 Tokio 等；**在官方 App 里关掉加密不表示这些库会从二进制消失**。轻量实现应复用线路协议，不直接装入整套官方核心。

## 3. 原版快传怎样退出，哪些入口要接住

| 现有代码 | 替换方式 |
|---|---|
| `com.alensw.transfer.a.*`：UDP `15353`，IPv4 组 `224.0.0.251` / IPv6 `ff02::fb`，二进制 `QPIC` 报文；还有 `PeerHub` 的 join/leave | 整条下线。新发现只公布 LocalSend JSON，不读取 BSSID 作为设备身份，不查询 PeerHub。旧版 QuickPic 私有快传将不再互通。 |
| `transfer.bd / bh / d / n`：动态 HTTP 端口；先 POST `/request`，接收方 GET `/file/…`，再 POST `/finish` | 删除旧收发及自动回拉，改成接收方授权、发送者推流。`n` 也提供 `/video/`，须先拆出播放职责。 |
| [`GalleryActivity.smali`](../../app/src/main/smali/com/alensw/PicFolder/GalleryActivity.smali#L1317)、[`shortcuts.xml`](../../app/src/main/res/xml/shortcuts.xml#L4)、[Manifest](../../app/src/main/AndroidManifest.xml#L131) | 优先保留 `com.alensw.transfer.TransferActivity` 的组件名，内部换成新界面：无入参时打开发现页；支持快捷方式的 `VIEW`、系统 `SEND/SEND_MULTIPLE`（`*/*`）、图库的 `EXTRA_STREAM` 列表，以及授予读取权的 `ClipData`。保留已有桌面入口。 |
| [`ui/c/at.smali`](../../app/src/main/smali/com/alensw/ui/c/at.smali#L114) → `TransferService` 的 `ACTION_VIEW` → `127.0.0.1` 上的 `/video/…` | 保留对播放调用的兼容，或明确改掉这个调用点。优先验证授权 `content://` 直交播放器；确需 HTTP Range 的提供者保留**独立、仅绑定 loopback** 的小代理和 206/416 语义。不把该代理挂在 LocalSend 的 LAN 端口上，也不删除 Manifest 中指向 `PlayerActivity` 的 `StreamingActivity` 别名。 |
| `org.apache.http.*`、`com.alensw.b.e.*`、其他 `JniUtils` 接口、共有资源 | 不是快传私有依赖。`cloud.a.ba/bg`、`cloud.ac`、`BasicAuthActivity` 等也引用 HTTP 类型。不要把整套库和图片 JNI 一并计作可删除体积。新界面可复用资源 ID，再清理仅用于旧协议的文案。 |

稳定版 DEX 索引：`com.alensw.transfer` **65 类** + `.transfer.a` **20 类**，合计 **85 类、239 个字段、470 个方法**。包外显式类型边是 `GalleryActivity → TransferActivity`、`ui.c.at → TransferService`；Manifest、字符串、XML 和已有桌面快捷方式不是这种类型边。标准工程保存这 85 个类的 Smali 文本，但 `bj,c,e,q` 这 **4 类实际由 javac/D8 产生**，其余 81 类由 Smali 汇编；只删 Smali 目录会漏掉它们。

现有 APK 编译输入：[原组播与端口](../../app/src/main/smali/com/alensw/transfer/a/a.smali#L1020)、[PeerHub 客户端](../../app/src/main/smali/com/alensw/transfer/a/k.smali#L141)、[旧 HTTP 路由](../../app/src/main/smali/com/alensw/transfer/n.smali#L1282)、[视频代理 URL](../../app/src/main/smali/com/alensw/transfer/n.smali#L1232)。

`OpenQPic/app/build.gradle` 对 javac 产物有完整的 `javaOwners` 白名单，Smali 又有单独的 `javaReplacements`。开发新变体时要把**新增 Java 类（含内部类）**、**替换的旧描述符**、**彻底删除的旧类**分开登记，清理 `app/recovered-java/{sources,replace}.txt`。不能把新描述符全部填进「排除已有 Smali」名单。原 `verifyStableDebug` 证明的是原包逐类/ELF 保真；新功能应有自己的验证任务或产品变体，允许明确列出的变化，继续核对无关图库类、资源和 JNI。`compileOnly` 头即便还能解析旧名字，也不能证明运行时有该类。

## 4. OpenQPicJni 的实现边界

| 模块 | 建议实现 |
|---|---|
| 原生发现与传输 | 在 `OpenQPicJni/native-src/` 下设独立 transfer 目标：UDP 组播、TCP 监听与出站连接、轻量 HTTP/1.1 请求/响应和 chunk 解码、超时/取消、文件描述符读写。只用 bionic/POSIX 和小型 HTTP 解析器；不引入第二套 C++ 运行时。优先覆盖 IPv4、两条并发文件流和零字节文件。 |
| 控制协议与 JNI | 跨 JNI 传配置、小 JSON、事件及已授权 FD，不逐块传 `byte[]`、不传任意存储路径。用 Android `org.json` 处理 wire DTO，平台随机数产生设备 ID、会话和独立文件 token；按文件流式验 SHA-256（native 增量实现，或 Java 流层的系统 `MessageDigest`）。单一会话状态所有者，线程回调汇入 Android 主线程；JNI 工作线程附着/分离、close/取消幂等。 |
| 状态与 I/O | `Idle → PendingDecision → Active → 完成/取消`；文件 `Pending → InProgress → 完成/失败/可重试`。等待人确认不能占满网络线程，也不能套用发现阶段的短超时。文件长度、进度及空间计算用 64 位，处理短读/写、磁盘满、丢网、同名冲突、超时回收。临时写入，成功后提交；SAF 文档提供者不能原子 rename 时采用能清理的暂存策略。 |
| Android 界面、文件与生命周期 | OpenQPic 持有 Activity、Service、接收对话框、文件选择与通知，负责 `ContentResolver`、SAF/MediaStore、媒体扫描。发送 `content://` 用授权 FD/流，明确 dup 或 detach 的唯一关闭方；对不可 seek、未知长度或有 offset 的内容先处理长度/边界，必要时临时落盘。`FileDto.size` 必填，不能借用 `InputStream.available()`。后台传输应有真正的前台通知；若升级 target SDK，按版本声明 `dataSync` 及对应权限/限制，不能把数据传输冒充 `mediaPlayback`。 |
| 网络与权限 | 当前 Manifest 已有 `INTERNET`、`ACCESS_NETWORK_STATE`、`ACCESS_WIFI_STATE`、`CHANGE_WIFI_MULTICAST_STATE`。使用 `WifiManager.MulticastLock` 并在退出时释放，绑定用户选定的本地接口，监听网络切换；API 23 兼容性可由 Java 枚举接口再交给 native。保留手动 IP+端口以应对组播被路由器过滤。当前 target 23；未来提高 target 时要审查明文网络策略和局域网权限，不要顺带放宽图库其他出站业务。 |

建议桥接操作是 `start/stop`、`announce/registerPeer`、`offer(files)`、`answer(session, selected)`、`provideFd(session,file,fd)`、`cancel`；事件是设备更新、待确认、文件进度、完成/失败。大文件始终走 FD，不要复用 `JniUtils.fuSocketPair` 当作网络连接：它只是 `AF_UNIX` socketpair，`fuCopyFD` 本身也没有 HTTP 分帧、长度/哈希校验或取消语义。

首版可把 Native 接收流分为「解析头 → 确定定长或 chunked → 逐块写 FD/更新哈希 → 完成或回滚」；只对小 JSON 控制消息整段缓存，并限制文件数、元数据及排队连接数。零字节也要完成一次有效提交。发送者选用已知 `Content-Length` 是合法的简化，但接收者必须接受两种分帧；接收端人尚未确认时，取消/发现等控制连接仍须能得到回应。

**链接方式是决定体积的关键：**

- **当前 App 保持原图像 ELF：** 推荐 OpenQPicJni 新增可独立加载的 `libqpictransfer.so`。它由同一仓库构建，OpenQPic 随包分发当前两个 ABI；只在进入快传时加载。不需要为一个网络功能把原本很小的整份图像库换掉。
- **产品本就采用 OpenQPicJni 重编的图像 ELF：** 将 transfer 作为内部静态对象合入 `libqpicjni156.so` 更合理；没有第二组 ELF 头/段和重复 STL。现有 `native-src/jni/exports.map` 只导出 `Java_com_alensw_jni_JniUtils_*` 和 `JNI_OnUnload`，`build_android.py` 又断言旧的 34 个 `Java_*`。新增类需要独立导出策略或 `JNI_OnLoad/RegisterNatives`，对新增桥做签名验证，继续单独核对原 34 个导出。`armeabi` 用 r16b；可选 `arm64-v8a` 用当前新移植版及 r28+ 的 16 KiB ELF 对齐，不依赖旧 arm64 实现。新增 ABI 必须同时提供图像库和传输功能，不能仅拷入一份 64 位网络库。

对应仓库：[NDK 构建与 ABI 校验](https://github.com/Sonic853/OpenQPicJni/blob/761302f/native-src/build_android.py#L70-L116)、[现有导出表](https://github.com/Sonic853/OpenQPicJni/blob/761302f/native-src/jni/exports.map)、[现有 FD 函数](https://github.com/Sonic853/OpenQPicJni/blob/761302f/native-src/src/files.cpp#L103-L145)。原生目标同时接入 `jni/Android.mk` 和 `CMakeLists.txt`；应用端把受支持 ABI 下的**已剥离**制品复制到对应 `app/src/main/jniLibs/<abi>/`，并按版本/摘要固定依赖。

## 5. 验收：什么才算「兼容」

1. 使用实际 LocalSend 桌面版与 Android 版，在关闭加密的同一 Wi-Fi 上分别由每边发起发现、注册、发送和接收。另测手动地址、切网重进、端口冲突、目标仅 HTTPS 时的清楚提示。
2. 双向测 0 字节、中文/emoji 文件名、同名文件、多文件只选一部分、两文件并发；覆盖定长与 chunked、文件大于 4 GiB 和非 seekable `content://`。核对落地 SHA 和媒体可见性，不能只看 HTTP 200。
3. 受理/拒绝、PIN、忙碌、读取文本后 204、等待确认时取消、中途断连、校验失败后整文件重试、空间不足；每种情形后下一次合法发送应能开始。用小型合成文件和本地接收目录测试。
4. 验证外部分享、图库多选、桌面快捷方式、旧视频转交/定位播放、前后台通知。核对新产品 DEX 不再含旧 QPIC 协议和 PeerHub 路径，同时不损伤非快传组件。

上游有可作为验收依据的[真实 HTTP 全流程用例](https://github.com/localsend/localsend/blob/d8702511/packages/core/tests/v2_server.rs#L305-L379)（两文件逐字节核对），以及同文件中的校验和、取消、PIN 测试。它们是上游参考用例，不是 OpenQPic 已完成端到端互传的证据。

**实施顺序：** 先用手动地址打通「OpenQPic 发 → 官方收」和「官方发 → OpenQPic 收」，两条都过了再做自动发现；随后接 Android 分享、存储、前后台、取消与并发；最后切换入口、下线旧协议、回归视频播放并冻结最终 APK 大小。这样若 UDP 被网络过滤，也能独立判断文件协议是否正确。
