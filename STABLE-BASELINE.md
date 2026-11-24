# OpenPic Stable 恢复基线（OpenQPic `e4c35cb`）

以下构建命令、类数、原版 ELF 和验证数据对应历史提交 `e4c35cb`。当前产品使用 LocalSend，见 [README](README.md)。

**用 Android Studio 打开本仓库根目录 `OpenQPic/`，运行 `app`。** 这是独立的 Gradle build，只有一个 `com.android.application` 模块。默认 `debug` 是完整相册：`com.alensw.PicFolder`，版本 `9.7`（`9700000`），启动入口 `.GalleryActivity`。普通 `:app:assembleDebug` 就会编译 Java、汇编 Smali、链接资源并签名，无需事后拼接 APK。

默认构建有 **254 份实际 Java 源码 → 256 个 DEX 类**，另 **2,076 个类**用 Smali 汇编。Java 类清单在 [`app/recovered-java/`](app/recovered-java/)；完整的类和成员导航在恢复材料的 `structure/PACKAGES.md`。原生库源码单独维护在同一工作区的 `OpenQPicJni/` 仓库。

## 构建和安装

需要 JDK 21 或 25、Android SDK Platform **36**、Build Tools **36.0.0**。用 Android Studio 的 SDK Manager 安装，或运行 `sdkmanager "platforms;android-36" "build-tools;36.0.0"`。设置 `ANDROID_HOME`，或在本目录的本机 `local.properties` 中指定 `sdk.dir`。只装平台 37 不等于已装平台 36。无需 NDK。

```powershell
# 在 OpenQPic 仓库根目录
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:bundleDebug
.\gradlew.bat :app:assembleRelease
```

| 命令 | 本目录下的标准输出 |
|---|---|
| `:app:assembleDebug` | `app/build/outputs/apk/debug/app-debug.apk`，本机 Android 调试密钥签名 |
| `:app:bundleDebug` | `app/build/outputs/bundle/debug/app-debug.aab`，含同样两个 DEX 和两个 ABI |
| `:app:assembleRelease` | `app/build/outputs/apk/release/app-release-unsigned.apk`，自行签名后安装 |

首次构建需要下载 Gradle 9.2.1、AGP 9.0.1 和 Google Smali 3.0.9，使用 Google Maven/Maven Central。依赖和 SDK 已缓存后可加 `--offline`。macOS/Linux 使用 `./gradlew`。默认构建使用仓库内的 `compileOnly` ABI JAR，不需要原 APK、Apktool、Python 或 JNI 源码仓库。

在本工作区复用已下载的私有缓存（本机 `local.properties` 指向私有 SDK 36）：

```powershell
$env:GRADLE_USER_HOME = (Resolve-Path '..\OpenPic\.local\android-project\gradle').Path
.\gradlew.bat :app:verifyStableDebug --offline
```

Android Studio 选择 `app`、Build Variant `debug`、Launch `Default Activity` 和能运行 `x86` 或 `armeabi` 32 位库的设备。命令行部署等价入口：

```powershell
.\gradlew.bat :app:installDebug
adb shell am start -W -n com.alensw.PicFolder/.GalleryActivity
```

选择允许安装 targetSdk 23 的系统。新签名不能直接覆盖原签名同包名应用。工程不自动卸载、清数据或更改设备设置。

## 来源与编辑位置

| 源根或文件 | 如何进入最终包 |
|---|---|
| `app/src/main/AndroidManifest.xml` | 真正的 AGP Manifest，应用、组件、权限、intent filter、元数据均可编辑；包名与版本来自 `app/build.gradle`。资源链接时恢复原相对组件名及两条重复许可的原始声明次数。 |
| `app/src/main/res/` | 全部 298 个解码资源文件。原 `values/public.xml` 保留，`app/resource-ids.txt` 固定运行时 918 项 ID。没有预编译 `resources.arsc` 输入。 |
| `app/src/main/java/com/alensw/jni/JniUtils.java` | 来自 canonical 的逐字节副本，AGP **javac → D8**。32 个 native 声明、构造器、加载 `qpicjni156` 并执行 `init` 的静态初始化共 34 个方法。 |
| `app/src/recovered/java/` | 另外 **253 份真实 Java**、255 个类，与上一行共同构成默认的 Java 编译源集。含图库自有包及随 APK 发布的第三方类；普通 assemble 会采用对这些文件的编辑。 |
| `app/src/main/smali/` | 除 `JniUtils` 外保存 2,331 份原始 Smali。默认仅汇编 **2,076 类**；[归属清单](app/recovered-java/replace.txt) 中 255 类的 Smali 保留供对照，不重复入包。 |
| `app/compileOnly/stable-dex-abi.jar` | 包内类型的编译期声明，解决 Java 引用 Smali 类时的名称与原型；零方法体，不经 D8 或打包。 |
| `app/src/main/jniLibs/{armeabi,x86}/libqpicjni156.so` | 原稳定版的两份 ELF。逐字节打包，不重新编译、strip 或用现代 ABI 替代。 |
| 恢复材料的 `reference/java/`（模型中的 `reading`） | **2,163** 份完整 JADX 阅读材料。目录存在时，IDE 将它作为独立的阅读源根；它不参与 javac。 |

