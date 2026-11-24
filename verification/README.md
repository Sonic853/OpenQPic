# 本次标准 Android 工程的验证快照

以下是原版恢复基线 `e4c35cb` 的交付记录。当前 LocalSend 产品使用 `:app:verifyLocalSendDebug`；见[当前产品验证](../docs/localsend-http/IMPLEMENTATION.md)。需要重验历史保真时，在基线提交运行 `:app:verifyStableDebug`。

当前快传：[APK/AAB 摘要](localsend-product.json) · [官方对端收发](localsend-smoke.json) · [多设备发送](multisend-smoke.json) · [接收后图库自动刷新](received-gallery-smoke.json)。

| 记录 | 含义 |
|---|---|
| [`default-debug.json`](default-debug.json) | 正常 Gradle `debug` APK / AAB，全部类、方法、资源、Manifest、native、签名和 Java 来源检查 |
| [`studio-model.json`](studio-model.json) | 实际 Gradle Tooling API V2 模型；路径是测试机路径，工程脚本中不依赖这些绝对路径 |
| [`single-java-control.json`](single-java-control.json) | 第一阶段对照 APK，只有 `JniUtils` 用 Java；不是最终交付的默认 APK |
| [`sourcefile-overlay.json`](sourcefile-overlay.json) | 中间对照，`JniUtils` 加 176 个有原始 SourceFile 依据的 Java 文件，最终 DEX 179 类来自 Java |
| [`example-overlay.json`](example-overlay.json) | 额外两份 canonical Java 替代对应 Smali 后的独立 APK/AAB；三份 javac 类与 D8 制品来源检查 |
| [`rejected-overlay.json`](rejected-overlay.json) | 负对照：只改 `NoHttpResponseException` 构造器从 `super(str)` 调用为 `super()`，仍能编译，打包前校验拒绝；私有干净输出目录未产生 APK |

默认 APK SHA-256：`56e9768377c074ef07334563fc835ab69eba49910db6a6eaa480fdb7412b88d7`。真实 Java 254 份，来自 Java 的 DEX 类 256 个。工作区 JDK 25 和独立目录 JDK 21（无相邻 Java 阅读树/稳定 APK，禁用构建缓存、同一调试密钥）输出相同的已签名 APK，[复建记录](standalone-jdk21.json)。不同机器的调试证书会影响已签名 APK 的摘要。

新的默认 `assembleRelease` 也生成了 `app-release-unsigned.apk`；[release 对照](release-app-diff.json)中 Manifest 属性及组件无差异，仍为 2,076 / 256 类，两份原 ELF 均在。模拟器记录见[原版](original-launch.json)和[重建包](standard-launch.json)。
