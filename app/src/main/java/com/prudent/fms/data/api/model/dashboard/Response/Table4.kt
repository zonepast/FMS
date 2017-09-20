package com.prudent.fms.data.api.model.dashboard.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Table4 {

    @SerializedName("sr")
    @Expose
    var sr: Int? = null
    @SerializedName("xcode")
    @Expose
    var xcode: String? = null
    @SerializedName("xname")
    @Expose
    var xname: String? = null
    @SerializedName("class")
    @Expose
    var class_: String? = null

}