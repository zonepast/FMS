package com.prudent.fms.ui.main

import com.prudent.fms.core.CoreApp
import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptBase.Request.ChequeReceiptBaseRequest
import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptBase.Response.ChequeReceiptBaseResponse
import com.prudent.fms.data.api.model.salesOrder.Broker.Request.SalesOrderBrokerBaseRequest
import com.prudent.fms.data.api.model.salesOrder.Broker.Response.SalesOrderBrokerResponse
import com.prudent.fms.data.api.model.salesOrder.CostCentre.Request.SalesOrderCostCentreRequest
import com.prudent.fms.data.api.model.salesOrder.CostCentre.Response.SalesOrderCostCentreResponse
import com.prudent.fms.data.api.model.salesOrder.Item.Request.SalesOrderItemBaseRequest
import com.prudent.fms.data.api.model.salesOrder.Item.Response.SalesOrderItemBaseResponse
import com.prudent.fms.data.api.model.salesOrder.PartyName.Base.Request.SalesOrderPartyBaseRequest
import com.prudent.fms.data.api.model.salesOrder.PartyName.Base.Response.SalesOrderPartyBaseResponse
import com.prudent.fms.data.api.model.summaryAccount.base.Request.SummaryAccountRequest
import com.prudent.fms.data.api.model.summaryAccount.base.Response.SummaryAccountBaseResponse
import com.prudent.fms.data.api.model.uploadDocument.TranscationUploadDocument.Request.TranscationUploadDocumentRequest
import com.prudent.fms.data.api.model.uploadDocument.TranscationUploadDocument.Response.TranscationUploadDocumentResponse
import com.prudent.fms.ui.base.Presenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

/**
 * Created by Dharmik Patel on 02-Aug-17.
 */
class MainPresenter : Presenter<MainView> {

    var mainView : MainView? = null

    override fun onAttach(view: MainView) {
        mainView = view
    }

    override fun onDetach() {
        mainView = null
    }

    fun LoadSummaryAccountBase(corpCentre : String,userId : String){
        mainView?.progressbarShow()

        val base = CoreApp.instance?.getNetworkManager()?.service?.GetSummaryAccountBase(
                SummaryAccountRequest(corpCentre,"Base",userId))
        base?.enqueue(object : Callback<SummaryAccountBaseResponse> {
            override fun onResponse(call: Call<SummaryAccountBaseResponse>?, response: Response<SummaryAccountBaseResponse>?) {
                mainView?.progressbarHide()
                if (response!!.isSuccessful){
                    val list = response.body()
                    mainView?.ShowSummaryAccountBaseResponse(list)
                }else{
                    mainView?.Error("Error", "Something went wrong")
                }
            }

            override fun onFailure(call: Call<SummaryAccountBaseResponse>?, t: Throwable?) {
                mainView?.progressbarHide()
                if (t is java.net.ConnectException) {
                    mainView?.Error("Internet Error", "Please, Check your Internet Connection!")
                } else if (t is SocketTimeoutException) {
                    mainView?.Error("Timeout Error", "Please, Try again!")
                } else {
                    mainView?.Error("Internet Error", "Please, Check your Internet Connection!")
                }
            }

        })
    }

    fun LoadSalesOrderBase(corpCentre : String,userId : String,date : String){
        mainView?.progressbarShow()

        val base = CoreApp.instance?.getNetworkManager()?.service?.GetSalesOrderBase(
                SalesOrderCostCentreRequest(corpCentre,"","drpLocation",
                        date,"WSALEORDER","Parameter",userId))
        base?.enqueue(object : Callback<SalesOrderCostCentreResponse> {
            override fun onResponse(call: Call<SalesOrderCostCentreResponse>?, response: Response<SalesOrderCostCentreResponse>?) {
                //mainView?.progressbarHide()
                if (response!!.isSuccessful){
                    val list = response.body()
                    mainView?.ShowSalesOrderBaseResponse(list)
                }else{
                    mainView?.Error("Error", "Something went wrong")
                }
            }

            override fun onFailure(call: Call<SalesOrderCostCentreResponse>?, t: Throwable?) {
                mainView?.progressbarHide()
                if (t is java.net.ConnectException) {
                    mainView?.Error("Internet Error", "Please, Check your Internet Connection!")
                } else if (t is SocketTimeoutException) {
                    mainView?.Error("Timeout Error", "Please, Try again!")
                } else {
                    mainView?.Error("Internet Error", "Please, Check your Internet Connection!")
                }
            }

        })
    }

    fun LoadSalesOrderPartyBase(corpCentre : String,userId : String){
        //mainView?.progressbarShow()

        val base = CoreApp.instance?.getNetworkManager()?.service?.GetSalesOrderPartyBase(
                SalesOrderPartyBaseRequest(corpCentre,"drpParty","base", userId))
        base?.enqueue(object : Callback<SalesOrderPartyBaseResponse> {
            override fun onResponse(call: Call<SalesOrderPartyBaseResponse>?, response: Response<SalesOrderPartyBaseResponse>?) {
                //mainView?.progressbarHide()
                if (response!!.isSuccessful){
                    val list = response.body()
                    mainView?.ShowSalesOrderPartyBaseResponse(list)
                }else{
                    mainView?.Error("Error", "Something went wrong")
                }
            }

            override fun onFailure(call: Call<SalesOrderPartyBaseResponse>?, t: Throwable?) {
                mainView?.progressbarHide()
                if (t is java.net.ConnectException) {
                    mainView?.Error("Internet Error", "Please, Check your Internet Connection!")
                } else if (t is SocketTimeoutException) {
                    mainView?.Error("Timeout Error", "Please, Try again!")
                } else {
                    mainView?.Error("Internet Error", "Please, Check your Internet Connection!")
                }
            }

        })
    }

