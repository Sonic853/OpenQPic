# Apache HttpCore 恢复与 Maven 迁移记录

图库曾用 Maven Central 的 `org.apache.httpcomponents:httpcore:4.4.16` 处理回环视频 HTTP 和 Range。当前回环视频使用 [OpenQPicLocalSend 的独立回环监听](../../verification/native-video.md)，HttpCore 及原 Java HTTP 适配器不再进入安装包。下文是切换前的记录。

当时下载的[官方 JAR](https://repo.maven.apache.org/maven2/org/apache/httpcomponents/httpcore/4.4.16/httpcore-4.4.16.jar) SHA-256 为 `6c9b3dd142a09dc468e23ad39aad6f75a0f2b85125104469f026e52a474e464f`。JAR 含 253 个类型：249 个与原包同名，4 个为上游新增。上游移除了旧线程安全注解 `GuardedBy`、`Immutable`、`NotThreadSafe`、`ThreadSafe` 和内部 `PoolEntryFuture`，应用并未使用这些类型。

原 APK 未携带 `org/apache/http/version.properties` 或 HttpCore Maven 元数据。现在 `:app:verifyLocalSendDebug` 禁止 `org.apache.http` 类型定义及引用重新进入 APK/AAB。

## 原包记录

原包匹配的是 **4.4.1**。[恢复逐类清单](httpcore-types.tsv)覆盖当时的 254 个类型、715 个字段和 1,652 个方法；`former_java_source` 列为已移除的本地源码路径。原发行版曾与原包逐个比对，254 个类型和 1,440 个方法体均匹配。详见[全 Java 迁移时的原版记录](https://github.com/Sonic853/OpenQPic/blob/d78a05d/app/src/recovered/java/org/apache/RECOVERY.md)。

## 许可

4.4.16 的 [Apache License 2.0 全文](../../licenses/android-support-apache-2.0.txt) 和 [NOTICE](../../licenses/apache/httpcore-4.4.16-NOTICE.txt) 仍保留在仓库。旧版 JCIP 注解的附加条款见 [4.4.1 LICENSE](../../licenses/apache/httpcore-4.4.1-LICENSE.txt)。这些文件不打包进 APK/AAB。
