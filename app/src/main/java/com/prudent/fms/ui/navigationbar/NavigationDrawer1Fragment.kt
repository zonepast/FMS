package com.prudent.fms.ui.navigationbar


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.DrawerLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.prudent.fms.R
import com.prudent.fms.adapter.NavigationDrawerAdapter1
import com.prudent.fms.extensions.GetDBNavigation2
import com.prudent.fms.kotlin_extensions.drawableRes
import com.prudent.fms.utils.DividerItemDecoration


/**
 * A simple [Fragment] subclass.
 */
class NavigationDrawer1Fragment : Fragment() {

    lateinit var drawer : DrawerLayout

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.fragment_navigation_drawer1, container, false)

        val value = arguments.getString("key")

        val corpname = view.findViewById(R.id.back_navigation) as TextView
        corpname.setOnClickListener {
            activity.supportFragmentManager.popBackStack()
        }
        val recyclerView_navigation_drawer = view.findViewById(R.id.recyclerView_navigation_drawer2) as RecyclerView

        SetNavigationRecyclerView(recyclerView_navigation_drawer, value)

        return view
    }

    private fun SetNavigationRecyclerView(recyclerView: RecyclerView, string: String) {
        val gridLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(activity.drawableRes(R.drawable.dr_divider),true,false))

        val adapter = NavigationDrawerAdapter1(GetDBNavigation2(string), activity,drawer)
        recyclerView.adapter = adapter
    }

    public fun SetDrawer(drawerLayout: DrawerLayout){
        this.drawer = drawerLayout
    }

}
