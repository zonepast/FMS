package com.prudent.fms.extensions

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.TextView
import java.util.*


/**
 * Created by Dharmik Patel on 26-Jul-17.
 */
public fun ShowDatePicker(textView: TextView, context: Context) {
    val mYear: Int
    val mMonth: Int
    val mDay: Int
    val c = Calendar.getInstance()
    mYear = c.get(Calendar.YEAR)
    mMonth = c.get(Calendar.MONTH)
    mDay = c.get(Calendar.DAY_OF_MONTH)

    // Launch Date Picker Dialog
    val dpd = DatePickerDialog(context,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val month = monthOfYear + 1
                var formattedMonth = "" + month
                var formattedDayOfMonth = "" + dayOfMonth

                if (month < 10) {

                    formattedMonth = "0" + month
                }
                if (dayOfMonth < 10) {

                    formattedDayOfMonth = "0" + dayOfMonth
                }
                textView.text = "$formattedDayOfMonth-$formattedMonth-$year"
            }, mYear, mMonth, mDay)
    dpd.show()
}

public fun ShowTimePicker(textView: TextView, context: Context) {
    val mHour: Int
    val mMinute: Int
    val c = Calendar.getInstance()
    mHour = c.get(Calendar.HOUR_OF_DAY)
    mMinute = c.get(Calendar.MINUTE)

    // Launch Date Picker Dialog
    val timePickerDialog = TimePickerDialog(context,
            TimePickerDialog.OnTimeSetListener {
                view, hourOfDay, minute -> textView.text = ConvertDateFormat(hourOfDay.toString() + ":" + minute,"hh:mm","hh:mm: aa")
            }, mHour, mMinute, true)
    timePickerDialog.show()
}