package com.prudent.fms.data.api.model.calender.Response

import com.google.gson.annotations.SerializedName

class CalenderResponse {

    @SerializedName("table")
    var table: List<Table>? = null

}
