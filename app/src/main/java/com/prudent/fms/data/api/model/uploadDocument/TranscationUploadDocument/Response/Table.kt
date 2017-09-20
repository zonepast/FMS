package com.prudent.fms.data.api.model.uploadDocument.TranscationUploadDocument.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Table {

    @SerializedName("xcode")
    @Expose
    var xcode: String? = null
    @SerializedName("xname")
    @Expose
    var xname: String? = null

}
