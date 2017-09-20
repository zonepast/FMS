package com.prudent.fms.events.graph

import com.github.mikephil.charting.data.LineData

/**
 * Created by Dharmik Patel on 01-Aug-17.
 */

open class EventLineData() {

    lateinit var linedata: LineData

    constructor(linedata: LineData) : this() {
        this.linedata = linedata
    }

}
