package com.prudent.fms.data.api.model.uploadDocument.ShowDocument.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Table {

    @SerializedName("doc_Value")
    @Expose
    var docValue: String? = null
    @SerializedName("doc_name")
    @Expose
    var docName: String? = null
    @SerializedName("doc_Ext")
    @Expose
    var docExt: String? = null

}
