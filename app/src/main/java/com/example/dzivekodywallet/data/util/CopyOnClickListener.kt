package com.example.dzivekodywallet.data.util

import android.content.ClipData
import android.content.ClipboardManager
import android.util.Log
import android.view.View

class CopyOnClickListener(
    private val clipboard: ClipboardManager,
    private val label: String,
    private val content: String,
) : View.OnClickListener {
    override fun onClick(view: View?) {
        clipboard.setPrimaryClip(ClipData.newPlainText(label, content))
        Log.d("JFLOG", "IN ${javaClass.canonicalName}, copied text: ${clipboard.primaryClip.toString()}")
    }
}