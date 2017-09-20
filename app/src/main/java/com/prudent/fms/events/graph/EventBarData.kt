package com.prudent.fms.events.graph

import com.github.mikephil.charting.data.BarData

/**
 * Created by Dharmik Patel on 01-Aug-17.
 */
open class EventBarData() {

    lateinit var bardata: BarData

    constructor(bardata: BarData) : this() {
        this.bardata = bardata
    }

}
