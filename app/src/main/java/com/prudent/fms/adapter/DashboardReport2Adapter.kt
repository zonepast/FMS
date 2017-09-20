package com.prudent.fms.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.prudent.fms.R
import com.prudent.fms.data.api.model.dashboard.Response.Table4
import com.prudent.fms.kotlin_extensions.colorRes
import com.prudent.fms.kotlin_extensions.inflateLayout
import com.prudent.fms.utils.Config
import kotlinx.android.synthetic.main.layout_dashboard_recyclerview_report2.view.*

/**
 * Created by Dharmik Patel on 26-Jul-17.
 */
class DashboardReport2Adapter(val responseList: List<Table4>, val context: Context, val tableAdapterInterface: TableAdapterInterface) : RecyclerView.Adapter<DashboardReport2Adapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DashboardReport2Adapter.ViewHolder {
        val view = context.inflateLayout(R.layout.layout_dashboard_recyclerview_report2, parent!!, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = responseList.size

    override fun onBindViewHolder(holder: DashboardReport2Adapter.ViewHolder?, position: Int) {

        val data = responseList[position]
        holder!!.onBind(data, context,tableAdapterInterface)

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtMainReport2 = itemView.txt_main_report2!!

        fun onBind(data: Table4, context: Context,tableAdapterInterface: TableAdapterInterface) {

            val color = data.class_
            val name = data.xname
            val xcode = data.xcode

            txtMainReport2.text = name

            setColor(color!!, context)

            txtMainReport2.setOnClickListener { ViewOnClick(xcode!!,tableAdapterInterface) }
        }

        private fun setColor(color: String, context: Context) {
            val drawable = txtMainReport2.background as GradientDrawable
            when (color) {
                "btn btn-success" ->
                    drawable.setColor(context.colorRes(R.color.success))
                "btn btn-primary" ->
                    drawable.setColor(context.colorRes(R.color.primary))
                "btn btn-info" ->
                    drawable.setColor(context.colorRes(R.color.info))
                "btn btn-warning" ->
                    drawable.setColor(context.colorRes(R.color.warning))
                "btn btn-danger" ->
                    drawable.setColor(context.colorRes(R.color.danger))
                else ->
                    drawable.setColor(Color.BLACK)
            }
        }

        private fun ViewOnClick(string: String,tableAdapterInterface: TableAdapterInterface) {
            val mBundle = Bundle()
            mBundle.putString(Config().KEY_REPORTNAME, string)
            mBundle.putInt(Config().KEY_REPORTTYPE, 2)
            tableAdapterInterface.DetailWithTableView(mBundle)
        }
    }

}