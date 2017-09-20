package com.prudent.fms.ui.uploadDocument

import com.prudent.fms.core.CoreApp
import com.prudent.fms.data.api.apihelper.ApiConfig
import com.prudent.fms.data.api.model.uploadDocument.DetailUploadDocument.Request.DetailUploadDocumentRequest
import com.prudent.fms.data.api.model.uploadDocument.DetailUploadDocument.Response.DetailUploadDocumentResponse
import com.prudent.fms.data.api.model.uploadDocument.ShowDocument.Request.ShowDocumentRequest
import com.prudent.fms.data.api.model.uploadDocument.ShowDocument.Response.ShowDocumentResponse
import com.prudent.fms.data.api.model.uploadDocument.UploadDocument.Request.UploadDocumentRequest
import com.prudent.fms.data.api.model.uploadDocument.UploadDocument.Response.UploadDocumentResponse
import com.prudent.fms.ui.base.Presenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

/**
 * Created by Dharmik Patel on 16-Aug-17.
 */
class UploadDocumentPresenter : Presenter<UploadDocumentView> {

    var uploadDocumentView: UploadDocumentView? = null

    override fun onAttach(view: UploadDocumentView) {
        uploadDocumentView = view
    }

    override fun onDetach() {
        uploadDocumentView = null
    }

    fun LoadDetailTransaction(Control: String, corpcentre: String, userid: String, transaction: String) {
        uploadDocumentView?.progressShow()
        val DrAC = CoreApp.instance?.getNetworkManager()?.service?.LoadDetailTransaction(
                DetailUploadDocumentRequest("Account", Control, userid,
                        corpcentre, transaction, "transaction"))
        DrAC?.enqueue(object : Callback<DetailUploadDocumentResponse> {
            override fun onResponse(call: Call<DetailUploadDocumentResponse>?, response: Response<DetailUploadDocumentResponse>?) {
                uploadDocumentView?.progressHide()
                if (response!!.isSuccessful) {
                    val list = response.body()
                    uploadDocumentView?.ShowTransactionDetailResponse(list)
                } else {
                    uploadDocumentView?.Error("Error", "Something went wrong", 1)
                }
            }

            override fun onFailure(call: Call<DetailUploadDocumentResponse>?, t: Throwable?) {
                uploadDocumentView?.progressHide()
                if (t is java.net.ConnectException) {
                    uploadDocumentView?.Error("Internet Error", "Please, Check your Internet Connection!", 1)
                } else if (t is SocketTimeoutException) {
                    uploadDocumentView?.Error("Timeout Error", "Please, Try again!", 1)
                } else {
                    uploadDocumentView?.Error("Internet Error", "Please, Check your Internet Connection!", 1)
                }
            }

        })
    }

    fun UploadDocument(voucher: String, srno: String, date: String, particular: String,
                       doc_value: String, doc_name: String, doc_ext: String,
                       doc_size: String, corpCentre: String, userid: String,
                       EntryDateTime: String) {
        uploadDocumentView?.progressShow()
        val upload = CoreApp.instance?.getNetworkManager()?.service?.UploadDocument(
                UploadDocumentRequest(voucher, srno, date, particular, doc_value, doc_name,
                        doc_ext, doc_size, corpCentre, userid, EntryDateTime))
        upload?.enqueue(object : Callback<UploadDocumentResponse> {
            override fun onResponse(call: Call<UploadDocumentResponse>, response: retrofit2.Response<UploadDocumentResponse>) {
                val serverResponse = response.body()
                uploadDocumentView?.progressHide()
                if (response.isSuccessful) {
                    uploadDocumentView?.UploadDocumentResponse(serverResponse)
                } else {
                    uploadDocumentView?.Error("Try Again", "Something went wrong!", 2)
                }
            }

            override fun onFailure(call: Call<UploadDocumentResponse>, t: Throwable) {
                uploadDocumentView?.progressHide()
                if (t is java.net.ConnectException) {
                    uploadDocumentView?.Error("Internet Error", "Please, Check your Internet Connection!", 2)
                } else if (t is SocketTimeoutException) {
                    uploadDocumentView?.Error("Timeout Error", "Please, Try again!", 2)
                } else {
                    uploadDocumentView?.Error("Internet Error", "Please, Check your Internet Connection!", 2)
                }
            }
        })
    }

    fun ShowDocument(corpcentre: String,userid: String,transaction: String,detail: String) {
        uploadDocumentView?.progressShow()
        val DrAC = CoreApp.instance?.getNetworkManager()?.service?.ShowDocument(
                ShowDocumentRequest("Account",userid,corpcentre,"btnShow",transaction,detail))
        DrAC?.enqueue(object : Callback<ShowDocumentResponse> {
            override fun onResponse(call: Call<ShowDocumentResponse>?, response: Response<ShowDocumentResponse>?) {
                uploadDocumentView?.progressHide()
                if (response!!.isSuccessful) {
                    val list = response.body()
                    uploadDocumentView?.ShowShowDocumentResponse(list)
                } else {
                    uploadDocumentView?.Error("Error", "Something went wrong", 3)
                }
            }

            override fun onFailure(call: Call<ShowDocumentResponse>?, t: Throwable?) {
                uploadDocumentView?.progressHide()
                if (t is java.net.ConnectException) {
                    uploadDocumentView?.Error("Internet Error", "Please, Check your Internet Connection!", 3)
                } else if (t is SocketTimeoutException) {
                    uploadDocumentView?.Error("Timeout Error", "Please, Try again!", 3)
                } else {
                    uploadDocumentView?.Error("Internet Error", "Please, Check your Internet Connection!", 3)
                }
            }

        })
    }

}