    fun LoadSalesOrderBrokerBase(corpCentre : String,userId : String){
        //mainView?.progressbarShow()

        val base = CoreApp.instance?.getNetworkManager()?.service?.GetSalesOrderBrokerBase(
                SalesOrderBrokerBaseRequest(corpCentre,"drpBroker","base", userId))
        base?.enqueue(object : Callback<SalesOrderBrokerResponse> {
            override fun onResponse(call: Call<SalesOrderBrokerResponse>?, response: Response<SalesOrderBrokerResponse>?) {
               // mainView?.progressbarHide()
                if (response!!.isSuccessful){
                    val list = response.body()
                    mainView?.ShowSalesOrderBrokerBaseResponse(list)
                }else{
                    mainView?.Error("Error", "Something went wrong")
                }
            }

            override fun onFailure(call: Call<SalesOrderBrokerResponse>?, t: Throwable?) {
                mainView?.progressbarHide()
                if (t is java.net.ConnectException) {
                    mainView?.Error("Internet Error", "Please, Check your Internet Connection!")
                } else if (t is SocketTimeoutException) {
                    mainView?.Error("Timeout Error", "Please, Try again!")
                } else {
                    mainView?.Error("Internet Error", "Please, Check your Internet Connection!")
                }
            }

        })
    }

    fun LoadSalesOrderItemBase(corpCentre : String,userId : String){
        //mainView?.progressbarShow()

        val base = CoreApp.instance?.getNetworkManager()?.service?.GetSalesOrderItemBase(
                SalesOrderItemBaseRequest(corpCentre,"drpItemname","base", userId))
        base?.enqueue(object : Callback<SalesOrderItemBaseResponse> {
            override fun onResponse(call: Call<SalesOrderItemBaseResponse>?, response: Response<SalesOrderItemBaseResponse>?) {
                mainView?.progressbarHide()
                if (response!!.isSuccessful){
                    val list = response.body()
                    mainView?.ShowSalesOrderItemBaseResponse(list)
                }else{
                    mainView?.Error("Error", "Something went wrong")
                }
            }

            override fun onFailure(call: Call<SalesOrderItemBaseResponse>?, t: Throwable?) {
                mainView?.progressbarHide()
                if (t is java.net.ConnectException) {
                    mainView?.Error("Internet Error", "Please, Check your Internet Connection!")
                } else if (t is SocketTimeoutException) {
                    mainView?.Error("Timeout Error", "Please, Try again!")
                } else {
                    mainView?.Error("Internet Error", "Please, Check your Internet Connection!")
                }
            }

        })
    }

    fun ChequeReceiptBase(type: String, CorpCentre: String, userId: String) {
        mainView!!.progressbarShow()
        val call = CoreApp.instance?.getNetworkManager()?.service?.GetChequeReceiptBase(ChequeReceiptBaseRequest(userId, CorpCentre, type))
        call?.enqueue(object : Callback<ChequeReceiptBaseResponse> {
            override fun onResponse(call: Call<ChequeReceiptBaseResponse>, response: retrofit2.Response<ChequeReceiptBaseResponse>) {
                val serverResponse = response.body()
                mainView!!.progressbarHide()
                if (response.isSuccessful) {
                    mainView!!.ShowChequeReceiptResponse(serverResponse)
                } else {
                    mainView!!.Error("Try Again", "Something went wrong!")
                }
            }

            override fun onFailure(call: Call<ChequeReceiptBaseResponse>, t: Throwable) {
                mainView!!.progressbarHide()
                if (t is java.net.ConnectException) {
                    mainView!!.Error("Internet Error", "Please, Check your Internet Connection!")
                } else if (t is SocketTimeoutException) {
                    mainView!!.Error("Timeout Error", "Please, Try again!")
                } else {
                    mainView!!.Error("Internet Error", "Please, Check your Internet Connection!")
                }
            }
        })
    }

    fun LoadUploadDocumentTransactionBase(CorpCentre: String, userId: String) {
        mainView!!.progressbarShow()
        val document = CoreApp.instance?.getNetworkManager()?.service?.LoadUploadTransactionBase(TranscationUploadDocumentRequest("Account", userId,CorpCentre,"Base"))
        document?.enqueue(object : Callback<TranscationUploadDocumentResponse> {
            override fun onResponse(call: Call<TranscationUploadDocumentResponse>, response: retrofit2.Response<TranscationUploadDocumentResponse>) {
                val serverResponse = response.body()
                mainView!!.progressbarHide()
                if (response.isSuccessful) {
                    mainView!!.ShowUploadDocumentResponse(serverResponse)
                } else {
                    mainView!!.Error("Try Again", "Something went wrong!")
                }
            }

            override fun onFailure(call: Call<TranscationUploadDocumentResponse>, t: Throwable) {
                mainView!!.progressbarHide()
                if (t is java.net.ConnectException) {
                    mainView!!.Error("Internet Error", "Please, Check your Internet Connection!")
                } else if (t is SocketTimeoutException) {
                    mainView!!.Error("Timeout Error", "Please, Try again!")
                } else {
                    mainView!!.Error("Internet Error", "Please, Check your Internet Connection!")
                }
            }
        })
    }
}