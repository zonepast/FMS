package com.prudent.fms.ui.dashboardComponent

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSnapHelper
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.github.mikephil.charting.data.*
import com.google.gson.JsonElement
import com.prudent.fms.R
import com.prudent.fms.adapter.DashboardReport1Adapter
import com.prudent.fms.adapter.DashboardReport2Adapter
import com.prudent.fms.adapter.DashboardReport3Adapter
import com.prudent.fms.adapter.TableAdapterInterface
import com.prudent.fms.core.CoreApp
import com.prudent.fms.data.api.model.dashboard.Response.DashboardDataResponse
import com.prudent.fms.data.api.model.reportTableView.Request.ReportTableViewRequest
import com.prudent.fms.events.EventDashboardResponse
import com.prudent.fms.events.EventTableViewJson
import com.prudent.fms.events.graph.EventBarData
import com.prudent.fms.events.graph.EventLineData
import com.prudent.fms.events.graph.EventRadarData
import com.prudent.fms.extensions.GetCorpCode
import com.prudent.fms.extensions.GetUserCode
import com.prudent.fms.extensions.ShowDatePicker
import com.prudent.fms.extensions.dateNow
import com.prudent.fms.kotlin_extensions.*
import com.prudent.fms.ui.graph.barchart.BarChartFragment
import com.prudent.fms.ui.graph.linechart.LineChartFragment
import com.prudent.fms.ui.graph.radarchart.RadarFragment
import com.prudent.fms.ui.tableview.TableViewActivity
import com.prudent.fms.utils.Config
import com.prudent.fms.utils.DividerItemDecoration
import com.prudent.fms.utils.PaddingItemDecoration
import com.prudent.fms.utils.RecyclerViewDashboard
import kotlinx.android.synthetic.main.fragment_recyclerview_dashbard.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.util.*

class RecyclerviewDashbardFragment : Fragment(), TableAdapterInterface {

    lateinit var recyclerView: RecyclerViewDashboard
    lateinit var recyclerView2: RecyclerView
    lateinit var recyclerView3: RecyclerView
    lateinit var txt_statement: TextView
    lateinit var txt_report_date: TextView
    var mProgressDialog: ProgressDialog? = null
    lateinit var data: DashboardDataResponse

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_recyclerview_dashbard, container, false)

        setProgress()

        recyclerView = view.findViewById(R.id.recyclerView_dashboard) as RecyclerViewDashboard
        recyclerView2 = view.findViewById(R.id.recyclerView_dashboard2) as RecyclerView
        recyclerView3 = view.findViewById(R.id.recyclerView_dashboard3) as RecyclerView
        txt_statement = view.findViewById(R.id.txt_dashboard_statement_title) as TextView
        txt_report_date = view.findViewById(R.id.txt_dashboard_report2_title) as TextView


        SetRecyclerView(recyclerView)
        SetRecyclerView2(recyclerView2)
        SetRecyclerView3(recyclerView3)

        txt_report_date.text = dateNow("dd-MM-yyyy")

        ClickListener()

        return view
    }

    private fun setProgress() {
        mProgressDialog = activity.progressDialog("Please Wait", "Loading")
    }

    fun progressShow() {
        mProgressDialog?.show()
    }

    fun progressHide() {
        mProgressDialog?.dismiss()
    }

    fun Error(title: String, message: String) {
        val dialog = activity.alert(message, title)
        dialog.negativeButton("CANCEL") {

        }
        dialog.positiveButton("OK") {

        }
        dialog.show()
    }

    private fun ClickListener() {
        txt_statement.setOnClickListener { expandableLayout_statement.toggle() }
        txt_report_date.setOnClickListener { ShowDatePicker(txt_report_date, activity) }
    }

    private fun SetRecyclerView(recyclerView: RecyclerViewDashboard) {
        LinearSnapHelper().attachToRecyclerView(recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager
        val spacingInPixels = dimenRes(R.dimen.recyclerView_dashboard_1).toDp(activity)
        recyclerView.addItemDecoration(PaddingItemDecoration(activity, spacingInPixels, spacingInPixels))
    }

    private fun SetRecyclerView2(recyclerView: RecyclerView) {
        LinearSnapHelper().attachToRecyclerView(recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager
        val spacingInPixels = dimenRes(R.dimen.recyclerView_dashboard_2).toDp(activity)
        recyclerView.addItemDecoration(PaddingItemDecoration(activity, spacingInPixels, spacingInPixels))
    }

    private fun SetRecyclerView3(recyclerView: RecyclerView) {
        recyclerView.setHasFixedSize(true)
        recyclerView.isNestedScrollingEnabled = false
        val linearLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(activity.drawableRes(R.drawable.dr_divider), false, false))
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun OnTransactionEvent(response1: EventDashboardResponse) {
        data = response1.response

        val adapter = DashboardReport1Adapter(data.table3!!, activity, this)
        recyclerView.adapter = adapter

        val adapter2 = DashboardReport2Adapter(data.table4!!, activity, this)
        recyclerView2.adapter = adapter2

        val adapter3 = DashboardReport3Adapter(data.table2!!, activity)
        recyclerView3.adapter = adapter3
        txt_statement.text = data.table!![0].c

    }

    override fun DetailWithTableView(bundle: Bundle) {
        DetailTableView(bundle.getString(Config().KEY_REPORTNAME), bundle.getInt(Config().KEY_REPORTTYPE))
    }

    fun DetailTableView(reportName: String, type: Int) {
        progressShow()
        if (type == 1) {
            val report = CoreApp.instance?.getNetworkManager()?.service?.
                    GetReportTableViewDetail(ReportTableViewRequest(GetCorpCode(),
                            reportName, "DashBoard", GetUserCode()))
            report?.enqueue(object : Callback<JsonElement> {
                override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                    progressHide()
                    if (response!!.isSuccessful) {
                        val list = response.body()
                        startTableView(list)
                    } else {
                        Error("Error", "Something went wrong")
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
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
        if (type == 2) {
            val report = CoreApp.instance?.getNetworkManager()?.service?.
                    GetReportTableViewDetail(ReportTableViewRequest(GetCorpCode(),
                            txt_report_date.text as String?, reportName, GetUserCode()))
            report?.enqueue(object : Callback<JsonElement> {
                override fun onResponse(call: Call<JsonElement>?, response: Response<JsonElement>?) {
                    progressHide()
                    if (response!!.isSuccessful) {
                        val list = response.body()
                        startTableView(list)
                    } else {
                        Error("Error", "Something went wrong")
                    }
                }

                override fun onFailure(call: Call<JsonElement>?, t: Throwable?) {
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
    }

    private fun startTableView(jsonElement: JsonElement) {
        EventBus.getDefault().postSticky(EventTableViewJson(jsonElement))
        activity.start<TableViewActivity>()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()
    }
}