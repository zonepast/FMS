package com.prudent.fms.events

import com.prudent.fms.data.api.model.summaryAccount.base.Response.SummaryAccountBaseResponse

/**
 * Created by Dharmik Patel on 02-Aug-17.
 */

open class EventSummaryResponseBase() {

    lateinit var response: SummaryAccountBaseResponse

    constructor(response: SummaryAccountBaseResponse) : this() {
        this.response = response
    }

}