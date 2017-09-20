package com.prudent.fms.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.prudent.fms.R
import com.prudent.fms.data.db.model.dbsalesOrder
import com.prudent.fms.kotlin_extensions.inflateLayout
import io.realm.RealmResults
import kotlinx.android.synthetic.main.layout_sales_order_data.view.*

/**
 * Created by Dharmik Patel on 08-Aug-17.
 */
class SalesOrderAdapter(val responseList: MutableList<dbsalesOrder>, val context: Context, val salesOrderAdapterInterface: SalesOrderAdapterInterface) : RecyclerView.Adapter<SalesOrderAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): SalesOrderAdapter.ViewHolder {
        val view = context.inflateLayout(R.layout.layout_sales_order_data, parent!!, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = responseList.size

    override fun onBindViewHolder(holder: SalesOrderAdapter.ViewHolder?, position: Int) {

        val data = responseList[position]
        holder!!.onBind(data)

        holder.btn_delete.setOnClickListener {
            removeData(position)
            salesOrderAdapterInterface.DeleteSalesOrder(data.ItemCode!!)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txtItem = itemView.txt_sales_order_item_name!!
        val txtQty = itemView.txt_sales_order_qty!!
        val txtRate = itemView.txt_sales_order_rate!!
        val txtamount = itemView.txt_sales_order_amount!!
        val btn_delete = itemView.btn_sales_order_delete!!

        fun onBind(data: dbsalesOrder) {

            val itemname = data.ItemName
            val itemcode = data.ItemCode
            val rate = data.rate
            val qty = data.qty
            val Amount = data.amount

            txtItem.text = itemname!!
            txtamount.text = "${"Amount : "} ${Amount!!}"
            txtQty.text = "${"Qty : "} ${qty!!}"
            txtRate.text = "${"Rate : "} ${rate!!}"

        }

    }

    fun removeData(position: Int) {
        responseList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

}

interface SalesOrderAdapterInterface{
    fun DeleteSalesOrder(xcode: String)
}