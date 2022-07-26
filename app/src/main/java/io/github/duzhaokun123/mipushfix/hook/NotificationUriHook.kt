package io.github.duzhaokun123.mipushfix.hook

import com.github.kyuubiran.ezxhelper.utils.*
import de.robv.android.xposed.callbacks.XC_LoadPackage
import io.github.duzhaokun123.mipushfix.messageLog
import io.github.duzhaokun123.mipushfix.prefs

object NotificationUriHook : BaseHook() {
    override fun init(lpparam: XC_LoadPackage.LoadPackageParam) {
        findMethod("com.xiaomi.push.service.MyMIPushNotificationHelper", lpparam.classLoader) {
            name == "notifyPushMessage"
        }.hookBefore {
            val buildContainer = it.args[1]
            messageLog.appendText("$buildContainer\n")
            val metaInfo = buildContainer.invokeMethod("getMetaInfo") ?: return@hookBefore
            val extra = metaInfo.invokeMethodAs<MutableMap<String, String>>("getExtra") ?: return@hookBefore
            val intentUri = extra["intent_uri"] ?: return@hookBefore
            if (extra["web_uri"] != null) return@hookBefore
            extra["web_uri"] = intentUri
            if (prefs.getBoolean("debug_add_intent_uri", false)) {
                metaInfo.invokeMethodAuto("setDescription", "${metaInfo.invokeMethod("getDescription")}\n$intentUri")
            }
        }
    }
}