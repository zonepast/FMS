package com.prudent.fms.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.content.Context
import android.graphics.Color
import android.view.ViewGroup
import com.prudent.fms.R
import com.prudent.fms.data.api.model.dashboard.Response.Table2
import com.prudent.fms.kotlin_extensions.colorRes
import com.prudent.fms.kotlin_extensions.inflateLayout
import kotlinx.android.synthetic.main.layout_dashboard_recyclerview_report3.view.*
/**
 * Created by Dharmik Patel on 26-Jul-17.
 */
class DashboardReport3Adapter(val responseList: List<Table2>, val context: Context) : RecyclerView.Adapter<DashboardReport3Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DashboardReport3Adapter.ViewHolder {
        val view = context.inflateLayout(R.layout.layout_dashboard_recyclerview_report3, parent!!, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int =  responseList.size

    override fun onBindViewHolder(holder: DashboardReport3Adapter.ViewHolder?, position: Int) {

        val data = responseList[position]
        holder!!.onBind(data,context)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txt_dashboard_statement = itemView.txt_dashboard_statement!!
        val progressBarDashboardStatement = itemView.progress_bar_dashboard_statement!!

        fun onBind(data : Table2, context: Context){

            val color = data.color
            val ActType = data.actype
            val percentage = data.performance

            txt_dashboard_statement.text = ActType
            progressBarDashboardStatement.progress = percentage!!.toInt().toFloat()

            setColor(color!!,context)
        }

        private fun setColor(color: String,context: Context) {
            when(color) {
                "Green" ->
                    progressBarDashboardStatement.progressColor = context.colorRes(R.color.success)
                "Red" ->
                    progressBarDashboardStatement.progressColor = context.colorRes(R.color.danger)
                else ->
                    progressBarDashboardStatement.progressColor = context.colorRes(R.color.black)
            }
        }

        private fun cardViewOnClick(string: String) {
           // val mBundle = Bundle()
            /* mBundle.putString(Constant.REPORTNAME, string)
             context.startActivity(context.intent<ReportDetailActivity>(mBundle))*/
        }
    }

}