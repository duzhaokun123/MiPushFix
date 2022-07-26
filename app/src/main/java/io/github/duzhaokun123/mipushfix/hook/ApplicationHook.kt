package io.github.duzhaokun123.mipushfix.hook

import android.app.Application
import android.content.Context
import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import de.robv.android.xposed.callbacks.XC_LoadPackage
import io.github.duzhaokun123.mipushfix.BuildConfig
import io.github.duzhaokun123.mipushfix.currentApplication
import io.github.duzhaokun123.mipushfix.prefs

object ApplicationHook: BaseHook() {
    override fun init(lpparam: XC_LoadPackage.LoadPackageParam) {
        findMethod(Application::class.java) {
            name == "onCreate"
        }.hookAfter {
            EzXHelperInit.initAppContext()
            EzXHelperInit.initActivityProxyManager(BuildConfig.APPLICATION_ID, "top.trumeet.mipushframework.permissions.ManagePermissionsActivity", lpparam.classLoader)
            EzXHelperInit.initSubActivity()
            prefs = currentApplication.getSharedPreferences("mipushfix", Context.MODE_MULTI_PROCESS)
        }
    }
}