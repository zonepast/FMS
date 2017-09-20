package com.prudent.fms.extensions

import java.text.DateFormat
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    const val DF_SIMPLE_STRING = "yyyy-MM-dd HH:mm:ss"
    @JvmField val DF_SIMPLE_FORMAT = object : ThreadLocal<DateFormat>() {
        override fun initialValue(): DateFormat {
            return SimpleDateFormat(DF_SIMPLE_STRING, Locale.US)
        }
    }
}

fun dateNow(format: String): String = Date().asString(format)

fun dateNow(): String = Date().asString()

fun timestamp(): Long = System.currentTimeMillis()

fun dateParse(s: String): Date = DateHelper.DF_SIMPLE_FORMAT.get().parse(s, ParsePosition(0))

fun Date.asString(format: DateFormat): String = format.format(this)

fun Date.asString(format: String): String = asString(SimpleDateFormat(format))

fun Date.asString(): String = DateHelper.DF_SIMPLE_FORMAT.get().format(this)

fun Long.asDateString(): String = Date(this).asString()

fun ConvertDateFormat(originaldate: String, oldFormat: String, newFormat: String): String? {
    val inputFormat = SimpleDateFormat(oldFormat)
    val outputFormat = SimpleDateFormat(newFormat)

    var date: Date? = null
    var str: String? = null

    date = inputFormat.parse(originaldate)
    str = outputFormat.format(date)

    return str

}