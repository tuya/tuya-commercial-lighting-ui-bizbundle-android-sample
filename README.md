## Tuya Smart Commercial Lighting UI BizBundle

[English](./README.md) | [中文文档](./README-zh.md)

### What is Tuya Smart Commercial Lighting?

Tuya provides a broad portfolio of IoT control systems to bring existing and new smart commercial lighting solutions to market wherever and whenever you are ready. End-to-end services cover the full process from devices to software and construction. Green buildings and healthy buildings can deliver ongoing value with smart features, such as device management, energy management, and human centric lighting.

Tuya's smart commercial lighting solutions support digital and visualized platform-based operations and maintenance (O&M) for your smart products. This accelerates the smart development of smart commercial lighting projects and minimizes your management costs.

The following advantages are supported:

- Out-of-the-box solutions with best price performance
- Abundant IoT capabilities
- A robust and fast-growing ecosystem
- Sustainable value-added services
- Digital O&M of global projects
- Financial-level data security

### What is Tuya Smart Commercial Lighting App SDK?

Tuya Smart Commercial Lighting App SDK is dedicated to smart commercial lighting solutions for Android. You can integrate with this SDK to quickly develop app features respecting smart commercial lighting and scene linkage, and implement management and control of projects, spaces, and devices.

> **Note**: Starting Smart Commercial Lighting App SDK v1.9.7, a security checksum has been optimized. You must get an SHA256 hash value and bind it with your app on the Tuya IoT Development Platform. Otherwise, an illegal client error will occur. For more information, see [How to Get SHA1 and SHA256 Keys](https://developer.tuya.com/en/docs/app-development/iot_app_sdk_core_sha1?id=Kao7c7b139vrh). To enable local debugging, you must configure the signature by using the Android closure in `build.gradle` of the app module.

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

### What is Tuya Smart Commercial Lighting UI BizBundle?

Tuya UI BizBundle for Android is a vertical service bundle that encompasses logic modules and UI pages. It enables efficient access to comprehensive service modules provided by Tuya and accelerates your development based on Tuya Smart Commercial Lighting App SDK.

A bunch of BizBundles are supported, including:

- Device Pairing UI BizBundle
- Device Control UI BizBundle

For more information, see [https://developer.tuya.com/cn/docs/app-development/commercial-lighting-app-sdk-for-android?id=平台地址占位]()

### Technical support
- Tuya IoT Developer Platform: https://developer.tuya.com/en/
- Tuya Help Center: https://support.tuya.com/en/help
- Tuya Service & Support: https://service.console.tuya.com/
