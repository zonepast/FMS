package com.prudent.fms.ui.dashboard

import android.Manifest
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.design.widget.AppBarLayout
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.PopupMenu
import android.transition.Explode
import android.transition.Fade
import android.transition.TransitionInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.TextView
import com.github.mikephil.charting.data.*
import com.prudent.fms.R
import com.prudent.fms.adapter.BranchArrayAdapter
import com.prudent.fms.core.CoreApp
import com.prudent.fms.data.api.model.Logout.Response.LogoutResponse
import com.prudent.fms.data.api.model.dashboard.Response.DashboardDataResponse
import com.prudent.fms.events.EventDashboardResponse
import com.prudent.fms.events.graph.EventBarData
import com.prudent.fms.events.graph.EventLineData
import com.prudent.fms.events.graph.EventRadarData
import com.prudent.fms.extensions.*
import com.prudent.fms.kotlin_extensions.*
import com.prudent.fms.ui.calender.CalenderActivity
import com.prudent.fms.ui.dashboardComponent.RecyclerviewDashbardFragment
import com.prudent.fms.ui.graph.barchart.BarChartFragment
import com.prudent.fms.ui.graph.linechart.LineChartFragment
import com.prudent.fms.ui.graph.radarchart.RadarFragment
import com.prudent.fms.ui.navigationbar.NavigationDrawerFragment
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.content_dashboard.*
import org.greenrobot.eventbus.EventBus
import java.util.ArrayList

class DashboardActivity : AppCompatActivity(), DashboardView {

    lateinit var dashboardPresenter: DashboardPresenter
    var mProgressDialog: ProgressDialog? = null
    lateinit var data: DashboardDataResponse
    var mxValues: MutableList<String> = mutableListOf<String>()
    var doubleBackToExitPressedOnce: Boolean? = false
    var permissions = arrayOf(Manifest.permission.INTERNET, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar_dashboard)

        initPresenter()
        onAttach()

        titleCollapseToolbar()

        checkPermissions()

        setProgress()

        branchSpinner()
        initNavigationDrawer()

        Dashboard()

