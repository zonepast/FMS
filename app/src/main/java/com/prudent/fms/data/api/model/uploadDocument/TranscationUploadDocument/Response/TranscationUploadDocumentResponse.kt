package com.prudent.fms.data.api.model.uploadDocument.TranscationUploadDocument.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TranscationUploadDocumentResponse {

    @SerializedName("table")
    @Expose
    var table: List<Table>? = null

}
