# 开源代码许可 / Open-source licenses

这里列出 OpenQPic 当前使用的第三方组件及其许可证和版权声明。应用内可通过「设置 → 关于 → 开源代码许可」打开此目录。

| 组件 | 许可证 / 版权声明 |
| --- | --- |
| AndroidX Core、DocumentFile、DrawerLayout、ViewPager 及其 AndroidX 依赖 | [Apache License 2.0](android-support-apache-2.0.txt) · [版权声明](android-support.txt) · [版本与来源](../docs/recovery/android-support.md) |
| Kotlin 标准库、kotlinx.coroutines、JetBrains Annotations、Guava ListenableFuture、JSpecify 注解 | [Apache License 2.0](android-support-apache-2.0.txt) · [上游声明与 Boost Software License 1.0](jvm-libraries.txt) · [实际打包的版本](../app/runtime-libraries.sha256) |
| picohttpparser（LocalSend 和回环视频） | [MIT 许可证及版权声明](picohttpparser.txt) |

这些文件不打包进 APK/AAB。再分发二进制时，请按各组件条款向接收者提供适用的许可证和版权声明。

## 旧版安装包

下列库已不在当前安装包中，但为此前安装的版本保留许可和版权声明：

| 组件 | 许可证 / 版权声明 |
| --- | --- |
| Apache Commons Codec 1.9 | [LICENSE](apache/commons-codec-1.9-LICENSE.txt) · [NOTICE](apache/commons-codec-1.9-NOTICE.txt) |
| Apache Commons Logging 1.2 | [LICENSE](apache/commons-logging-1.2-LICENSE.txt) · [NOTICE](apache/commons-logging-1.2-NOTICE.txt) |
| Apache HttpClient 4.5 | [LICENSE](apache/httpclient-4.5-LICENSE.txt) · [NOTICE](apache/httpclient-4.5-NOTICE.txt) |
| Apache HttpCore 4.4.16 | [LICENSE](android-support-apache-2.0.txt) · [NOTICE](apache/httpcore-4.4.16-NOTICE.txt) |
| Apache HttpCore 4.4.1 | [LICENSE](apache/httpcore-4.4.1-LICENSE.txt) · [NOTICE](apache/httpcore-4.4.1-NOTICE.txt) |
| 旧版定制 Android Support Library 与手工恢复的 AndroidX Core 子集 | [Apache License 2.0](android-support-apache-2.0.txt) · [版权声明](android-support.txt) |
