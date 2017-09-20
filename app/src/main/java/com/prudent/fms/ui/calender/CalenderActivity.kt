package com.prudent.fms.ui.calender

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.prudent.fms.R
import com.prudent.fms.core.CoreApp
import com.prudent.fms.data.api.model.calender.Request.CalenderRequest
import com.prudent.fms.data.api.model.calender.Response.CalenderResponse
import com.prudent.fms.extensions.ConvertDateFormat
import com.prudent.fms.extensions.GetCorpCode
import com.prudent.fms.extensions.GetUserCode
import com.prudent.fms.kotlin_extensions.alert
import com.prudent.fms.kotlin_extensions.colorRes
import com.prudent.fms.kotlin_extensions.e
import com.prudent.fms.kotlin_extensions.progressDialog
import com.prudent.fms.ui.calender.AddEvent.AddEventFragment

import kotlinx.android.synthetic.main.activity_calender.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import kotlinx.android.synthetic.main.activity_calender.*
import kotlinx.android.synthetic.main.content_calender.*
import com.prudent.fms.utils.Calender.OnDateClickListener
import com.prudent.fms.utils.Calender.OnWeekDayViewClickListener
import com.prudent.fms.utils.Calender.OnEventClickListener
import java.util.*


class CalenderActivity : AppCompatActivity() {

    var mProgressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setProgress()

