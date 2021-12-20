
## Register Developer Account

Go to [Tuya Smart Development Platform](https://iot.tuya.com/) to register a developer account, create products, create dp points, etc. Please refer to [Access Process]((https://docs.tuya.com/zh/iot/device-intelligentize-in-5-minutes/device-intelligentize-in-5-minutes?id=K914joxbogkm6))

## Create SDK application

Click "**SDK Development**" under the "**App**" sidebar in the "**App Workbench**" in the [Tuya IoT Platform](https://iot.tuya.com/) and click "**Create App**".

![img](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/16245056441eb3b555213.png)

Select App SDK type.

![Select App SDK type.png](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/162450560288ea2df67a1.png)

Fill in related information of App and click **Confirm**.

- **App Name**: Fill in the name of your App.
- **iOS App Package Name**: Fill in the name of your iOS App package (suggested format: com.xxxxx.xxxxx).
- **Android App Package Name**: Fill in the name of your Android App package (can be consistent or inconsistent).
- **Channel Identifier**: Not required, if not filled in, the system will automatically generate according to the package name.

![Create App SDK](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/1624505727c4dfdbb2b08.png)

You can choose the required options according to your actual needs, support multiple choices, and then integrate the SDK according to Podfile and Gradle.

- Get the key
  Click **Get Key** to get the AppKey, AppSecret, security image and other information of the SDK.

  ![image.png](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/162935428707a87f44ab0.png)
  
  :::important
  
  	1. After ioT background **Create App** successfully, please provide the package name of the created application to our business developer, and he will forward the relevant information to the cloud department for the creation of customer projects and other related supporting operations before the **SDK can be used properly**!
   	2. From version 1.9.7 , you need to set SHA256 before you can use it. How to get SHA256 key, you can refer to the document [How to get SHA256 key](https://developer.tuya.com/en/docs/app-development/iot_app_sdk_core_sha1?id=Kao7c7b139vrh).

​		:::

> Note: After ioT background **Create App** successfully, please provide the package name of the created application to our business developer, and he will forward the relevant information to the cloud department for the creation of customer projects and other related supporting operations before the **SDK can be used properly**!

## SDK Integration 

- Before you start, please make sure you have completed the [Preparation](https://developer.tuya.com/cn/docs/app-development/preparation?id=Ka69nt983bhh5).
- If you haven't installed Android Studio yet, please visit [Android official website](https://developer.android.com/studio) to download and install it.

### Step 1: Create an Android project

Create a new project in Android Studio.

### Step 2: Configure build.gradle file

In the ``build.gradle`` file of the Android project, add the dependencies downloaded from the integration preparation in ``dependencies``.

```groovy
android {
    defaultConfig {
        ndk {
            abiFilters "armeabi-v7a", "arm64-v8a"
        }
    }
    packagingOptions {
        pickFirst 'lib/*/libc++_shared.so' // Multiple aar exist in this so, need to pick the first one
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

Add the Tuya Smart Maven repository address to the ``build.gradle`` file in the root directory for repository configuration.

```
repositories {
    maven {url "https://maven-other.tuya.com/repository/maven-releases/"}
}
```
### Step 3: Integrate security image and set Appkey and AppSecret

1. In the [App Workbench](https://iot.tuya.com/oem/sdkList), find the SDK you created.
2. In **Get Key**, click **Download Secure Image** > **Secure Image Download** to download the secure image.

    ![image.png](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/1624505812b1ecbeb724c.png)

3. Rename the downloaded security image as `t_s.bmp` and place it in the `assets` folder of the project directory.
    ![image.png](https://images.tuyacn.com/fe-static/docs/img/2de282a3-5498-479e-bb31-3688e3ac1eb2.png)

4. Return to the Android project, configure appkey and appSecret in the ``AndroidManifest.xml`` file, and configure the corresponding permissions, etc.

    ```xml
    <meta-data
    android:name="TUYA_SMART_APPKEY"
    android:value="app Appkey" />
    <meta-data
    android:name="TUYA_SMART_SECRET"
    android:value="AppKey AppSecret" />
    ```

### Step 4: Obfuscate the configuration

Configure the appropriate obfuscation configuration in the ``proguard-rules.pro`` file.

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

-keep class com.tuya.** { *; }
-dontwarn com.tuya.**
```

### Step 5: Initialize the SDK

You need to initialize the SDK in the main thread of `Application`, making sure that all processes are initialized. The sample code is as follows.

``` java
public class TuyaSmartApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        TuyaSdk.init(this);
    }
}
```
>**Note**: `appKey` and `appSecret` can be configured in `AndroidManifest.xml` file, or initialized in the initialization code.
>```java
>TuyaSdk.init(Application application, String appkey, String appSerect)
>```


### Step 6: Turn logging on or off

* In debug mode, you can turn on the SDK's switch of log to see more log information and help you locate problems quickly.
* In release mode, it is recommended to turn off the switch.

    ```java
    TuyaSdk.setDebugMode(true);
    ```

## Run Demo App

>**Note**: After completing the quick integration, you will get the `AppKey`, `AppSecret`, and security image information used by the SDK. When integrating the SDK, please make sure the `AppKey`, `AppSecret` and security image are the same as the information on the platform, any mismatch will make the SDK unusable. For details, please refer to [Step 3: Integrate secure image and set Appkey and AppSecret](#bmp&keySetting).

The Demo App demonstrates the development process of the App SDK. Before developing the App, we recommend you to follow the following procedure to complete the Demo App.

### Demo App Introduction

The Demo App mainly includes:
- Project management function
- Area management function
- Group management function
- Account management function
- Device access function
- Device Control Function
- Outdoor project function

**demo schematic**.


<p> <img src="https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/16245061997b8ad3f64cb.jpg" width = "150" / style='vertical-align:middle; display:inline;'>
 <img src="https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/1624506225a29de0d2f56.jpg" width = "150" / style='vertical-align:middle; display: inline;'> 
 <img src="https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/1624506251a201b149a12.jpg" width = "150" / style='vertical-align:middle; display:inline;'>
</p>


### Run the Demo

1. Replace `applicationId` in the `build.gradle` file in the `app` directory with the name of your app package.
   ![6769cfd5-ac6f-4a19-b5b2-0174b0ae7acb.png](https://airtake-public-data-1254153901.cos.ap-shanghai.myqcloud.com/content-platform/hestia/1624355273d62a193f8b9.png)
2. Make sure you have finished [Step 3: Integrate security image and set Appkey and AppSecret](#bmp&keySetting).
3. Then click Run to run the Demo.

### Troubleshooting: API interface request prompted with signature error

* **Problem Phenomenon**: The following error is prompted when running Demo.

    ```json
    {
        "success": false,
        "errorCode" : "SING_VALIDATE_FALED",
        "status" : "error",
        "errorMsg" : "Permission Verification Failed",
        "t" : 1583208740059
    }
    ```

* **Solution**.

    * Please check that your AppKey, AppSecret and security image are configured correctly and that they are consistent with [Preparation](https://developer.tuya.com/cn/docs/app-development/preparation/preparation?id= Ka69nt983bhh5).
    * The security image is placed in the correct directory and the file name is `t_s.bmp`.