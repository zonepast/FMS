package com.prudent.fms.events

import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptBase.Response.ChequeReceiptBaseResponse

/**
 * Created by Dharmik Patel on 10-Aug-17.
 */
open class EventChequeReceiptBaseResponse() {

    lateinit var response: ChequeReceiptBaseResponse

    constructor(response: ChequeReceiptBaseResponse) : this() {
        this.response = response
    }


}