`smali` 和可选的 `reading` 由 Android Studio 的 V2 SourceProvider 提供；实测模型返回 `:app / APPLICATION`、`debug`、`release` 及上述源根。自定义源根在不同 IDE 版本的 Android 视图中显示位置可不同，可切到 Project 视图。混淆重命名、类型恢复错误和失败方法仍在阅读材料中；查精确实现以类描述符及可汇编 Smali 为准。

这是对稳定版单一 `main` 资源树的恢复。要加入依赖库的资源或新的 `src/debug/res` 覆盖层，须先扩展资源编译接缝；仅有 Smali 的自定义 View 也不保证能在 Studio 的 JVM 布局预览中实例化。

### 为什么资源有两次链接

原包的 `string/default`、`string/interface` 是有效 Android 资源，却不是有效 Java 字段名。AGP 资源合并/R 符号生成器会拒绝它们。`compile*StableResources` 从**同一份** `src/main/res` 生成运行时与编译期两套 AAPT2 输入。AGP 为 Java 生成 `R.string.stable_keyword_default`、`R.string.stable_keyword_interface`，对应原来的 `0x7f0a002a`、`0x7f0a003c`；运行时 AAPT2 表使用原名、原 ID、所有语言和限定符。替换的是签名前链接的资源档案，APK/AAB 的包装、签名、IDE 输出元数据及安装任务仍由 AGP 负责。

稳定版没有大写 `R/R$*` 的 DEX 定义，只有内联 ID。构建保留编译用 `R.jar`，对 D8 输入仅剔除这些生成类；大小写不同的真实类 `com.alensw.PicFolder.r` 保留。Smali DEX 接到 AGP 包装器的 DEX 输入。上述接缝固定在 AGP 9.0.1；升级插件请重跑全量验证。`app/lint-baseline.xml` 只记录原样式的一个误报，实际父链是 `ActionButton → ActionButton.Base → ActionButton.System → Android 框架样式`。

## 验证默认制品

此项额外需要 Python 3.10+ 和恢复材料目录，其中包含 `reference/OpenPic_stable.apk`、`reference/java/`、`tools/apktool-2.12.1.jar`。在本工作区会自动使用同级的 `../OpenPic/`，原便携工程的父目录布局也仍受支持。放在其他位置时，可将环境变量 `OPENPIC_RECOVERY_ROOT` 指向该目录的绝对路径。验证器先核对稳定版与 Apktool 的固定 SHA-256。

```powershell
.\gradlew.bat :app:verifyStableDebug --offline
python tools/check_studio_model.py
```

报告：`app/build/reports/stable-debug/verification.json`、`studio-model.json`。前者以**已签名 APK 和最终 AAB**为样本，后者通过 Gradle Tooling API 请求 Android Studio 使用的 V2 模型，不依赖 IDE 截图。`-Ppython=...` 可指定 Python 可执行文件。

本次测试的便携报告和制品摘要见 [`verification/`](verification/README.md)。

