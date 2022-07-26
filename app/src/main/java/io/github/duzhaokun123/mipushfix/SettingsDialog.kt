package io.github.duzhaokun123.mipushfix

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.preference.PreferenceFragment
import com.github.kyuubiran.ezxhelper.init.EzXHelperInit

class SettingsDialog(context: Context): AlertDialog.Builder(context) {
    class PrefsFragment : PreferenceFragment() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            preferenceManager.sharedPreferencesName = "mipushfix"
            addPreferencesFromResource(R.xml.settings)
        }
    }

    init {
        val activity = context as Activity
        EzXHelperInit.addModuleAssetPath(context)
        val prefsFragment = PrefsFragment()
        activity.fragmentManager.beginTransaction().add(prefsFragment, "Setting").commit()
        activity.fragmentManager.executePendingTransactions()
        prefsFragment.onActivityCreated(null)
        setView(prefsFragment.view)
        setTitle("Settings")
        setPositiveButton(android.R.string.ok, null)
    }
}