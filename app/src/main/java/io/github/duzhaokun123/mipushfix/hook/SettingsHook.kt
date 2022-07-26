package io.github.duzhaokun123.mipushfix.hook

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import com.github.kyuubiran.ezxhelper.utils.*
import de.robv.android.xposed.callbacks.XC_LoadPackage
import io.github.duzhaokun123.mipushfix.*
import java.lang.reflect.Proxy

object SettingsHook : BaseHook() {
    private lateinit var Preference_OnPreferenceClickListener: Class<*>
    private lateinit var category: Any

    override fun init(lpparam: XC_LoadPackage.LoadPackageParam) {
        findMethod(
            "top.trumeet.mipushframework.settings.SettingsFragment", lpparam.classLoader,
            findSuper = true) {
            name == "onCreatePreferences"
        }.hookAfter {
            val context = it.thisObject.invokeMethod("requireContext") as Context
            val root = it.thisObject.invokeMethod("getPreferenceScreen")!!
            category =
                lpparam.classLoader.loadClass("moe.shizuku.preference.PreferenceCategory")
                    .newInstance(args(context), argTypes(Context::class.java))!!
            category.invokeMethodAuto("setTitle", "MiPushFix")
            Preference_OnPreferenceClickListener =
                lpparam.classLoader.loadClass(
                    "moe.shizuku.preference.Preference\$OnPreferenceClickListener")
            root.invokeMethodAuto("addPreference", category)
            addPreference(context, lpparam, "Settings", ::settings)
            addPreference(context, lpparam, "MessageLog", ::log)
            addPreference(context, lpparam, "About", ::about)
        }
    }

    private fun addPreference(context: Context, lpparam: XC_LoadPackage.LoadPackageParam, title: String, onClick: (Context) -> Unit) {
        val preference =
            lpparam.classLoader.loadClass("moe.shizuku.preference.Preference")
                .newInstance(args(context), argTypes(Context::class.java))!!
        preference.invokeMethodAuto("setTitle", title)
        preference.invokeMethod(
                "setOnPreferenceClickListener",
        args(Proxy.newProxyInstance(
            lpparam.classLoader, arrayOf(Preference_OnPreferenceClickListener))
        { _, _, _ -> onClick(context); true }),
        argTypes(Preference_OnPreferenceClickListener))
        category.invokeMethodAuto("addPreference", preference)
    }

    private fun settings(context: Context) {
        SettingsDialog(context).show()
    }
    private fun log(context: Context) {
        context.startActivity(Intent(context, MessageLogActivity::class.java))
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
                ${prefs.all}
            """.trimIndent())
            .setPositiveButton("官方 repo") { _, _ ->
                context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/duzhaokun123/MiPushFix")))
            }.show()
    }
}