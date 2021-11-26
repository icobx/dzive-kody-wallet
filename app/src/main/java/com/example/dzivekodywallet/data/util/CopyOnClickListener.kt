package com.example.dzivekodywallet.data.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat

class CopyOnClickListener(
    private val context: Context,
    private val label: String,
    private val content: String,
) : View.OnClickListener {
    override fun onClick(view: View?) {
        val clipboard = ContextCompat.getSystemService(context, ClipboardManager::class.java)

        clipboard!!.setPrimaryClip(ClipData.newPlainText(label, content))
        Log.d("JFLOG", "IN ${javaClass.canonicalName}, copied text: ${clipboard.primaryClip.toString()}")

        val text = "$label copied to clipboard"
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}