- DEX：基线 `2,332` 类 / `15,225` 方法；默认产物 `classes.dex` 为 `2,076 / 14,541`，`classes2.dex` 为 `256 / 684`。逐描述符重新反汇编，2,217 类文本全同，另 115 类仅有源文件或行号差异。最终 DEX 的字节分别与 Smali 汇编器和 AGP D8 输出一致。[Java DEX 核验](verification/standard-java-dex.json)另行比对 256 个类（144 个方法体）的指令、寄存器、引用、异常表和注解。
- Manifest：对比最终二进制 XML 树（忽略 XML 源行号）。debug 唯一内容差异是 `android:debuggable=true`；含 `9` 个 Activity、`1` 个别名、`2` 个 Provider、`1` 个 Service、`14` 个 intent filter、`29` 条权限声明。
- 资源：对比所有 918 个 `类型/名 → ID`、编译期 `R.txt`、完整资源表 dump 及 298 个解码资源。AAPT2 将 20 个 mipmap PNG 的 ZIP 内部路径写成 `*-v4`；密度选择、图片字节、解码资源不变。另验 50 个 raw 项及所有非签名 payload 的清单；比原包仅增加第二个 DEX 与 AGP 元数据。
- native：`armeabi` SHA-256 `e5fd228d0794f113b0988c1087bbdc297d48090732ae6867daa0095443da7340`；`x86` SHA-256 `5277d455bff687ce8749709d22002dc84fcdbde951a9290c2e26fbaa268823df`。AAB 内这两项与 APK 相同。
- `apksigner verify`、`zipalign -c 4`；AAB 最终 DEX/ELF 和转换回二进制的 Manifest/资源表亦与 APK 核对。[原版](verification/original-launch.json)与[重建包](verification/standard-launch.json)的模拟器记录另行核对图库启动、JNI 库映射和设置页文本。

## 扩展真正参与 javac 的源码

默认构建完全独立于 `structure/java-audit`。`app/recovered-java/sources.txt` 与 `replace.txt` 分别记录 253 份额外的 Java 路径和 255 个原 DEX 描述符；`JniUtils.java` 是固定的主源。若要将一个新单元纳入默认源，请将 Java 放在 `app/src/recovered/java/` 对应包目录，更新这两份清单（包括内部类），然后重跑 `:app:verifyStableDebug`。该任务按原版验收未改写的恢复结果；有意修改应用行为时仍可用普通 `assembleDebug` 构建编辑后的代码。

也可以用 `-PstableJavaOverlay=<目录>` 独立试编：以 `JniUtils` 为起点，暂时采用指定清单**代替**内建的 253 份源。`-PstableCompileAbi=true` 启用同一 **`compileOnly`** 声明 JAR。`-PstableSingleJava=true` 则重建只有 `JniUtils` 经 javac 的历史对照版本，输出在 `build/single-java/app/`。

工程自带极小示例：从 canonical 选取会引用 `ProtocolVersion` 的 `RequestLine` 和具有真实构造器的 `NoHttpResponseException`。示例和用户自选源码都写到 **`build/java-overlay/app/`** 下，与默认 APK 独立。

```powershell
.\gradlew.bat :app:verifyStableDebug -PstableJavaOverlay=examples/java-overlay -PstableCompileAbi=true
# 仅使用有独立 SourceFile 证据的 176 份额外 Java
.\gradlew.bat :app:verifyStableDebug -PstableJavaOverlay=examples/verified-overlay -PstableCompileAbi=true
```

自建 overlay 的格式：

1. `sources.txt`：每行一个相对恢复材料 `reference/java/` 的 `.java` 路径，保持原文；需要人工修复的 Java 可放在 overlay 的 `java/` 包目录中。二者可以并用；同一文件只提供一次。
2. `replace.txt`：每行一个要替换的**原 DEX 描述符**（如 `Lorg/apache/http/RequestLine;`），列出包括内部类在内的全部额外 javac 输出。`JniUtils` 已由主源集提供，不要再列。
3. 构建核对 javac 实际 class 名与清单；仅从临时 Smali 源树排除对应描述符。打包前对本次 D8 的额外类再反汇编，与待替换的 Smali 对照，只有声明与指令匹配才继续。全量 `verifyStableDebug` 仍检查最终包中所有类。该规则保守：即使另一种指令排列可能等价，也不会自动认定通过。

`java-audit` 的「1,145 份能联合编译」只是 javac 结果，不能整表直接当作等价替换。不要把 ABI/辅助头的 class 文件复制到应用，也不要把异常占位的方法改成空方法。

## 功能设计评估

[用 OpenQPicJni 实现兼容 LocalSend v2.2 的明文 Wi-Fi 快传](docs/localsend-http/README.md)：协议、旧实现的替换范围、Android 接口和 APK 体积实测。此为设计方案，当前应用仍使用原快传。
