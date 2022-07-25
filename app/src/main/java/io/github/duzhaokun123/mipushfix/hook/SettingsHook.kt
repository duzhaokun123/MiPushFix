package io.github.duzhaokun123.mipushfix.hook

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import io.github.duzhaokun123.mipushfix.BuildConfig
import com.github.kyuubiran.ezxhelper.utils.*
import de.robv.android.xposed.callbacks.XC_LoadPackage
import io.github.duzhaokun123.mipushfix.utils.currentApplication
import java.lang.reflect.Proxy

object SettingsHook : BaseHook() {
    override fun init(lpparam: XC_LoadPackage.LoadPackageParam) {
        findMethod(
            "top.trumeet.mipushframework.settings.SettingsFragment", lpparam.classLoader,
            findSuper = true) {
            name == "onCreatePreferences"
        }.hookAfter {
            val context = it.thisObject.invokeMethod("requireContext") as Context
            val root = it.thisObject.invokeMethod("getPreferenceScreen")!!
            val category =
                lpparam.classLoader.loadClass("moe.shizuku.preference.PreferenceCategory")
                    .newInstance(args(context), argTypes(Context::class.java))!!
            category.invokeMethodAuto("setTitle", "MiPushFix")
            val preference =
                lpparam.classLoader.loadClass("moe.shizuku.preference.Preference")
                    .newInstance(args(context), argTypes(Context::class.java))!!
            preference.invokeMethodAuto("setTitle", "About")
            val Preference_OnPreferenceClickListener =
                lpparam.classLoader.loadClass(
                    "moe.shizuku.preference.Preference\$OnPreferenceClickListener")
            preference.invokeMethod(
                "setOnPreferenceClickListener",
                args(Proxy.newProxyInstance(
                    lpparam.classLoader, arrayOf(Preference_OnPreferenceClickListener))
                { _, _, _ -> about(context); true }),
                argTypes(Preference_OnPreferenceClickListener))
            root.invokeMethodAuto("addPreference", category)
            category.invokeMethodAuto("addPreference", preference)
        }
    }

    private fun about(context: Context) {
        val pm = context.packageManager
        val pi = pm.getPackageInfo(currentApplication.packageName, 0)
        AlertDialog.Builder(context)
            .setTitle("About")
            .setMessage("""
                MiPushFix: ${BuildConfig.VERSION_NAME}(${BuildConfig.VERSION_CODE})
                xmsf: ${pi.versionName}(${pi.versionCode})
                SDK: ${Build.VERSION.RELEASE}(${Build.VERSION.SDK_INT}); Phone: ${Build.BRAND} ${Build.MODEL}
            """.trimIndent())
            .setPositiveButton("官方 repo") { _, _ ->
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/duzhaokun123/MiPushFix")))
            }.show()
    }
}