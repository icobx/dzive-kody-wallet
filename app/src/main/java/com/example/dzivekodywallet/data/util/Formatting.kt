package com.example.dzivekodywallet.data.util

import java.text.SimpleDateFormat

import java.util.*

object Formatting {
    fun formatDateTime(ts: String): String {
        var dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val date = dateFormat.parse(ts)
        dateFormat = SimpleDateFormat("EEEE, dd-MMM-yyyy HH:mm:ss", Locale.getDefault())

        return dateFormat.format(date!!)
    }

    fun formatDateTimePayment(ts: String): String {
        var dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val date = dateFormat.parse(ts)
        dateFormat = SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.getDefault())

        return dateFormat.format(date!!)
    }

    fun resolveAssetName(an: String): String {
        return if (an != "") an else "of unknown asset type"
    }
}