        Calender()

    }

    private fun Calender() {
        progressShow()
        val calender = CoreApp.instance?.getNetworkManager()?.service?.Calender(CalenderRequest(
                GetCorpCode()!!,"2017","", GetUserCode()!!))
        calender?.enqueue(object : Callback<CalenderResponse> {
            override fun onResponse(call: Call<CalenderResponse>?, response: Response<CalenderResponse>?) {
                progressHide()
                if (response!!.isSuccessful){
                    val list = response.body()
                    ShowCalenderResponse(list)
                }else{
                    Error("Error", "Something went wrong")
                }
            }

            override fun onFailure(call: Call<CalenderResponse>?, t: Throwable?) {
                progressHide()
                if (t is java.net.ConnectException) {
                    Error("Internet Error", "Please, Check your Internet Connection!")
                } else if (t is SocketTimeoutException) {
                    Error("Timeout Error", "Please, Try again!")
                } else {
                    Error("Internet Error", "Please, Check your Internet Connection!")
                }
            }

        })
    }

    private fun setProgress() {
        mProgressDialog = progressDialog("Please Wait", "Loading")
        /* mProgressDialog = ProgressDialog(this)
         mProgressDialog!!.setMessage("Please Wait")
         mProgressDialog!!.setTitle("Loading")
         mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)*/
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun progressShow() {
        mProgressDialog?.show()
    }

    fun progressHide() {
        mProgressDialog?.dismiss()
    }

    fun Error(title: String, message: String) {
        val dialog = alert(message, title)
        dialog.negativeButton("OK") {

        }
        dialog.positiveButton("RETRY") {
            Calender()
        }
        dialog.show()
    }

    fun ShowCalenderResponse(response: CalenderResponse) {
           if (response.table!!.isNotEmpty()){
               calendar_event.showMonthView();
               calendar_event.setHeaderBackgroundColor(colorRes(R.color.colorPrimary));
               calendar_event.setHeaderTextColor(Color.WHITE);
               calendar_event.setNextPreviousIndicatorColor(Color.WHITE)
               calendar_event.setDatesOfMonthTextColor(Color.BLACK);
               calendar_event.setEventCellTextColor(Color.BLACK);
               /*calendar_event.setWeekDayLayoutBackgroundColor("#965471");
               calendar_event.setWeekDayLayoutTextColor("#246245");*/

               for (i in 0..response.table!!.size - 1) {

                   val id = response.table!![i].id
                   val name = response.table!![i].name
                   val StartTime = response.table!![i].startTime
                   val EndTime = response.table!![i].endTime
                   val date = response.table!![i].fromDate
                   val color = response.table!![i].color

                   val Date2 = date!!.replace("T".toRegex(), " ")
                   val finalDate = ConvertDateFormat(Date2,"yyyy-MM-dd hh:mm:ss","dd-MM-yyyy")

                   val colorList = color!!.split(", ".toRegex()).dropLastWhile({ it.isEmpty() }).toTypedArray()
                   val r = Integer.parseInt(colorList[0])
                   val g = Integer.parseInt(colorList[1])
                   val b = Integer.parseInt(colorList[2])

                   calendar_event.setEventCellBackgroundColor(getHexColor(r, g, b))

                   calendar_event.addEvent(finalDate,StartTime,EndTime,name,getHexColor(r, g, b))

               }
           }else{

           }
    }

    fun getHexColor(r: Int,g: Int,b: Int): String {
        return String.format("#%02x%02x%02x", r, g, b)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_calender, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            R.id.action_month -> {
                showMonthView()
                return true
            }
            R.id.action_month_with_below_events -> {
                showMonthViewWithBelowEvents()
                return true
            }
            R.id.action_week -> {
                showWeekView()
                return true
            }
            R.id.action_day -> {
                showDayView()
                return true
            }
            R.id.action_agenda -> {
                showAgendaView()
                return true
            }
            R.id.action_today -> {
                calendar_event.goToCurrentDate()
                return true
            }
            R.id.action_add_event -> {
                val fm = fragmentManager
                val dialogFragment = AddEventFragment()
                dialogFragment.retainInstance = true
                dialogFragment.show(fm, "Add Event Fragment");
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    private fun showMonthView() {

        calendar_event.showMonthView()

        /*myCalendar.setOnDateClickListener(object : OnDateClickListener {
            fun onClick(date: Date) {
                Log.e("date", String.valueOf(date))
            }

            fun onLongClick(date: Date) {
                Log.e("date", String.valueOf(date))
            }
        })*/

    }

    private fun showMonthViewWithBelowEvents() {

        calendar_event.showMonthViewWithBelowEvents()

        /*myCalendar.setOnDateClickListener(object : OnDateClickListener {
            fun onClick(date: Date) {
                Log.e("date", String.valueOf(date))
            }

            fun onLongClick(date: Date) {
                Log.e("date", String.valueOf(date))
            }
        })*/

    }

    private fun showWeekView() {

        calendar_event.showWeekView()

        /*myCalendar.setOnEventClickListener(object : OnEventClickListener {
            override fun onClick() {
                Log.e("showWeekView", "from setOnEventClickListener onClick")
            }

            override fun onLongClick() {
                Log.e("showWeekView", "from setOnEventClickListener onLongClick")

            }
        })

        myCalendar.setOnWeekDayViewClickListener(object : OnWeekDayViewClickListener {
            override fun onClick(date: String, time: String) {
                Log.e("showWeekView", "from setOnWeekDayViewClickListener onClick")
                Log.e("tag", "date:-$date time:-$time")

            }

            override fun onLongClick(date: String, time: String) {
                Log.e("showWeekView", "from setOnWeekDayViewClickListener onLongClick")
                Log.e("tag", "date:-$date time:-$time")

            }
        })*/


    }

    private fun showDayView() {

        calendar_event.showDayView()

        /*myCalendar.setOnEventClickListener(object : OnEventClickListener {
            override fun onClick() {
                Log.e("showDayView", "from setOnEventClickListener onClick")

            }

            override fun onLongClick() {
                Log.e("showDayView", "from setOnEventClickListener onLongClick")

            }
        })

        myCalendar.setOnWeekDayViewClickListener(object : OnWeekDayViewClickListener {
            override fun onClick(date: String, time: String) {
                Log.e("showDayView", "from setOnWeekDayViewClickListener onClick")
                Log.e("tag", "date:-$date time:-$time")
            }

            override fun onLongClick(date: String, time: String) {
                Log.e("showDayView", "from setOnWeekDayViewClickListener onLongClick")
                Log.e("tag", "date:-$date time:-$time")
            }
        })*/

    }

    private fun showAgendaView() {

        calendar_event.showAgendaView()

        /*calendar_event.setOnDateClickListener(object : OnDateClickListener {
            fun onClick(date: Date) {
                Log.e("date", String.valueOf(date))
            }

            fun onLongClick(date: Date) {
                Log.e("date", String.valueOf(date))
            }
        })*/

    }
}
