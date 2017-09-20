package com.prudent.fms.ui.graph.linechart

import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.prudent.fms.R
import com.prudent.fms.events.graph.EventLineData
import com.prudent.fms.utils.Graph.MyMarkerView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*

class LineChartFragment : Fragment() {

    lateinit var lineChart: LineChart

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_line_chart, container, false)
        lineChart = view.findViewById(R.id.chart_line) as LineChart

        SetUpLineChart()

        return view
    }

    private fun SetUpLineChart() {
        val mv = MyMarkerView(activity, R.layout.layout_custom_markerview)
        lineChart.markerView = mv

        lineChart.setDrawGridBackground(false)
        lineChart.setDescription("")
        lineChart.isHighlightEnabled = true
        lineChart.setTouchEnabled(true)
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(true)
        lineChart.setPinchZoom(false)

        lineChart.animateXY(1000, 1500)

        val yAxis = lineChart.axisLeft
        yAxis.textColor = Color.parseColor("#757575")
        yAxis.setStartAtZero(false)

        lineChart.axisRight.isEnabled = false
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.xAxis.setDrawGridLines(false)
        val xLabels = lineChart.xAxis
        xLabels.position = XAxis.XAxisPosition.BOTTOM
        xLabels.textColor = Color.parseColor("#757575")

        val legend = lineChart.legend
        legend.position = Legend.LegendPosition.BELOW_CHART_RIGHT
    }

    private fun LoadChart(lineData: LineData) {
        lineChart.data = lineData
        lineChart.invalidate()
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
    fun OnTransactionEvent(lineData: EventLineData) {
        LoadChart(lineData.linedata)
    }
}
