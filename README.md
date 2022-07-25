# MiPushFix

[![GitHub license](https://img.shields.io/github/license/duzhaokun123/MiPushFix?style=flat-square)](https://github.com/duzhaokun123/MiPushFix/blob/main/LICENSE)
![Android SDK min 24](https://img.shields.io/badge/Android%20SDK-%3E%3D%2024-brightgreen?style=flat-square&logo=android)
![Android SDK target 33](https://img.shields.io/badge/Android%20SDK-target%2033-brightgreen?style=flat-square&logo=android)
![Xposed Module](https://img.shields.io/badge/Xposed-Module-blue?style=flat-square)

这个模块应该解决了 [mipush](https://github.com/MiPushFramework/MiPushFramework) 点通知不跳转应用的问题

建议配合 [0.3.7.20210106.1425051](https://github.com/MiPushFramework/MiPushFramework/releases/tag/0.3.7.20210106.1425051) 使用 因为只有这个做了测试

解决方法很暴力

```kotlin
findMethod("com.xiaomi.push.service.MyMIPushNotificationHelper", lpparam.classLoader) { 
    name == "getClickedPendingIntent"
}.hookBefore {
    val paramPushMetaInfo = it.args[2] ?: return@hookBefore
    val extra = paramPushMetaInfo.invokeMethodAs<MutableMap<String, String>>("getExtra") ?: return@hookBefore
    if (extra["web_uri"] != null) return@hookBefore
    extra["web_uri"] = extra["intent_uri"] ?: return@hookBefore
}
```

## 鸣谢
- [EzXHelper](https://github.com/KyuubiRan/EzXHelper)
- Xposed