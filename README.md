
# Oops
[![jcenter](https://api.bintray.com/packages/iota9star/nichijou/oops/images/download.svg)](https://bintray.com/iota9star/nichijou/oops/_latestVersion) [![Build Status](https://travis-ci.org/iota9star/oops-android-kt.svg?branch=master)](https://travis-ci.org/iota9star/oops-android-kt) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/5d36b3333d02491499c8c0b6d1765d42)](https://app.codacy.com/app/iota9star/oops-android-kt?utm_source=github.com&utm_medium=referral&utm_content=iota9star/oops-android-kt&utm_campaign=Badge_Grade_Dashboard)[![License](https://img.shields.io/hexpm/l/plug.svg)](https://www.apache.org/licenses/LICENSE-2.0.html) [![API](https://img.shields.io/badge/API-16%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=16)
### Oops - Android Material Theme engine, Powered by LiveData & ViewModel.
一个基于**LiveData** & **ViewModel** 的Android **Material** 主题引擎，由**Kotlin**实现。本主题引擎通过简单的方式实现各个**View**间无缝的色彩切换。整个过程除了当您更改了主题时才会触发重启**Activity**外，其余过程均无需重启**Activity**。当前版本处于`alpha`阶段，所有`API`均可能变动


下载示例应用：[simple-release.apk](https://github.com/iota9star/oops-android-kt/raw/master/simple/release/simple-release.apk "simple-release.apk")

![demo](https://github.com/iota9star/oops-android-kt/blob/master/simple/release/demo.gif "demo")

----
### API
``` java
// java
Oops.immed().isFirstTime()// 用于判断是否是第一次配置，初始化需要用到配置的颜色
Oops.bulk()
        .themeSet(...) // 设置当前的主题，设置主题会导致activity重启
        .isDarkSet(...)// 设置当前主题是否为黑色主题
        .windowBackgroundSet(...)// 设置当前主题的背景色
        .windowBackgroundResSet(...)
        .colorAccentSet(...)// 设置Android 自带属性的基本颜色
        .colorAccentResSet(...)
        .colorPrimarySet(...)
        .colorPrimaryResSet(...)
        .colorPrimaryDarkSet(...)
        .colorPrimaryDarkResSet(...)
        .textColorPrimarySet(...)
        .textColorPrimaryResSet(...)
        .textColorPrimaryInverseSet(...)
        .textColorPrimaryInverseResSet(...)
        .textColorSecondarySet(...)
        .textColorSecondaryResSet(...)
        .textColorSecondaryInverseSet(...)
        .textColorSecondaryInverseResSet(...)
        .statusBarColorSet(...)// 设置状态栏颜色
        .statusBarColorResSet(...)
        .statusBarModeSet(...)// 设置状态栏文本图标的显示模式
        .navBarColorSet(...)// 设置导航栏颜色
        .navBarColorResSet(...)
        .putStaticStatusBarColor(...) // 设置指定activity的状态栏颜色
        .putStaticStatusBarColorRes(...)
        .putStaticNavBarColor(...) // 设置指定activity的导航栏颜色
        .putStaticNavBarColorRes(...)
        .toolbarTitleColorSet(...)// 设置toolbar相关颜色
        .toolbarTitleColorResSet(...)
        .toolbarSubtitleColorSet(...)
        .toolbarSubtitleColorResSet(...)
        .toolbarIconColorSet(...)
        .toolbarIconColorResSet(...)
        .navViewSelectedColorSet(...)// 设置NavigationView未选中时文本和图标的颜色
        .navViewSelectedColorResSet(...)
        .bottomNavigationViewNormalColorSet(...)// 设置BottomNavigationView未选中时文本和图标的颜色
        .bottomNavigationViewNormalColorResSet(...)
        .bottomNavigationViewSelectedColorSet(...)// 设置BottomNavigationView选中时文本和图标的颜色
        .bottomNavigationViewSelectedColorResSet(...)
        .tabLayoutTextColorSet(...)// 设置TabLayout未选中时文本和图标的颜色
        .tabLayoutTextColorResSet(...)
        .tabLayoutSelectedTextColorSet(...)// 设置TabLayout选中时文本和图标的颜色
        .tabLayoutSelectedTextColorResSet(...)
        .swipeRefreshLayoutSchemeColorSet(...)// 设置SwipeRefreshLayout SchemeColor
        .swipeRefreshLayoutSchemeColorResSet(...)
        .swipeRefreshLayoutSchemeColorsSet(...)
        .swipeRefreshLayoutSchemeColorsResSet(...)
        .swipeRefreshLayoutBackgroundColorSet(...)// 设置SwipeRefreshLayout 圆形背景色
        .swipeRefreshLayoutBackgroundColorResSet(...)
        .snackBarTextColorSet(...)// 设置snackbar 文本的颜色
        .snackBarTextColorResSet(...)
        .snackBarActionColorSet(...)// 设置snackbar 按钮颜色
        .snackBarActionColorResSet(...)
        .snackBarBackgroundColorSet(...)// 设置snackbar 背景色
        .snackBarBackgroundColorResSet(...)
        .collapsingToolbarDominantColorSet(...)// 设置CollapsingToolbar 的主色调，以便于标题在展开时文本和图标显示黑色还是白色
        .collapsingToolbarDominantColorResSet(...)
        .attrColorSet(...)// 设置自定义attr的颜色
        .attrColorResSet(...)
        .rippleViewSet(...)// 设置动画开始的view
        .rippleAnimDurationSet(...)// 设置动画的时长
        .apply(...)// 应用上面的设置
Oops.immed().removeStaticStatusBarColor()// 移除之前设置的指定activity状态栏的颜色
Oops.immed().removeStaticNavBarColor()// 移除之前设置的指定activity导航栏的颜色
```
### 基础使用
#### -> 在应用中使用
- 在你应用的``module``的``build.gradle``中添加
``` gradle
dependencies {
  // 其他
  implementation 'io.nichijou:oops:0.8.0'
}
```
- Activity直接继承OopsActivity，或在activity的`onCreate`方法中调用`Oops.attach(this)`，在`setContentView`之前调用
``` kotlin
// kotlin
class PrimaryActivity : OopsActivity()
```
``` java
// java
class PrimaryActivity extends OopsActivity
```
或
``` kotlin
// kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    Oops.attach(this)
    super.onCreate(savedInstanceState)
}
```
- 在xml布局文件中
``` xml
<TextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:textColor="?colorAccent"/> //该文本控件字体颜色将会变成你设置的colorAccent的值，详细请看示例应用simple
```
#### -> 获取Oops实例
``` kotlin
// kotlin 和 java
Oops.immed()// 返回一个Oops实例

Oops.bulk()// 返回一个Oops实例，并且开启事务，批量修改属性，需要调用apply()方法使配置生效
```
#### -> Oops获取颜色
``` kotlin
// kotlin
Oops.immed().colorAccent// 获取colorAccent的颜色值

Oops.bulk().colorAccent// 获取colorAccent的颜色值，与immed()无区别，但不建议
```
``` java
// java
Oops.immed().getColorAccent();// 获取colorAccent的颜色值

Oops.bulk().getColorAccent();// 获取colorAccent的颜色值，与immed()无区别，但不建议
```
#### -> Oops修改颜色
``` java
// java
public void foo(){

    Oops.immed().setColorAccent(Color.WHITE);// 使用set方法设置colorAccent颜色为白色，不可链式调用

    Oops.immed()
            .colorPrimarySet(Color.RED)
            .colorAccentSet(Color.GREEN);// Oops会立刻修改colorPrimary为红色，并且修改colorAccent为绿色，中间分别调用了两次保存属性，可链式调用，调了几次就保存几次

    Oops.bulk()
            .colorAccentSet(Color.BLUE)
            .colorPrimarySet(Color.WHITE)
            .apply();// Oops会修改colorPrimary为白色，并且修改colorAccent为蓝色，中间只调用了一次保存属性；如果使用bulk()而未调用apply()方法，中间调用Set结尾方法均不会生效，set开头方法可生效，但不可链式调用
}
```
``` kotlin
// kotlin
fun foo(){

	Oops.immed().colorAccent = Color.WHITE// 直接设置colorAccent为白色

    Oops.immed()
            .colorPrimarySet(Color.RED)
            .colorAccentSet(Color.GREEN)// Oops会立刻修改colorPrimary为红色，并且修改colorAccent为绿色，中间分别调用了两次保存属性

    Oops.bulk()
            .colorAccentSet(Color.BLUE)
            .colorPrimarySet(Color.WHITE)
            .apply()// Oops会修改colorPrimary为白色，并且修改colorAccent为蓝色，中间只调用了一次保存属性

    Oops.bulk {
            colorAccent = Color.WHITE
            colorPrimarySet(Color.RED)
    }// kotlin 方法，无需调用apply()，修改colorAccent为白色，colorPrimary为红色，中间只调用了一次保存属性
}
```
#### -> 忽略不修改颜色
- 在布局文件
``` xml
<TextView
    android:layout_width="0dp"
    android:layout_height="wrap_content"
    android:layout_weight="1"
    android:text="@string/text_color_secondary"
    android:textColor="?android:textColorSecondary"
    android:textSize="16sp"
    android:tag="@string/oops_ignore_view" // Oops 提供的string资源，或者直接设置android:tag="oops_ignore_view"，字符串 'oops_ignore_view'
    android:textStyle="bold" />
```
#### -> Oops切换动画，仅支持LOLLIPOP以上机型
``` java
// java
public void foo(){

    Oops.bulk()// 只有调用bulk()方法时才支持动画属性
            .rippleViewSet(view)// 你点击的view或其他view，动画开始的起点
            .rippleAnimDurationSet(480)// 动画执行的时间
            .apply();// 调用apply()方法调用时开始执行动画
}
```
``` kotlin
// kotlin
fun foo(){

    Oops.bulk()// 只有调用bulk()方法时才支持动画属性
            .rippleViewSet(view)// 你点击的view或其他view，动画开始的起点
            .rippleAnimDurationSet(480)// 动画执行的时间
            .apply()// 调用apply()方法调用时开始执行动画

    Oops.bulk {
            rippleView = view// 你点击的view或其他view，动画开始的起点
            rippleAnimDurationSet(480)// 动画执行的时间
    }// 调用即开始动画
}
```
### 高级使用
#### -> 自定义属性
- 新建``attrs.xml``文件，自定义属性值
``` xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <attr name="customColor1" format="color" />// 自定义属性1
    <attr name="customColor2" format="color" />// 自定义属性2
</resources>
```
- xml布局文件中使用
``` xml
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="24dp"
    android:layout_marginBottom="12dp"
    android:gravity="center_vertical">
    <TextView
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:text="@string/custom_attr_color1"
        android:textColor="?customColor1" // 文字颜色1
        android:textSize="16sp"
        android:textStyle="bold" />
    <TextView
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:text="@string/custom_attr_color2"
        android:textColor="?customColor2" // 文字颜色2
        android:textSize="16sp"
        android:textStyle="bold" />
</LinearLayout>
```
- 给属性赋值
``` java
// java
Oops.immed().setAttrColor(context, R.attr.customColor1, Color.WHITE);// 上面步骤中布局文件中的文字颜色将会更改为白色
Oops.bulk()
        .attrColorSet(context, R.attr.customColor1, Color.WHITE)
        .attrColorResSet(context, R.attr.customColor2, R.color.colorAccent)
        .apply();// 上面步骤中布局文件中的文字颜色customColor1将会是白色，customColor1将会是R.color.colorAccent的颜色
```
``` kotlin
Oops.immed().setAttrColor(context, R.attr.customColor1, Color.WHITE)// 上面步骤中布局文件中的文字颜色将会更改为白色
Oops.bulk()
        .attrColorSet(context, R.attr.customColor1, Color.WHITE)
        .attrColorResSet(context, R.attr.customColor2, R.color.colorAccent)
        .apply()// 上面步骤中布局文件中的文字颜色customColor1将会是白色，customColor1将会是R.color.colorAccent的颜色
Oops.bulk {
    attrColorSet(context, R.attr.customColor1, Color.WHITE)
}// 上面步骤中布局文件中的文字颜色customColor1将会是白色
```
#### -> 自定义View
-	``View``实现``OopsLifecycleOwner``接口
``` kotlin
// kotlin
class OopsTextView : AppCompatTextView, OopsLifecycleOwner
```
在大部分情况下，你只需将下面代码复制到你的``View``中（ java 版本大同小异 ）
``` kotlin
// kotlin
private val lifecycleRegistry = LifecycleRegistry(this)
override fun getLifecycle(): Lifecycle = lifecycleRegistry
override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    attachOopsLife()
}
override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
    super.onWindowFocusChanged(hasWindowFocus)
    handleOopsLifeStartOrStop(hasWindowFocus)
}
override fun onDetachedFromWindow() {
    detachOopsLife()
    super.onDetachedFromWindow()
}
```
需个人实现的部分，复写方法``liveInOops``，例如``OopsTextView ``
``` kotlin
// kotlin
override fun liveInOops() {
    Oops.living(this.activity()).live(textColorAttrValue)?.observe(this, Observer(this::setTextColor))
}
```
完整自定义View示例可查看：[Oops->Widget](https://github.com/iota9star/oops-android-kt/tree/master/oops/src/main/kotlin/io/nichijou/oops/widget "Oops自定义View")
- 实现``OopsLayoutInflaterFactory``接口，详细自定义过程查看：[CustomTextView](https://github.com/iota9star/oops-android-kt/blob/master/simple/src/main/kotlin/io/nichijou/oops/simple/CustomView.kt "CustomTextView")，[MyFactory](https://github.com/iota9star/oops-android-kt/blob/master/simple/src/main/kotlin/io/nichijou/oops/simple/MyFactory.kt "MyFactory")，[BaseActivity](https://github.com/iota9star/oops-android-kt/blob/master/simple/src/main/kotlin/io/nichijou/oops/simple/BaseActivity.kt "BaseActivity")
### 致谢
- [@afollestad](https://github.com/afollestad "afollestad")
- [@chibatching](https://github.com/chibatching "chibatching")
- [@wuyr](https://github.com/wuyr "wuyr")

----
### Licenses
```
   Copyright 2018 iota9star

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```