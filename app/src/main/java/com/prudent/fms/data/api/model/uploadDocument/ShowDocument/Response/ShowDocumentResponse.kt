package com.prudent.fms.data.api.model.uploadDocument.ShowDocument.Response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ShowDocumentResponse {

    @SerializedName("table")
    @Expose
    var table: List<Table>? = null

}
