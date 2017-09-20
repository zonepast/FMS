package com.prudent.fms.data.api.model.uploadDocument.DetailUploadDocument.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class DetailUploadDocumentResponse {

    @SerializedName("table")
    @Expose
    var table: List<Table>? = null

}
