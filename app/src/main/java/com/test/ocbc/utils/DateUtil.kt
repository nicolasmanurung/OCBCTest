package com.test.ocbc.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {
    //2022-03-12T15:13:58.927Z
    fun String.changeTo(formatOutputDate: String): String =
        with(SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sss'Z'", Locale.getDefault())) {
            parse(this@changeTo)?.let { formatDate(it, formatOutputDate) }.toString()
        }

    private fun formatDate(date: Date, format: String): String =
        with(SimpleDateFormat(format, Locale.getDefault())) {
            format(date)
        }
}