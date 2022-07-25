package io.github.duzhaokun123.mipushfix.hook

import android.app.Application
import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import com.github.kyuubiran.ezxhelper.utils.findMethod
import com.github.kyuubiran.ezxhelper.utils.hookAfter
import de.robv.android.xposed.callbacks.XC_LoadPackage

object ApplicationHook: BaseHook() {
    override fun init(lpparam: XC_LoadPackage.LoadPackageParam) {
        findMethod(Application::class.java) {
            name == "onCreate"
        }.hookAfter {
            EzXHelperInit.initAppContext()
        }
    }
}