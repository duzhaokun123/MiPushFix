package io.github.duzhaokun123.mipushfix

import android.app.AndroidAppHelper
import android.content.SharedPreferences
import java.io.File

val currentApplication get() = AndroidAppHelper.currentApplication()

val messageLog by lazy { File(currentApplication.externalCacheDir, "messageLog").apply { parentFile!!.mkdirs();if (exists().not()) createNewFile() } }

lateinit var prefs: SharedPreferences