package com.prudent.fms.adapter

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.prudent.fms.R
import com.prudent.fms.data.db.model.dbNavigationDrawer
import com.prudent.fms.kotlin_extensions.e
import com.prudent.fms.kotlin_extensions.inflateLayout
import com.prudent.fms.kotlin_extensions.lollipopOrNewer
import com.prudent.fms.ui.main.MainActivity
import com.prudent.fms.utils.Config
import kotlinx.android.synthetic.main.layout_navigation.view.*

/**
 * Created by Dharmik Patel on 25-Jul-17.
 */

class NavigationDrawerAdapter(private val responseList: List<dbNavigationDrawer>, private val context: Context,private val navigationDrawer1: NavigationDrawer1,private val drawerLayout: DrawerLayout) : RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): NavigationDrawerAdapter.ViewHolder {
        val view = context.inflateLayout(R.layout.layout_navigation, parent!!, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = responseList.size

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: NavigationDrawerAdapter.ViewHolder?, position: Int) {

        val data = responseList[position]
        holder!!.onBind(data)

        holder.txt_nav_item.setOnClickListener {
            if (data.count == 0) {
                drawerLayout.closeDrawers()
                drawerLayout.closeDrawers()
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra(Config().KEY_CATEGORYNAME, data.category)
                if (lollipopOrNewer()) {
                    context.startActivity(intent,
                            ActivityOptions.makeSceneTransitionAnimation(context as Activity?).toBundle())
                } else {
                    context.startActivity(intent)
                }
            } else {
                cardViewOnClick(data)
            }
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

            txt_nav_item.text = category!!

            if (count == 0) {
                txt_nav_item.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)
            } else {
                txt_nav_item.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_navigate_next_grey, 0)
            }

        }
    }

    private fun cardViewOnClick(data: dbNavigationDrawer) {
        navigationDrawer1.Navigation1(data.category!!)
    }
}

interface NavigationDrawer1{
    fun Navigation1(string: String)
}