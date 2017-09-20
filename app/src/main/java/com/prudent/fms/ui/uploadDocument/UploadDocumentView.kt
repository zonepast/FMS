package com.prudent.fms.ui.uploadDocument

import com.prudent.fms.data.api.model.uploadDocument.DetailUploadDocument.Response.DetailUploadDocumentResponse
import com.prudent.fms.data.api.model.uploadDocument.ShowDocument.Response.ShowDocumentResponse
import com.prudent.fms.data.api.model.uploadDocument.UploadDocument.Response.UploadDocumentResponse
import com.prudent.fms.ui.base.View

/**
 * Created by Dharmik Patel on 16-Aug-17.
 */
interface UploadDocumentView : View{

    fun progressShow()

    fun progressHide()

    fun Error(title: String, message: String, ref : Int)

    fun ShowTransactionDetailResponse(response: DetailUploadDocumentResponse)

    fun ShowShowDocumentResponse(response: ShowDocumentResponse)

    fun UploadDocumentResponse(response: UploadDocumentResponse)
}