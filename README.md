
# Oops
[![jcenter](https://api.bintray.com/packages/iota9star/nichijou/oops/images/download.svg)](https://bintray.com/iota9star/nichijou/oops/_latestVersion) [![Build Status](https://travis-ci.org/iota9star/oops-android-kt.svg?branch=master)](https://travis-ci.org/iota9star/oops-android-kt) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/5d36b3333d02491499c8c0b6d1765d42)](https://app.codacy.com/app/iota9star/oops-android-kt?utm_source=github.com&utm_medium=referral&utm_content=iota9star/oops-android-kt&utm_campaign=Badge_Grade_Dashboard)[![License](https://img.shields.io/hexpm/l/plug.svg)](https://www.apache.org/licenses/LICENSE-2.0.html) [![API](https://img.shields.io/badge/API-16%2B-green.svg?style=flat)](https://android-arsenal.com/api?level=16)
### Oops - Android Material Theme engine, Powered by LiveData & ViewModel.
一个基于**LiveData** & **ViewModel** 的Android **Material** 主题引擎，由**Kotlin**实现。本主题引擎通过简单的方式实现各个**View**的色彩变幻，整个过程除了当您更改了主题时才会触发重启**Activity**外，其余过程均无需重启**Activity**。

下载示例应用：[simple-release.apk](https://github.com/iota9star/oops-android-kt/raw/master/simple/release/simple-release.apk "simple-release.apk")

![demo](https://github.com/iota9star/oops-android-kt/raw/master/simple/release/demo.png "demo")

----
### 基础使用
#### -> 在应用中使用
- 在你应用的``module``的``build.gradle``中添加
``` gradle
dependencies {
  // 其他
  implementation 'io.nichijou:oops:0.4.1'
}
```
- Activit直接继承OopsActivity，或在activity的`onCreate`方法中调用`Oops.attach(this)`，在`setContentView`之前调用
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
#### -> 忽略别修改
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
或在代码中动态设置tag的值为``oops_ignore_view``，如果你需要给``tag``设置其他的值，你也可以使用setTag方法设置特殊的值达到同样的效果
``` kotlin
view.setTag(R.string.oops_ignore_view,"any value but not null")// 第一个参数你必须设置为R.string.oops_ignore_view，后面的值你可以随意设置一个不为null的值
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
Oops.immed().customAttrColorSet(context, R.attr.customColor1, Color.WHITE);// 上面步骤中布局文件中的文字颜色将会更改为白色
Oops.bulk()
        .customAttrColorSet(context, R.attr.customColor1, Color.WHITE)
        .customAttrColorResSet(context, R.attr.customColor2, R.color.colorAccent)
        .apply();// 上面步骤中布局文件中的文字颜色customColor1将会是白色，customColor1将会是R.color.colorAccent的颜色
```
``` kotlin
Oops.immed().customAttrColorSet(context, R.attr.customColor1, Color.WHITE)// 上面步骤中布局文件中的文字颜色将会更改为白色
Oops.bulk()
        .customAttrColorSet(context, R.attr.customColor1, Color.WHITE)
        .customAttrColorResSet(context, R.attr.customColor2, R.color.colorAccent)
        .apply()// 上面步骤中布局文件中的文字颜色customColor1将会是白色，customColor1将会是R.color.colorAccent的颜色
Oops.bulk {
    customAttrColorSet(context, R.attr.customColor1, Color.WHITE)
}// 上面步骤中布局文件中的文字颜色customColor1将会是白色
```
#### -> 自定义View
-	``View``实现``OopsViewLifeAndLive``接口
``` kotlin
// kotlin
class OopsTextView : AppCompatTextView, OopsViewLifeAndLive
```
在大部分情况下，你只需将下面代码复制到你的``View``中（ java 版本大同小异 ）
``` kotlin
// kotlin
override fun getOopsViewModel(): OopsViewModel = oopsVM
private val oopsVM by lazy {
    ViewModelProviders.of(this.activity()).get(OopsViewModel::class.java)
}
private val oopsLife: LifecycleRegistry by lazy {
    LifecycleRegistry(this)
}
override fun getLifecycle(): Lifecycle = oopsLife
override fun onAttachedToWindow() {
    super.onAttachedToWindow()
    startOopsLife()
}
override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
    super.onWindowFocusChanged(hasWindowFocus)
    resumeOrPauseLife(hasWindowFocus)
}
override fun onDetachedFromWindow() {
    endOopsLife()
    super.onDetachedFromWindow()
}
```
需个人实现的部分，复写方法``howToLive``，例如``OopsTextView ``
``` kotlin
// kotlin
override fun howToLive() {
    oopsVM.live(textColorAttrName)?.observe(this, Observer(this::setTextColor))
}
```
完整示例可查看：[Oops->Widget](https://github.com/iota9star/oops-android-kt/tree/master/oops/src/main/kotlin/io/nichijou/oops/widget "Oops自定义View")
- 实现``OopsLayoutInflaterFactory``接口，详细查看：[CustomTextView](https://github.com/iota9star/oops-android-kt/blob/master/simple/src/main/kotlin/io/nichijou/oops/simple/CustomView.kt "CustomTextView")，[MyFactory](https://github.com/iota9star/oops-android-kt/blob/master/simple/src/main/kotlin/io/nichijou/oops/simple/MyFactory.kt "MyFactory")，[BaseActivity](https://github.com/iota9star/oops-android-kt/blob/master/simple/src/main/kotlin/io/nichijou/oops/simple/BaseActivity.kt "BaseActivity")

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