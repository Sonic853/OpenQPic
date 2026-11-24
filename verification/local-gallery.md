# 本地图库与 GitHub 更新验收

`2026.11.24`（`versionCode=175085184`），基于含多设备发送的 `7174ca2`。制品摘要见 [localsend-product.json](localsend-product.json)。

- **安装包**：`:app:verifyLocalSendDebug :app:assembleRelease` 通过。Debug APK/AAB 含相同的 DEX 与三组 ABI 的图库、LocalSend 库。按[精确移除清单](../app/removed-legacy-types.txt)核对：775 个旧类在 DEX 中既未定义，也未被引用；原版其余 1,472 个非快传类全部保留。918 个原资源 ID 仍在，废弃云资源等 74 个 ID 只保留空值。
- **升级和界面**：API 23 ARM 模拟器上，同签名 `9.7`（9700000）可原地升级。侧栏显示 Albums、Moments、Camera、WiFi transfer、Settings；更新页显示构建日期、GitHub 仓库，自动检查初始为关闭。
- **回归**：[相册和活动页](received-gallery-smoke.json)在接收媒体后自动刷新，打开目录中的计数从 1 到 3；[官方 LocalSend 对端收发](localsend-smoke.json)与[五台接收方的队列、取消及后台发送](multisend-smoke.json)均通过。以真实 MediaStore 视频验证了回环 HTTP 200、指定及后缀 Range 206、越界 416，播放器成功打开。
- **版本**：验证北京时间跨日、闰日、`p9 < p10`、次日高于前日 `p65535`、非法日期与补丁溢出。另以 `-PopenqpicVersion=2026.11.24p10` 构建并核对实际 APK/AAB 的 `versionCode=175085194`。发布流程锁覆盖版本分配、构建、发布；`actionlint 1.7.12` 检查通过。只读运行分配器时，仓库有 0 个标签、0 个 Release，选出的版本为 `2026.11.24`。
- **更新响应**：`tools/github_update_smoke.py` 在模拟器上加载已安装 APK 的更新代码，用固定 GitHub 响应验证：标准命名的 APK 附件、附件地址不匹配时进入对应发布页、404 无发布、403 检查失败、无效 JSON、预发布与旧格式标签。验收时仓库尚无正式 Release，未进行线上下载安装。

产品代码保留系统文档 URI 与本地 FD/HTTP Range，不再初始化旧网盘、扫描云 Provider、调度旧同步任务或记录友盟事件。此改动不清除用户媒体或重置图库设置。编译用的原版 ABI 头文件与历史验证记录不打入 APK。
