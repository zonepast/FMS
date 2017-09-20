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
import com.prudent.fms.adapter.NavigationDrawer1
import com.prudent.fms.adapter.NavigationDrawerAdapter
import com.prudent.fms.extensions.GetCorpName
import com.prudent.fms.extensions.GetDBNavigation1
import com.prudent.fms.extensions.GetUserName
import com.prudent.fms.kotlin_extensions.drawableRes
import com.prudent.fms.utils.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_navigation_drawer.*

class NavigationDrawerFragment : Fragment(), NavigationDrawer1 {

    lateinit var drawer : DrawerLayout

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view =  inflater!!.inflate(R.layout.fragment_navigation_drawer, container, false)

        val username = view.findViewById(R.id.txt_nav_username) as TextView
        val corpname = view.findViewById(R.id.txt_nav_corpname) as TextView
        val recyclerView_navigation_drawer = view.findViewById(R.id.recyclerView_navigation_drawer) as RecyclerView
        username.text = GetUserName()
        corpname.text = GetCorpName()

        SetNavigationRecyclerView(recyclerView_navigation_drawer)

        return view
    }

    private fun SetNavigationRecyclerView(recyclerView: RecyclerView){
        val gridLayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(activity.drawableRes(R.drawable.dr_divider),true,false))

        val adapter = NavigationDrawerAdapter(GetDBNavigation1(),activity,this,drawer)
        recyclerView.adapter = adapter
    }

    override fun Navigation1(string: String) {
        val detail = NavigationDrawer1Fragment()
        detail.SetDrawer(drawer)
        val args = Bundle()
        args.putString("key", string)
        detail.arguments = args
        val t = fragmentManager.beginTransaction()
        t.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
        t.replace(R.id.frame_layout_nav_dashboard, detail, "name")
        t.addToBackStack("name")
        t.commit()
    }

    public fun SetDrawer(drawerLayout: DrawerLayout){
        this.drawer = drawerLayout
    }

}
