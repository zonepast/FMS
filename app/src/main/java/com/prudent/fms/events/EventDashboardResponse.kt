package com.prudent.fms.events

import com.prudent.fms.data.api.model.dashboard.Response.DashboardDataResponse

/**
 * Created by Dharmik Patel on 25-Jul-17.
 */
open class EventDashboardResponse() {

    lateinit var response: DashboardDataResponse

    constructor(response: DashboardDataResponse) : this() {
        this.response = response
    }


}