        btn_dashboard_graph_type.setOnClickListener { SetGraphPopUpMenu(btn_dashboard_graph_type) }
    }


    private fun initNavigationDrawer(){
        val drawer = findViewById(R.id.drawer_layout_dashboard) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar_dashboard, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.setDrawerListener(toggle)
        toggle.syncState()

        val detail = NavigationDrawerFragment()
        detail.SetDrawer(drawer)
        val t = supportFragmentManager.beginTransaction()
        t.replace(R.id.frame_layout_nav_dashboard, detail)
        t.commit()
    }

    private fun branchSpinner() {
        val branchAdapter = BranchArrayAdapter(this, GetRealmData())
        main_dashboard_collapse_spinner.adapter = branchAdapter
        main_dashboard_collapse_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapter: AdapterView<*>, v: View,
                                        position: Int, id: Long) {

                val txt = v.findViewById(R.id.txt_spinner_title) as TextView
                val BranchXname = txt.text.toString()
                val BranchXcode = txt.tag.toString()
                CoreApp.instance?.getSessionManager()?.SaveBranch(BranchXname, BranchXcode)
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {


            }
        }
    }

    private fun titleCollapseToolbar() {
        app_bar_dashboard.addOnOffsetChangedListener(object : AppBarLayout.OnOffsetChangedListener {
            internal var isShow = false
            internal var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    main_collapsing_dashboard.title = "Dashboard"
                    isShow = true
                } else if (isShow) {
                    main_collapsing_dashboard.title = " "//carefull there should a space between double quote otherwise it wont work
                    isShow = false
                }
            }
        })
    }

    private fun Dashboard() {
        dashboardPresenter.Dashboard(GetCorpCode()!!, GetUserCode()!!)
    }

    private fun initPresenter() {
        dashboardPresenter = DashboardPresenter()
    }

    private fun setProgress() {
        mProgressDialog = progressDialog("Please Wait", "Loading")
        /* mProgressDialog = ProgressDialog(this)
         mProgressDialog!!.setMessage("Please Wait")
         mProgressDialog!!.setTitle("Loading")
         mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)*/
    }

    override fun onDestroy() {
        onDetach()
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
        if (doubleBackToExitPressedOnce!!) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP//***Change Here***
            startActivity(intent)
            finish()
            System.exit(0)
            return
        }
        this.doubleBackToExitPressedOnce = true
        toast("Please click BACK again to exit")
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_dashboard, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.menu_calender ->  start<CalenderActivity>()
            R.id.menu_logout -> AlertLogout()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onAttach() {
        dashboardPresenter.onAttach(this)
    }

    override fun onDetach() {
        dashboardPresenter.onDetach()
    }

    override fun progressShow() {
       // mProgressDialog?.show()
        dashboard_progressBar.visibility = View.VISIBLE
        layout_dashboard.visibility = View.GONE
        layout_dashboard_graph.visibility = View.GONE
    }

    override fun progressHide() {
        //mProgressDialog?.dismiss()
        dashboard_progressBar.visibility = View.GONE
        layout_dashboard.visibility = View.VISIBLE
        layout_dashboard_graph.visibility = View.VISIBLE
    }

    override fun Error(title: String, message: String) {
        val dialog = alert(message, title)
        dialog.positiveButton("RETRY") {
            Dashboard()
        }
        dialog.show()
    }

    override fun ShowDashboardResponse(response: DashboardDataResponse) {

        data = response

        EventBus.getDefault().postSticky(EventDashboardResponse(response))

        val t = supportFragmentManager.beginTransaction()
        t.replace(R.id.frame_layout_dashboard_component, RecyclerviewDashbardFragment())
        t.commit()

        // Graph
        txt_dashboard_graph_title.text = "${data.table!![0].a} ${data.table!![0].b}"
        for (i in 0..data.table1!!.size - 1) {
            mxValues.add(data.table1!![i].x!!)
        }

        SetLineChart()

    }

    // Graph
    private fun SetGraphPopUpMenu(view: View) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.menu_chart, popup.menu)
        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
            val id = item.itemId
            when (id) {
                R.id.action_line_chart -> {
                    SetLineChart()
                    return@OnMenuItemClickListener true
                }
                R.id.action_bar_chart -> {
                    SetBarChart()
                    return@OnMenuItemClickListener true
                }
                R.id.action_radar_chart -> {
                    SetRadarChart()
                    return@OnMenuItemClickListener true
                }
                R.id.action_scatter_chart -> {

                    return@OnMenuItemClickListener true
                }
            }
            true
        })
        popup.show()
    }

    private fun SetLineChart() {
        val dataSets = ArrayList<LineDataSet>()
        val values = ArrayList<Entry>()
        val values1 = ArrayList<Entry>()
        val values2 = ArrayList<Entry>()
        for (i in 0..data.table1!!.size - 1) {
            val y = data.table1!![i].y
            val z = data.table1!![i].z
            val a = data.table1!![i].a
            values.add(Entry(y!!.toFloat(), i))
            values1.add(Entry(z!!.toFloat(), i))
            values2.add(Entry(a!!.toFloat(), i))
        }
        val d = LineDataSet(values, data.table!![0].d)
        val e = LineDataSet(values1, data.table!![0].f)
        val f = LineDataSet(values2, data.table!![0].f)

        d.color = Color.parseColor("#ffff0000")
        d.fillColor = Color.parseColor("#ffff0000")
        d.circleSize = 1f
        d.setCircleColor(Color.parseColor("#ffff0000"))
        d.lineWidth = 1f
        d.setDrawValues(false)
        d.setDrawCircleHole(false)
        d.valueTextSize = 8f
        d.setDrawFilled(true)
        d.setDrawCubic(true)

        e.color = Color.parseColor("#00ddff")
        e.fillColor = Color.parseColor("#00ddff")
        e.setCircleColor(Color.parseColor("#00ddff"))
        e.lineWidth = 1f
        e.circleSize = 1f
        e.setDrawValues(false)
        e.setDrawCircleHole(false)
        e.valueTextSize = 8f
        e.setDrawFilled(true)
        e.setDrawCubic(true)

        f.color = Color.parseColor("#1aa14c")
        f.fillColor = Color.parseColor("#1aa14c")
        f.circleSize = 1f
        f.setCircleColor(Color.parseColor("#1aa14c"))
        f.lineWidth = 1f
        f.setDrawValues(false)
        f.setDrawCircleHole(false)
        f.valueTextSize = 8f
        f.setDrawFilled(true)
        f.setDrawCubic(true)

        dataSets.add(d)
        dataSets.add(e)
        dataSets.add(f)
        val data = LineData(mxValues, dataSets)

        EventBus.getDefault().postSticky(EventLineData(data))

        val t = supportFragmentManager.beginTransaction()
        t.replace(R.id.frame_layout_dashboard_graph, LineChartFragment())
     //   t.addToBackStack(null);
       // t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        t.commit()
    }

    private fun SetBarChart() {

        val barDataSets = ArrayList<BarDataSet>()
        val values = ArrayList<BarEntry>()
        val values1 = ArrayList<BarEntry>()
        val values2 = ArrayList<BarEntry>()
        for (i in 0..data.table1!!.size - 1) {
            val y = data.table1!![i].y
            val z = data.table1!![i].z
            val a = data.table1!![i].a
            values.add(BarEntry(y!!.toFloat(), i))
            values1.add(BarEntry(z!!.toFloat(), i))
            values2.add(BarEntry(a!!.toFloat(), i))
        }

        val d = BarDataSet(values, data.table!![0].d)
        val e = BarDataSet(values1, data.table!![0].e)
        val f = BarDataSet(values2, data.table!![0].f)

        // set the line to be drawn like this "- - - - - -"
        d.color = Color.parseColor("#ffff0000")
        d.setDrawValues(false)
        d.valueTextSize = 8f

        e.color = Color.parseColor("#00ddff")
        e.setDrawValues(false)
        e.valueTextSize = 8f

        f.color = Color.parseColor("#1aa14c")
        f.setDrawValues(false)
        f.valueTextSize = 8f

        barDataSets.add(d)
        barDataSets.add(e)
        barDataSets.add(f)

        val data = BarData(mxValues, barDataSets)

        EventBus.getDefault().postSticky(EventBarData(data))

        val t = supportFragmentManager.beginTransaction()
        t.replace(R.id.frame_layout_dashboard_graph, BarChartFragment())
       // t.addToBackStack(null);
      //  t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        t.commit()
    }

    private fun SetRadarChart() {

        val values = ArrayList<Entry>()
        val values1 = ArrayList<Entry>()
        val values2 = ArrayList<Entry>()
        for (i in 0..data.table1!!.size - 1) {
            val y = data.table1!![i].y
            val z = data.table1!![i].z
            val a = data.table1!![i].a
            values.add(Entry(y!!.toFloat(), i))
            values1.add(Entry(z!!.toFloat(), i))
            values2.add(Entry(a!!.toFloat(), i))
        }

        val d = RadarDataSet(values, data.table!![0].d)
        val e = RadarDataSet(values1, data.table!![0].e)
        val f = RadarDataSet(values2, data.table!![0].f)

        // set the line to be drawn like this "- - - - - -"
        d.color = Color.parseColor("#ffff0000")
        d.setDrawValues(false)
        d.setDrawFilled(true)
        d.valueTextSize = 8f
        d.fillColor = Color.BLACK

        e.color = Color.parseColor("#00ddff")
        e.setDrawValues(false)
        e.valueTextSize = 8f
        e.setDrawFilled(true)
        e.fillColor = Color.BLACK

        f.color = Color.parseColor("#1aa14c")
        f.setDrawValues(false)
        f.setDrawFilled(true)
        f.valueTextSize = 8f
        f.fillColor = Color.BLACK

        val sets = ArrayList<RadarDataSet>()
        sets.add(d)
        sets.add(e)
        sets.add(f)

        val data = RadarData(mxValues, sets)

        EventBus.getDefault().postSticky(EventRadarData(data))

        val t = supportFragmentManager.beginTransaction()
        t.replace(R.id.frame_layout_dashboard_graph, RadarFragment())
       // t.addToBackStack(null);
      //  t.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        t.commit()
    }

    private fun AlertLogout(){
        val dialog = alert("Are you sure you want to logout?", "Logout")
        dialog.negativeButton("NO") {

        }
        dialog.positiveButton("YES") {
            dashboardPresenter.Logout("Logout", GetCorpCode(), "",
                    "Login", "", GetUserCode(), "")
        }
        dialog.show()
    }

    override fun ShowLogoutResponse(response: LogoutResponse) {
        if (response.table!![0].success == 1) {
            toast(response.table!![0].message!!)
            DeletedbLogin()
            DeleteDBNavigation()
            CoreApp.instance?.getSessionManager()!!.logoutUser()
        } else {
            toast(response.table!![0].message!!)
        }
    }

    private fun checkPermissions(): Boolean {
        var result: Int
        val listPermissionsNeeded = ArrayList<String>()
        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(this, p)
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p)
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toTypedArray(), 100)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // do something
            }
            return
        }
    }
}
