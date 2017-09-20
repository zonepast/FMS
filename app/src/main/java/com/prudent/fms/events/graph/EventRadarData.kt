package com.prudent.fms.events.graph

import com.github.mikephil.charting.data.RadarData

/**
 * Created by Dharmik Patel on 01-Aug-17.
 */
open class EventRadarData() {

    lateinit var radardata: RadarData

    constructor(radardata: RadarData) : this() {
        this.radardata = radardata
    }

}