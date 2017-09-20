package com.prudent.fms.data.model

/**
 * Created by Dharmik Patel on 02-Aug-17.
 */
class SpinnerData(xName: String?, xCode: String?) {
    var xName: String? = ""
    var xCode: String? = ""

    init {
        this.xName = xName ?: ""
        this.xCode = xCode ?: ""
    }

}