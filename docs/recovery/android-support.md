# 定制 Android Support 与 DocumentFile

[`app/src/recovered/java/android/support`](../../app/src/recovered/java/android/support/) 保留定制控件和旧 `FileProvider` 的 **57 份 Java、60 个原类型**，保留 **202 个原字段和 446 个原方法**（含构造器和类初始化器）。DocumentFile 的 5 个类型改为从 Google Maven 引入。全 Java 迁移时恢复过的 13 个 `androidx.core` 类型已因未使用而移除，[最初 78 类的恢复记录](https://github.com/Sonic853/OpenQPic/blob/d78a05d/docs/recovery/android-support.md)可供对照。

[逐类型清单](android-support-types.tsv) 给出 DEX 描述符、恢复时的 Java 相对路径及原成员数。还原依据是迁移前 `app/src/main/smali/{android/support,androidx}` 的指令和控制流；此范围的基线文本与旁边恢复材料 `OpenPic/app/src/main/smali` 一致。

| 包 | Java / 原类型 / 原方法 | 对应实现 |
|---|---:|---|
| `android.support.a.a` | 13 / 13 / 64 | `a` 抽屉开关；`b` 构造器标记；`c–d` 委托接口；`e–f` 箭头位置；`g–i` 各系统版本的 action bar；`j` 滑动图标；`k–l` 反射和 home 视图查找；`m` 可绘制箭头。 |
| `android.support.v4.a` | 27 / 27 / 168 | `a–e` 重力；`f–i` 触摸指针；`j` 页适配器；`k–o` 速度；`p` 分页视图；`q–z`、`aa` 排序、布局、监听、数据观察和滚动。 |
| `android.support.v4.widget` | 16 / 17 / 191 | `DrawerLayout` 和静态 `SavedState`；`a–i` 窗口 inset、抽屉回调和布局；`j–k` 边缘效果与滚动；`l–o` 拖动、插值、回调。 |
| `android.support.v4.content` | 1 / 3 / 23 | `FileProvider`、`PathStrategy`、`SimplePathStrategy`。 |

## 从 Google Maven 引入 DocumentFile

固定使用 [`androidx.documentfile:documentfile:1.1.0`](https://dl.google.com/dl/android/maven2/androidx/documentfile/documentfile/1.1.0/documentfile-1.1.0.aar)。AAR 的 SHA-256 为 `684e0ffcb21a660c939d1bb814521f42c4bd74816f4a117291fe6a101cf3a822`，保留原来的 **5 个类型**，不声明 Android 资源，库的最低版本为 API 21。

1.1.0 声明了 `androidx.core:core:1.7.0` 并实际调用 `DocumentsContractCompat`。其中 `isDocumentUri`、`getDocumentId`、`getTreeDocumentId`、`buildDocumentUriUsingTree` 在 API 21 及以上直接委托给同签名的系统 API。因此构建在 D8 前仅将这四种调用链接到系统的 `DocumentsContract`，无需打包 Core，图库源码不变。AndroidX Annotation `1.8.1` 与 JSpecify `1.0.0` 仅在编译期使用。

`com.alensw.ui.activity.p` 仍通过 `DocumentFile.fromTreeUri(...).createFile(...)` 处理图库目录授权结果。URI 已是 document URI 时沿用 document ID，否则使用 tree ID；列表、元数据查询和单文档操作仍使用这一版本的实现。

## 跟随实包的行为

- **图库版开关与抽屉。** `android.support.a.a.a` 的 `Object` 参数是 `Drawable` 时使用 `j` 滑动图标，是整数时给 `e` 箭头着色；委托按 Activity 自带接口或 API 18 / 11 / 更早的分支选择。`m` 保留 24dp 尺寸、18dp 杠宽、2dp 线宽、3.2dp 间距、11.31dp 箭头和 16dp 箭杆，使用主题 `textColorPrimary`。它按原方式将传入 alpha 与画笔现有 alpha 相乘。`DrawerLayout` 用左抽屉的上下边距绘制内容遮罩；状态按抽屉保存，通过 `(View, 是否关闭, 状态)` 通知监听器。`SavedState` 虽有两个锁定模式字段，Parcel 只写读打开抽屉的 gravity。
- **拖动与分页。** 保留两边拖动器的非短路求值、多个指针逐个检查、取消手势时的零浮点速度释放，以及连续的浮点偏移。页适配器返回的页面对象保持 `Object`；没有误转成 `u`。空页的 `a(0, 0.0f, 0)` 与取消拖动的 `a(0.0f, 0.0f)` 显式选择原 Smali 的浮点重载。`j` 的 `ViewGroup` 重载转调 `View` 重载，供图库子类覆盖。
- **文件授权。** `FileProvider` 保留 authority 缓存锁、XML 根映射、最长根匹配、查询列筛选、各打开模式及原异常，拒绝 insert/update。图库 Manifest 将它注册为 `com.alensw.PicFolder.provider`，与 `.FileProvider` 不同。

## 引用和源码级差异

`GalleryActivity` 和 `res/layout/main.xml` 使用 `DrawerLayout`；`TransferActivity` 的 Java 创建分页视图并安装 `j` 和 `x`。`PlayerActivity`、`HomeButton` 使用 `m`，`ImageGridView` 等使用滚动适配器 `k`。定制 Support 源码可仅依赖 Android 平台编译。

两个原调用不能由 API 36 的公有 SDK 加定制 Support 类直接解析：

| 原调用 | 源码如何处理 |
|---|---|
| `android.support.v4.content.ContextCompat.getExternalFilesDirs/getExternalCacheDirs` | 原 APK 无此旧包类。仅在解析 `external-files-path` / `external-cache-path` 时，`FileProvider.legacyExternalDirs` 用原二进制名和原签名调用；缺类为 `NoClassDefFoundError`。本产品 `provider_paths.xml` 当前使用的是独立的 `external-path` 分支。 |
| `Canvas.save(int)` | 旧的单参数重载不在 API 36 公有 `android.jar` 声明中。`m.saveMatrix` 缓存该方法，按原实参 `1`（只保存矩阵）调用，不变成同时保存裁剪的 `save()`。 |

这些查找都在原分支执行，不依赖被删除库的占位实现；被调用方法抛出的异常解包后原样传播。反射查找失败的异常信息、堆栈可因运行时而异。

这些兼容处理没有新增二进制类或公开构造器。实现增加了 `m.matrixSave`、私有兼容调用和异常转发方法，以及 Java 对原本没有任何构造器的 8 个类所需的私有构造器（`a.a.{b,k}`、`v4.a.{a,e,f,k,o}`、`v4.widget.i`，此处均省略 `android.support.`）。

原 DEX 中由编译器生成但被混淆成普通名称的访问器、委托构造器、比较器桥及 `Creator` 桥，现以原签名显式书写；Java 不能为显式成员标记 `synthetic`。`DrawerLayout.SavedState` 为静态嵌套类，原文件无内部类元数据，未添加外层实例参数。省略了发行包未定义的 Support/AndroidX **build-retention** 注解（如 `Nullable`、`RequiresApi`、`GuardedBy`）；框架回调、权限和 SDK 判断仍在方法体内。因此这是可维护的行为恢复，不是逐字节复刻。

## 来源与许可

- 行为依据：迁移前的 Smali。`OpenPic/reference/java` 是阅读对照，来自 `OpenPic_stable.apk`（SHA-256 `23ed7071608dc05465c6885a98c5884478defddc9731580929533bd3d9a6b107`）。
- Google Maven 源码：恢复时使用的是 [`androidx.documentfile:documentfile:1.0.1`](https://dl.google.com/dl/android/maven2/androidx/documentfile/documentfile/1.0.1/documentfile-1.0.1-sources.jar) 和 [`com.android.support:support-core-utils:27.1.1`](https://dl.google.com/dl/android/maven2/com/android/support/support-core-utils/27.1.1/support-core-utils-27.1.1-sources.jar) 的 `FileProvider`。这是选取的可核对源码版本，不是从 APK 调出的依赖锁文件。
- AOSP `platform/frameworks/support` 的 [`android-5.1.1_r1`](https://android.googlesource.com/platform/frameworks/support/+/android-5.1.1_r1/v4/java/android/support/v4/)：按实际描述符和分支适配 `ViewPager`、`ViewDragHelper`、`DrawerLayout` 的对应方法。`android.support.a.a` 等混淆代码按图库 Smali 和阅读稿恢复，没有直接替换成同名现代控件。
- 使用的 AOSP / AndroidX 上游源码版权属于 The Android Open Source Project，许可为 [Apache License 2.0（全文）](../../licenses/android-support-apache-2.0.txt)。选用上游源的文件保留原版权头并标注此项目的修改；原 APK 私有修改的确切作者和独立许可未由 Smali 证明。本说明不将图库其他代码重新许可。

## 定制 Support 独立验证

在 `OpenQPic` 根目录可用以下 PowerShell 命令复核。Java 8 源/目标，API 36 为 bootclasspath；普通 classpath 和 sourcepath 均为空。无需 Gradle、ABI JAR 或预编译实现。

```powershell
$sdk = (Resolve-Path '..\OpenPic\.local\android-project\sdk\platforms\android-36\android.jar').Path
$tmp = Join-Path $env:TEMP ('openqpic-support-' + [guid]::NewGuid())
$classes = New-Item -ItemType Directory -Path "$tmp\classes" -Force
$empty = New-Item -ItemType Directory -Path "$tmp\empty"
$files = Get-ChildItem -Path 'app/src/recovered/java/android/support' -Recurse -Filter '*.java'
$sources = $files | ForEach-Object { '"' + $_.FullName.Replace('\','/') + '"' }
[IO.File]::WriteAllLines("$tmp\sources.txt", [string[]]$sources, [Text.UTF8Encoding]::new($false))
javac -source 8 -target 8 -encoding UTF-8 -proc:none -bootclasspath "$sdk" `
  -classpath "$empty" -sourcepath "$empty" -d "$classes" "@$tmp\sources.txt"
if ($LASTEXITCODE -ne 0) { throw 'javac failed' }
"$($files.Count) Java -> $((Get-ChildItem $classes -Recurse -Filter '*.class').Count) classes"
```

当前 Support 源码集对应 `57 Java -> 60 classes`。原迁移阶段已用 D8 逐描述符核对，并以宿主 JVM 测试空页回调、半页像素偏移、多指拖动、左右抽屉和 `save(1)`。DocumentFile 的发布 AAR 和最终 DEX 归属由 `:app:verifyLocalSendDebug` 另行检查。
