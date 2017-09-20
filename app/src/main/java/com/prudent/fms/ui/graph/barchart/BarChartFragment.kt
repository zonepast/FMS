package com.prudent.fms.ui.graph.barchart


import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData

import com.prudent.fms.R
import com.prudent.fms.events.graph.EventBarData
import com.prudent.fms.utils.Graph.MyMarkerView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * A simple [Fragment] subclass.
 */
class BarChartFragment : Fragment() {

    lateinit var barChart: BarChart

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_bar_chart, container, false)

        barChart = view.findViewById(R.id.chart_bar) as BarChart

        SetUpBarChart()

        return view
    }

    private fun SetUpBarChart() {
        val mv = MyMarkerView(activity, R.layout.layout_custom_markerview)
        barChart.markerView = mv

        barChart.setDescription("")
        barChart.setDrawBarShadow(false)
        barChart.isHighlightEnabled = true
        barChart.setTouchEnabled(true)
        barChart.isDragEnabled = true
        barChart.setScaleEnabled(true)
        barChart.setPinchZoom(false)
        barChart.setDrawGridBackground(false)

        val l = barChart.legend
        l.position = Legend.LegendPosition.BELOW_CHART_RIGHT

        val leftAxis = barChart.getAxisLeft()
        leftAxis.textColor = Color.parseColor("#757575")
        leftAxis.setStartAtZero(false)

        barChart.axisRight.isEnabled = false
        barChart.animateXY(1000, 1500)

        barChart.axisLeft.setDrawGridLines(false)
        barChart.xAxis.setDrawGridLines(false)
        val xLabels = barChart.xAxis
        xLabels.position = XAxis.XAxisPosition.BOTTOM
        xLabels.textColor = Color.parseColor("#757575")
    }

    private fun LoadChart(barData: BarData) {
        barChart.data = barData
        barChart.invalidate()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun OnTransactionEvent(barData: EventBarData) {
        LoadChart(barData.bardata)
    }

}
