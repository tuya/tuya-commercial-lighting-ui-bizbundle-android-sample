## 涂鸦商照 UI 业务包
[English](./README.md)  | [中文文档](./README-zh.md) 

### 关于涂鸦商照

涂鸦商用照明解决方案是一套完整的物联网控制系统，适用于新装和存量的商用照明市场，为其提供从设备端到软件控制端以及施工端的一套完整方案服务。通过设备管理、能源管控、人因照明等实现绿色建筑与健康建筑。

涂鸦商用照明解决方案为客户后期的产品维护与运营提供数字化、可视化的管理平台，帮助客户实现商业智能化，降低管理成本。

涂鸦商用照明解决方案的六大核心优势：

- 低成本的即开即用方案
- 丰富的物联能力
- 快速增长的强大生态系统
- 可持续的增值服务
- 数字化全球项目运营管理
- 金融级网络数据安全

### 关于涂鸦商照SDK

Tuya Smart Commercial Lighting SDK是一套针对商用照明领域开发的Android端解决方案，Android开发者可以基于SDK快速实现商用照明及关联场景的App功能开发，实现对项目、空间以及设备的管理和控制等操作。

> 注意：涂鸦商照SDK从1.9.7版本开始，做了安全校验的升级。您需要在[IoT平台根据说明文档](https://developer.tuya.com/cn/docs/app-development/iot_app_sdk_core_sha1?id=Kao7c7b139vrh)来获取SHA256，然后在IoT平台绑定您的SHA256，否则会报错非法客户端。如果您需要本地dubug运行Sample，您需要在app模块的build.gradle下，android闭包中配置您的签名信息：
```groovy
signingConfigs {
        debug {
            storeFile file('../xxx.jks')
            storePassword 'xxx'
            keyAlias 'xxx'
            keyPassword 'xxx'
        }
    }
```

### 关于涂鸦商照 UI 业务包
涂鸦 Android 业务包是指包含业务逻辑和 UI 界面的涂鸦垂直业务模块，旨在为基于涂鸦商业照明 SDK 开发的应用提供快速的一站式接入涂鸦业务模块的能力。

目前提供的业务包，例如：

- 设备配网
- 设备控制

涂鸦商照 UI 业务包文档：[https://developer.tuya.com/cn/docs/app-development/android-saas-lighting-bizbundle?id=Kb5r1ydy6a0qn](https://developer.tuya.com/cn/docs/app-development/android-saas-lighting-bizbundle?id=Kb5r1ydy6a0qn)

### 技术支持

- Tuya IoT 开发者平台: https://developer.tuya.com/en/
- Tuya 开发者帮助中心: https://support.tuya.com/en/help
- Tuya 工单系统: https://service.console.tuya.com/