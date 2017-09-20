package com.prudent.fms.adapter

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.prudent.fms.R
import com.prudent.fms.data.api.model.dashboard.Response.Table3
import com.prudent.fms.kotlin_extensions.colorRes
import com.prudent.fms.kotlin_extensions.inflateLayout
import com.prudent.fms.utils.Config
import kotlinx.android.synthetic.main.layout_dashboard_recyclerview_report1.view.*

/**
 * Created by Dharmik Patel on 18-Jul-17.
 */

class DashboardReport1Adapter(val responseList: List<Table3>, val context: Context, val tableAdapterInterface: TableAdapterInterface) : RecyclerView.Adapter<DashboardReport1Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DashboardReport1Adapter.ViewHolder {
        val view = context.inflateLayout(R.layout.layout_dashboard_recyclerview_report1, parent!!, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = responseList.size

    override fun onBindViewHolder(holder: DashboardReport1Adapter.ViewHolder?, position: Int) {

        val data = responseList[position]
        holder!!.onBind(data, context, tableAdapterInterface)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtDashboardReportTitle = itemView.txt_dashboard_recyclerView_report_title!!
        val txtDashboardReportNo = itemView.txt_dashboard_recyclerView_report_no!!
        val txtDashboardReportLabel = itemView.txt_dashboard_recyclerView_report_label!!
        val txtDashboardReportSubLabel = itemView.txt_dashboard_recyclerView_report_sublabel!!
        val cardViewGraph = itemView.cardView_dashboard_recyclerView_report!!

        fun onBind(data: Table3, context: Context, tableAdapterInterface: TableAdapterInterface) {

            val color = data.color
            val labelColor = data.pCColor
            val icon = data.status
            val ActType = data.actype
            val Amount = data.amount
            val Label = data.lable
            val percentage = data.pc

            txtDashboardReportTitle.text = ActType!!
            txtDashboardReportNo.text = Amount!!
            txtDashboardReportSubLabel.text = Label!!
            txtDashboardReportLabel.text = "%.2f".format(percentage!!) + "%"

            setReportNo(color!!, context)
            setReportLabel(labelColor!!, context)
            setReportLabelIcon(icon!!)

            cardViewGraph.setOnClickListener { cardViewOnClick(ActType, tableAdapterInterface) }

        }

        private fun setReportLabel(color: String, context: Context) {
            when (color) {
                "green" -> txtDashboardReportLabel.setTextColor(context.colorRes(R.color.success))
                "red" -> txtDashboardReportLabel.setTextColor(context.colorRes(R.color.danger))
                else -> txtDashboardReportLabel.setTextColor(Color.BLACK)
            }
        }

        private fun setReportLabelIcon(icon: String) {
            when (icon) {
                "fa fa-sort-asc" -> txtDashboardReportLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_up, 0, 0, 0)
                "fa fa-sort-desc" -> txtDashboardReportLabel.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_arrow_drop_down, 0, 0, 0)
            }
        }

        private fun setReportNo(color: String, context: Context) {
            when (color) {
                "green" -> txtDashboardReportNo.setTextColor(context.colorRes(R.color.success))
                "red" -> txtDashboardReportNo.setTextColor(context.colorRes(R.color.danger))
                else -> txtDashboardReportNo.setTextColor(Color.BLACK)
            }
        }

        private fun cardViewOnClick(string: String, tableAdapterInterface: TableAdapterInterface) {
            val mBundle = Bundle()
            mBundle.putString(Config().KEY_REPORTNAME, string)
            mBundle.putInt(Config().KEY_REPORTTYPE, 1)
            tableAdapterInterface.DetailWithTableView(mBundle)

            /*context.startActivity(context.intent<ReportDetailActivity>(mBundle))*/
        }
    }

}


