package io.github.duzhaokun123.mipushfix.hook

import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookBefore
import com.github.kyuubiran.ezxhelper.utils.invokeMethodAs
import de.robv.android.xposed.callbacks.XC_LoadPackage

object NotificationUriHook : BaseHook() {
    override fun init(lpparam: XC_LoadPackage.LoadPackageParam) {
        findMethod("com.xiaomi.push.service.MyMIPushNotificationHelper", lpparam.classLoader) {
            name == "getClickedPendingIntent"
        }.hookBefore {
            val paramPushMetaInfo = it.args[2] ?: return@hookBefore
            val extra = paramPushMetaInfo.invokeMethodAs<MutableMap<String, String>>("getExtra") ?: return@hookBefore
            if (extra["web_uri"] != null) return@hookBefore
            extra["web_uri"] = extra["intent_uri"] ?: return@hookBefore
        }
    }
}