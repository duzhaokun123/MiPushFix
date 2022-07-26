package io.github.duzhaokun123.mipushfix

import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.github.kyuubiran.ezxhelper.utils.parasitics.TransferActivity

class MessageLogActivity : TransferActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(ScrollView(this).apply {
            addView(LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                addView(Button(context).apply {
                    text = "delete"
                    setOnClickListener {
                        messageLog.delete()
                        messageLog.createNewFile()
                        finish()
                    }
                }, WRAP_CONTENT, WRAP_CONTENT)
                addView(TextView(context).apply {
                    text = messageLog.readText()
                    setTextIsSelectable(true)
                }, MATCH_PARENT, WRAP_CONTENT)
            }, MATCH_PARENT, WRAP_CONTENT)
        })
    }
}