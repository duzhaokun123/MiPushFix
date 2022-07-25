package io.github.duzhaokun123.mipushfix.hook

import de.robv.android.xposed.callbacks.XC_LoadPackage

abstract class BaseHook {
    var isInit: Boolean = false
    abstract fun init(lpparam: XC_LoadPackage.LoadPackageParam)
}