# OpenQPic 随包 Apache 库的 Java 源码

本目录的 **707 份 Java** 对应图库中 **828 个原 DEX 类型**。[逐类清单](recovery/ownership.tsv) 给出原描述符、Java 文件、Maven 坐标、成员数及声明指纹。内部类、匿名类和两个枚举分派表都有原名，无需重命名映射。接手时本包已有 176 份 Java、178 个已登记的类型。

| 实际匹配的发行版（链接为官方发布的 sources JAR） | Java 文件 | DEX 类型 | 字段 | 方法 | 有方法体 |
|---|---:|---:|---:|---:|---:|
| [HttpClient 4.5](https://repo.maven.apache.org/maven2/org/apache/httpcomponents/httpclient/4.5/httpclient-4.5-sources.jar) | 396 | 461 | 1,150 | 2,995 | 2,786 |
| [HttpCore 4.4.1](https://repo.maven.apache.org/maven2/org/apache/httpcomponents/httpcore/4.4.1/httpcore-4.4.1-sources.jar) | 241 | 254 | 715 | 1,652 | 1,440 |
| [Commons Codec 1.9](https://repo.maven.apache.org/maven2/commons-codec/commons-codec/1.9/commons-codec-1.9-sources.jar) | 56 | 85 | 277 | 625 | 605 |
| [Commons Logging 1.2](https://repo.maven.apache.org/maven2/commons-logging/commons-logging/1.2/commons-logging-1.2-sources.jar) | 14 | 28 | 124 | 337 | 312 |
| **合计** | **707** | **828** | **2,266** | **5,609** | **5,143** |

## 如何确认版本

1. 以实际 `app/src/main/smali/org/apache` 为准。对稳定 APK（SHA-256 `23ed7071608dc05465c6885a98c5884478defddc9731580929533bd3d9a6b107`）用 Apktool 2.12.1 重新反汇编，828 份文本逐一相同（仅统一换行）。
2. 比较 Maven Central 相邻发行版的类、字段和方法描述符；上表四个二进制 JAR 与原包正好对应，无缺失或多余声明。[HttpClient 4.5 父 POM](https://repo.maven.apache.org/maven2/org/apache/httpcomponents/httpcomponents-client/4.5/httpcomponents-client-4.5.pom) 独立印证另外三个版本。匹配的是 Apache 原发行版，无需套用 Android 平台 fork。
3. 将这四个**原发行 JAR 仅作比对样本**，用 [D8 1.4.93](https://storage.googleapis.com/r8-releases/raw/1.4.93/r8.jar)（`--debug --min-api 15`，工具 SHA-256 `ac7a31930ad75616f3b08ee7dcc8332899eba32b3f78c8de3126a76d39173710`）生成临时 DEX。逐类解析，与稳定 APK 比较父类、接口、成员、标志、注解、静态值、指令、寄存器、符号引用、分支和异常表：**828/828 类、5,143/5,143 个方法体完全匹配**。位置和局部调试信息不参与方法体比较；3,506 个方法的局部/参数调试记录有差异。旧 D8 输出的临时 DEX 头内 SHA-1 与标准计算不符，供严格读取器使用的副本仅重算了头部校验字段，原 APK 和代码区均未改变。此结果确认了源码选版，不表示现代 javac 会产生相同字节码。
4. 700 份 Java 与 sources JAR 内容一致（统一换行、行尾空格及文件末尾的空行）。保留各文件的原始许可头。只取定义随包类型的 Java；发行 JAR 附带的 `version.properties`、Mozilla 后缀表及 Codec 的 Beider–Morse 词典均未出现在原 APK，未引入它们来改变资源查找行为。

上述代码比较采用同一工作区 `OpenPic/structure/java-audit/overlay_dex.py` 的 `Image.model` 和 `body`。只从 `body` 的末项移除调试记录，保留寄存器头、指令流、所有异常区域和处理器，并独立比较引用集。按描述符排序的 828 类声明及代码的归一化汇总 SHA-256 为 `9983bf94d75963c3f91f16ca4b4dc840a2dd32a35ee57dd5455aadd9de4e188b`。

| 来源 | sources JAR SHA-256 | 用作比对的原发行 JAR SHA-256 |
|---|---|---|
| HttpClient 4.5 | `36951c2e276912621e958b98a118d9eca5bf7f6e8b1988897f14b13d98d7b613` | `a3c155991de1e35e851e828a67f894d101158c4124cc0f1df5f12d27cfa47772` |
| HttpCore 4.4.1 | `540be9b0e857a74b3b8def47774b8495682a6f62928dd7bce2e3feb2f32ffbb9` | `dd1390c17d40f760f7e51bb20523a8d63deb69e94babeaf567eb76ecd2cad422` |
| Commons Codec 1.9 | `9b675502f73a7a78c4ba543a68a448649875203d5be2281c94eb31dbee426f2c` | `ad19d2601c3abf0b946b5c3a4113e226a8c1e3305e395b90013b78dd94a723ce` |
| Commons Logging 1.2 | `44347acfe5860461728e9cb33251e97345be36f8a0dfd5c5130c172559455f41` | `daddea1ea0be0f56978ab3006b8ac92834afeefbd9b7e4e6316fca57df0fa636` |

## 七处源文件适配

- `GGSSchemeBase` 与 `RFC6265CookieSpecProvider`：现代 javac 对同一外部类里的 enum 直接使用 `ordinal()`，不生成原来的 `GGSSchemeBase$1` 和 `RFC6265CookieSpecProvider$2`。源码显式实现并使用原分派表：逐项设置、各自处理 `NoSuchFieldError`，分派值分别为 `[1,3,4,2]` 和 `[1,0,2]`。后者的 `$1` 仍是 IE 兼容策略的匿名路径处理器。
- `LogFactory`、`LogFactoryImpl`、`Log4JLogger`、`ServletContextCleaner`、`SimpleLog`：1.2 发布 JAR 使用旧 Java 编译器的类字面量翻译。显式恢复 11 个包可见 `class$...` 惰性缓存和 5 个 `class$(String)`，包括单参数 `Class.forName` 的初始化语义，以及查找失败时按原消息抛 `NoClassDefFoundError`。普通现代 `.class` 字面量没有这组语义。

## 现代工具链验证

- **javac 21.0.12.1 和 25.0.5，Java 8 源码/目标**：各产出 828 个 class，二者的类型、字段、方法描述符相同。JDK 21 以 Android 36 为 bootclasspath，仅加下面的外部 API 声明即可编译；JDK 25 同时加平台 legacy JAR 和工程 stable ABI 也能编译，包内实现仍来自这 707 份源码。
- **SDK 36.0.0 的 D8 8.10.9-dev，debug、min API 23**：保留全部 828 个原类、2,266 个字段、5,609 个原方法；5,143 个有实现的原方法仍有代码。各类的父类、接口、字段静态/实例归属、方法 direct/virtual 归属和静态初值一致。编译器新增 13 个 enum 的私有 `$values()` 和两个显式分派表的私有构造器；另为 `HttpServer.start/stop` 的 `AtomicReference.compareAndSet` 生成一个 `HttpServer$$ExternalSyntheticBackportWithForwarding0` 类及其 `m` 方法。故独立 D8 输出为 **829 类 / 5,625 方法**。
- 对 JDK 21 的现代 D8 编译结果，5,143 个原方法体中有 2,555 个连指令、寄存器和异常表都相同；5,082 个的符号引用集合相同。剩余差异包括寄存器和分支布局、字符串拼接/常量折叠、enum 初始化、旧 Dalvik 的 `Double.isNaN` 编译器处理、`BackoffStrategyExec` 不可达的 `close` 分支及上述 CAS 辅助函数。匿名类的 `final`、捕获字段/手写辅助成员的标志、内部类/参数元数据也有工具链差异。现代 D8 未保留部分 CLASS 保留期的 Apache 注解；注解仍在 Java 源码中。
- 当前产品其他 Smali 指令对本包有 **36 个不同成员引用**，30 个在指定类声明，另 6 个经父类/接口解析，全部可解析。稳定 APK 中其他包的 61 个不同成员引用（含已退出产品的类）也均可解析。现代及原 DEX 引用的非平台外部 API 同为下面的 20 个直接类型。DEX 系统注解单独计，不作为运行库依赖。
- 在宿主 JVM 用相同输入分别运行发布 JAR 与新编译的 Java：HTTP 请求类型、含引号逗号的头参数、URI 编码、三种 RFC6265 策略的路径校验/匹配、两张分派表、Logging 查找失败及选用 NoOpLog、Base64 往返和 SHA-256 结果一致。验证在临时目录进行，不依赖整工程 Gradle 或设备。

## 复现与接入

Android SDK 缺少原包直接引用的 20 个可选类型：JNDI 6、JGSS 6、Servlet 2、Avalon 1、LogKit 2、Log4j 3。`org.apache.http.legacy.jar` 不提供它们。下列工具从 JDK 自带 Java 8 `ct.sym` 和 [Commons Logging 自己声明的可选依赖](https://repo.maven.apache.org/maven2/commons-logging/commons-logging/1.2/commons-logging-1.2.pom) 提取声明：`avalon-framework:4.1.5`、`servlet-api:2.3`、`log4j:1.2.17`、`logkit:1.0.1`。含必要父类型共 **23 份外部声明，零方法体**，没有本表中的任何实现。首次读取四个 Maven 文件时校验固定 SHA-256；已有缓存可离线复用。

在 `OpenQPic/` 运行，例如：

```powershell
$r = 'app/src/recovered/java/org/apache/recovery'
$w = Join-Path $env:TEMP 'openqpic-apache-verify'
$sdk = (Resolve-Path '../OpenPic/.local/android-project/sdk').Path
$api = Join-Path $w 'apache-optional-api.jar'
python "$r/prepare_compileonly.py" --jdk "$env:JAVA_HOME" --cache "$w/downloads" --out $api
python "$r/verify.py" --jdk "$env:JAVA_HOME" --android-jar "$sdk/platforms/android-36/android.jar" --compile-api $api --d8 "$sdk/build-tools/36.0.0/lib/d8.jar" --work $w
```

验证器从空输出目录编译这些源文件，根据 [ownership.tsv](recovery/ownership.tsv) 核对 javac 身份及 D8 声明指纹。该表的 `java` 列去重后是 707 个源码路径；`descriptor` 列是 828 个原描述符，可用于合并总工程归属清单。与接手时相比新增 **531 个源码路径、650 个原描述符**。总工程接入时，`apache-optional-api.jar` 仅列为 `compileOnly`；现代 D8 的额外 CAS 辅助类归 D8 生成，不列入原 Smali 替换清单。

## 许可

四个组件均为 [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0)。仓库的 [`licenses/apache/`](../../../../../../licenses/apache/) 保留四份发行版各自完整的 LICENSE 和 NOTICE；应用的「关于」页可打开该仓库的许可目录。HttpCore 的 `org.apache.http.annotation` 含源自 Brian Goetz、Tim Peierls 的 JCIP 注解；其附加 [CC BY 2.5](https://creativecommons.org/licenses/by/2.5/) 条款全文保留在 HttpCore LICENSE 中。
