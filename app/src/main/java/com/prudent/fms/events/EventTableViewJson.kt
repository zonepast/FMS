package com.prudent.fms.events

import com.google.gson.JsonElement

/**
 * Created by Dharmik Patel on 29-Jul-17.
 */
open class EventTableViewJson() {

    lateinit var response: JsonElement

    constructor(response: JsonElement) : this() {
        this.response = response
    }

}