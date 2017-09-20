package com.prudent.fms.data.api.model.uploadDocument.UploadDocument.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Table {

    @SerializedName("success")
    @Expose
    var success: Int? = null
    @SerializedName("messsage")
    @Expose
    var messsage: String? = null

}
