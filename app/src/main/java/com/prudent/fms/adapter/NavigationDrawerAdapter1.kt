package com.prudent.fms.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.prudent.fms.R
import com.prudent.fms.data.db.model.dbNavigationDrawer
import com.prudent.fms.kotlin_extensions.inflateLayout
import com.prudent.fms.kotlin_extensions.intent
import com.prudent.fms.kotlin_extensions.lollipopOrNewer
import com.prudent.fms.ui.main.MainActivity
import com.prudent.fms.utils.Config
import kotlinx.android.synthetic.main.layout_navigation.view.*

/**
 * Created by Dharmik Patel on 25-Jul-17.
 */
class NavigationDrawerAdapter1(val responseList: List<dbNavigationDrawer>, val context: Context, private val drawerLayout: DrawerLayout) : RecyclerView.Adapter<NavigationDrawerAdapter1.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NavigationDrawerAdapter1.ViewHolder {
        val view = context.inflateLayout(R.layout.layout_navigation, parent!!, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = responseList.size

    override fun onBindViewHolder(holder: NavigationDrawerAdapter1.ViewHolder?, position: Int) {

        val data = responseList[position]
        holder!!.onBind(data)

        holder.txt_nav_item.setOnClickListener {
            cardViewOnClick(data)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val txt_nav_item = itemView.txt_nav_item!!

        fun onBind(data: dbNavigationDrawer) {

            val category = data.category
            val subCategory = data.subcategory
            val classname = data.classname
            val count = data.count
            val no = data.no

            txt_nav_item.text = subCategory!!

        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun cardViewOnClick(data: dbNavigationDrawer) {
        drawerLayout.closeDrawers()
        val intent = Intent(context,MainActivity::class.java)
        intent.putExtra(Config().KEY_CATEGORYNAME, data.subcategory)
        if (lollipopOrNewer()) {
            context.startActivity(intent,
                    ActivityOptions.makeSceneTransitionAnimation(context as Activity?).toBundle())
        } else {
            context.startActivity(intent)
        }
    }
}