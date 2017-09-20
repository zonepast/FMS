package com.prudent.fms.ui.calender.AddEvent

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.prudent.fms.R
import com.prudent.fms.extensions.ShowDatePicker
import com.prudent.fms.extensions.ShowTimePicker
import com.prudent.fms.kotlin_extensions.toast
import com.rtugeek.android.colorseekbar.ColorSeekBar



/**
 * Created by Dharmik Patel on 17-Aug-17.
 */
class AddEventFragment : DialogFragment() {

    lateinit var txt_date: TextView
    lateinit var txt_start_time: TextView
    lateinit var txt_end_time: TextView
    lateinit var edt_event_name: TextView
    lateinit var color: ColorSeekBar

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity.layoutInflater.inflate(R.layout.layout_add_event, null)

        txt_date = view.findViewById(R.id.add_event_txt_date) as TextView
        txt_start_time = view.findViewById(R.id.add_event_txt_start_time) as TextView
        txt_end_time = view.findViewById(R.id.add_event_txt_end_time) as TextView
        edt_event_name = view.findViewById(R.id.add_event_edt_name) as EditText
        color = view.findViewById(R.id.add_event_colorSlider) as ColorSeekBar

        SetupListeners()

        return AlertDialog.Builder(activity)
                .setTitle("Add Event")
                .setView(view)
                .setNegativeButton("CANCEL") { dialog, which ->

                }
                .setPositiveButton("OK") { dialog, which ->

                }
                .create()
                .apply {
                    setCanceledOnTouchOutside(false)
                }
    }

    private fun SetupListeners(){
        txt_date.setOnClickListener {
            ShowDatePicker(txt_date,activity)
        }
        txt_start_time.setOnClickListener {
            ShowTimePicker(txt_start_time,activity)
        }
        txt_end_time.setOnClickListener {
            ShowTimePicker(txt_end_time,activity)
        }
        color.setOnColorChangeListener(ColorSeekBar.OnColorChangeListener { colorBarPosition, alphaBarPosition, color ->
            val strColor = String.format("#%06X", 0xFFFFFF and color)
            activity.toast(strColor)
        })
    }

}