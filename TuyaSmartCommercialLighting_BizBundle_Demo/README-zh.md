## 准备工作

前往 [涂鸦 IoT 平台](https://iot.tuya.com/) 注册开发者账号。

## 创建 SDK 应用

1. 在 [涂鸦 IoT 平台](https://iot.tuya.com/oem/sdkList) 中 **App 工作台** 中点击 **App**边栏下的**SDK 开发**，点击**创建 App**。

	![img](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/16291806887945bf17686.png)

2. 选择App SDK类型：

	![选择App SDK类型.png](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/16243534800b5e94483e6.png)

3. 填写 App 相关信息，点击**确认**。

	- **应用名称**：填写您的 App 名称。
	- **iOS 应用包名**：填写您的 iOS App 包名（建议格式：com.xxxxx.xxxxx)。
	- **安卓应用包名**：填写您的安卓 App 包名（两者可以保持一致，也可以不一致）。
	- **渠道标识符**：不是必填项，如果不填写，系统会根据包名自动生成。


	您可以根据实际需求选择需要的选择方案，支持多选，然后根据 Podfile 和 Gradle 进行 SDK 的集成。

4. 点击**获取密钥**，获取 SDK 的 AppKey，AppSecret，安全图片等信息。

	![image.png](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/1629354107c0dedbc5e3a.png)

	:::important
	
	1. IoT 平台 **创建App** 成功后，请将创建的应用的包名提供到我司商务，并由商务转交相关信息给云端进行客户项目的创建等相关配套操作后，**SDK才可正常使用**。
	
	2. 从1.9.7版本开始，需要设置SHA256。关于如何获取SHA256密钥，可以参考文档[如何获取SHA256密钥](https://developer.tuya.com/cn/docs/app-development/iot_app_sdk_core_sha1?id=Kao7c7b139vrh)。
	
	:::

## 集成SDK

- 开始操作前，请确保您已经完成了上文步骤。
- 如果您还未安装 Android Studio，请访问 [安卓官网](https://developer.android.com/studio) 进行下载安装。

### 第一步：创建 Android 工程

在 Android Studio 中新建工程。

### 第二步：配置 build.gradle 文件

在安卓项目的 `build.gradle` 文件里，添加集成准备中下载的 `dependencies` 依赖库。

```groovy
android {
    defaultConfig {
        ndk {
            abiFilters "armeabi-v7a", "arm64-v8a"
        }
    }
    packagingOptions {
        pickFirst 'lib/*/libc++_shared.so' // 多个aar存在此so，需要选择第一个
    }
}
dependencies {
    implementation 'com.facebook.soloader:soloader:0.8.0'
    implementation 'com.alibaba:fastjson:1.1.67.android'
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'com.squareup.okhttp3:okhttp-urlconnection:3.12.3'
    implementation "androidx.annotation:annotation:1.0.0"

    implementation "com.tuya.smart:tuyasmart-tuyacommerciallightingsdk:1.9.6"
}
```

在根目录的 `build.gradle` 文件，中增加涂鸦 IoT Maven 仓库地址，进行仓库配置。

```groovy
repositories {
    maven {url "https://maven-other.tuya.com/repository/maven-releases/"}
}
```

<a id="bmp&keySetting"></a>

### 第三步：集成安全图片和设置 Appkey 和 AppSecret

1. 在 [App 工作台](https://iot.tuya.com/oem/sdkList)，找到您创建的 SDK。
2. 在 **获取密钥** 中，点击 **下载安全图片** > **安全图片下载** 下载安全图片。

	![image.png](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/1620789752da515dde378.png)

3. 将下载的安全图片命名为 `t_s.bmp`，放置到工程目录的 `assets` 文件夹下。

	<img src="https://images.tuyacn.com/fe-static/docs/img/2de282a3-5498-479e-bb31-3688e3ac1eb2.png" alt="涂鸦技术文档示意图" width="250"  style="border:1px solid black"/>

4. 返回安卓项目，在 `AndroidManifest.xml` 文件里配置 appkey 和 appSecret，在配置相应的权限等。

	```xml
	<meta-data
	android:name="TUYA_SMART_APPKEY"
	android:value="应用 Appkey" />
	<meta-data
	android:name="TUYA_SMART_SECRET"
	android:value="应用密钥 AppSecret" />
	```

### 第四步：混淆配置

在 `proguard-rules.pro` 文件配置相应混淆配置。

```bash
#fastJson
-keep class com.alibaba.fastjson.**{*;}
-dontwarn com.alibaba.fastjson.**

#mqtt
-keep class com.tuya.smart.mqttclient.mqttv3.** { *; }
-dontwarn com.tuya.smart.mqttclient.mqttv3.**

#OkHttp3
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

-keep class okio.** { *; }
-dontwarn okio.**

-keep class com.tuya.**{*;}
-dontwarn com.tuya.**
```

### 第五步：初始化 SDK

您需要在 `Application` 的主线程中初始化 SDK，确保所有进程都能初始化。示例代码如下：

```java
public class TuyaSmartApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TuyaSdk.init(this);
    }
}
```

:::info
`appKey` 和 `appSecret` 可以配置在 `AndroidManifest.xml` 文件里，也可以在初始化代码里初始化。
```java
TuyaSdk.init(Application application, String appkey, String appSerect)
```
:::

### 第六步：开启或关闭日志

* 在 debug 模式下，您可以开启 SDK 的日志开关，查看更多的日志信息，帮助您快速定位问题。
* 在 release 模式下，建议关闭日志开关。

	```java
	TuyaSdk.setDebugMode(true);
	```

## 运行 Demo App

:::important
在完成快速集成 SDK 后，您将获取到 SDK 使用的 `AppKey`、 `AppSecret`、安全图片信息。集成 SDK 时请确认 `AppKey`、`AppSecret`、安全图片是否与平台上的信息一致，任意一个不匹配会导致 SDK 无法使用。详细操作，请参考 [第三步：集成安全图片和设置 Appkey 和 AppSecret](#bmp&keySetting)。
:::

Demo App 演示了 App SDK 的开发流程。在开发 App 之前，建议您先按照以下流程完成 Demo App 的操作。

### Demo App 介绍

Demo App 主要包括了：
- 项目管理功能
- 区域管理功能
- 群组管理功能
- 账号管理功能
- 设备入网功能
- 设备控制功能
- 户外项目功能

**Demo示意图**：

<p> <img src="https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/16243554383b21d81e279.jpg" width = "150" style="vertical-align:top; display:inline;">	<img src="https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/1624355571470c1caa8cd.jpg" width = "150" style="vertical-align:top; display:inline;">  <img src="https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/1624355595647d9161359.jpg" width = "150" style="vertical-align:top; display:inline;"></p>

<!-- 更多详情，请参考 []() -->

### 运行 Demo

1. 替换 `app` 目录下 `build.gradle` 文件中的 `applicationId` 为您的应用包名。
![6769cfd5-ac6f-4a19-b5b2-0174b0ae7acb.png](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/1624355273d62a193f8b9.png)


2. 确认您已经完成了 [第三步：集成安全图片和设置 Appkey 和 AppSecret](#bmp&keySetting)。

3. 然后点击运行，运行 Demo。

### 故障排查：API 接口请求提示签名错误

* **问题现象**：运行 Demo 时提示以下错误：

	```json
	{
		"success": false,
		"errorCode" : "SING_VALIDATE_FALED",
		"status" : "error",
		"errorMsg" : "Permission Verification Failed",
		"t" : 1583208740059
	}
	```

* **解决方法**：

	* 请检查您的 AppKey 、AppSecret 和 安全图片是否正确配置。
	* 安全图片是否放在正确目录，文件名是否为 `t_s.bmp`。