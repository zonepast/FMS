package com.prudent.fms.ui.graph.radarchart

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.RadarData

import com.prudent.fms.R
import com.prudent.fms.events.graph.EventRadarData
import com.prudent.fms.utils.Graph.MyMarkerView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class RadarFragment : Fragment() {

    lateinit var radarChart: RadarChart

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_radar, container, false)
        radarChart = view.findViewById(R.id.chart_radar) as RadarChart

        SetUpLineChart()

        return view
    }

    private fun SetUpLineChart() {
        val mv = MyMarkerView(activity, R.layout.layout_custom_markerview)
        radarChart.markerView = mv

        radarChart.setDescription("")
        radarChart.webLineWidth = 1.5f
        radarChart.webLineWidthInner = 0.75f
        radarChart.webAlpha = 100

        val xAxis = radarChart.xAxis
        xAxis.textSize = 9f
        xAxis.yOffset = 0f
        xAxis.xOffset = 0f

        val yAxis = radarChart.yAxis
        yAxis.textSize = 9f
      //  yAxis.setLabelCount(mActivities.size, true)
        yAxis.setStartAtZero(false)
        yAxis.setDrawLabels(false)

        val l = radarChart.legend
        l.position = Legend.LegendPosition.RIGHT_OF_CHART
        l.xEntrySpace = 7f
        l.yEntrySpace = 5f
    }

    private fun LoadChart(radarData: RadarData) {
        radarChart.data = radarData
        radarChart.invalidate()
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
    fun OnTransactionEvent(radarData: EventRadarData) {
        LoadChart(radarData.radardata)
    }

}
