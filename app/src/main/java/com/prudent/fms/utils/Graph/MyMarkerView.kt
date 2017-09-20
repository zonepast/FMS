package com.prudent.fms.utils.Graph

import android.content.Context
import com.github.library.bubbleview.BubbleTextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.prudent.fms.R

/**
 * Created by Dharmik Patel on 01-Aug-17.
 */
class MyMarkerView(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    private val tvContent: BubbleTextView = findViewById(R.id.tvContent) as BubbleTextView

    override fun refreshContent(e: Entry, highlight: Highlight) {

        if (e is CandleEntry) {

            tvContent.text = "" + e.high
        } else {

            tvContent.text = "" + e.`val`
        }
    }

    override fun getXOffset(): Int {
        // this will center the marker-view horizontally
        return -(width / 2)
    }

    override fun getYOffset(): Int {
        // this will cause the marker-view to be above the selected value
        return -height
    }

    private fun ShowArrow() {

    }
}