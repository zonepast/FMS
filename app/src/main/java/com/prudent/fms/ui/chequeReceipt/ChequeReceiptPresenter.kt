package com.prudent.fms.ui.chequeReceipt

import com.prudent.fms.core.CoreApp
import com.prudent.fms.data.api.apihelper.ApiConfig
import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptAmountCalc.Request.CheckReceiptAmountRequest
import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptAmountCalc.Response.ChequeReceiptAmountResponse
import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptBase.Request.ChequeReceiptBaseRequest
import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptBase.Response.ChequeReceiptBaseResponse
import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptSave.Request.CheckReceiptSaveRequest
import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptSave.Response.CheckReceiptSaveResponse
import com.prudent.fms.ui.base.Presenter

import java.net.SocketTimeoutException

import retrofit2.Call
import retrofit2.Callback

/**
 * Created by Dharmik Patel on 14-Jul-17.
 */

class ChequeReceiptPresenter : Presenter<ChequeReceiptView> {

    private var chequeReceiptView: ChequeReceiptView? = null

    override fun onAttach(view: ChequeReceiptView) {
        chequeReceiptView = view
    }

    override fun onDetach() {
        chequeReceiptView = null
    }

    fun AmountCalc(control: String, date1: String, party: String, rate: String,
                   Bank: String, date2: String, days: String, amount: String) {
        chequeReceiptView!!.ShowBottomSheetProgress()
        val call = CoreApp.instance?.getNetworkManager()?.service?.GetChequeReceiptAmount(CheckReceiptAmountRequest(control,
                "", date1, party, "", rate, "", Bank, "", date2, "", "", days, amount))
        call?.enqueue(object : Callback<ChequeReceiptAmountResponse> {
            override fun onResponse(call: Call<ChequeReceiptAmountResponse>, response: retrofit2.Response<ChequeReceiptAmountResponse>) {
                val serverResponse = response.body()
                chequeReceiptView!!.HideBottomSheetProgress()
                if (response.isSuccessful()) {
                    chequeReceiptView!!.ShowChequeReceiptAmountResponse(serverResponse)
                } else {
                    chequeReceiptView!!.ErrorAmountDialog("Try Again", "Something went wrong!")
                }
            }

            override fun onFailure(call: Call<ChequeReceiptAmountResponse>, t: Throwable) {
                chequeReceiptView!!.HideBottomSheetProgress()
                if (t is java.net.ConnectException) {
                    chequeReceiptView!!.ErrorAmountDialog("Internet Error", "Please, Check your Internet Connection!")
                } else if (t is SocketTimeoutException) {
                    chequeReceiptView!!.ErrorAmountDialog("Timeout Error", "Please, Try again!")
                } else {
                    chequeReceiptView!!.ErrorAmountDialog("Internet Error", "Please, Check your Internet Connection!")
                }
            }
        })
    }

    fun SaveChequeReceipt(srno: String, date: String, party: String,
                          rate: String, chqno: String, bankname: String,
                          chqdate: String, total: String, amount: String,
                          interest: String, netamt: String, adjamt: String,
                          payable: String, userid: String,
                          entrydatetime: String, editedby: String,
                          editdatetime: String, corpcentre: String,
                          unitCorp: String, terminal: String) {
        chequeReceiptView!!.ShowBottomSheetProgress()
        val call = CoreApp.instance?.getNetworkManager()?.service?.SaveChequeReceiptAmount(CheckReceiptSaveRequest(srno, date, party, rate, chqno, bankname,
                chqdate, total, amount, interest, netamt, adjamt, payable, userid,
                entrydatetime, editedby, editdatetime, corpcentre, unitCorp,
                terminal))
        call?.enqueue(object : Callback<CheckReceiptSaveResponse> {
            override fun onResponse(call: Call<CheckReceiptSaveResponse>, response: retrofit2.Response<CheckReceiptSaveResponse>) {
                val serverResponse = response.body()
                chequeReceiptView!!.HideBottomSheetProgress()
                if (response.isSuccessful()) {
                    chequeReceiptView!!.ShowChequeReceiptSaveResponse(serverResponse)
                } else {
                    chequeReceiptView!!.ErrorSaveDialog("Try Again", "Something went wrong!")
                }
            }

            override fun onFailure(call: Call<CheckReceiptSaveResponse>, t: Throwable) {
                chequeReceiptView!!.HideBottomSheetProgress()
                if (t is java.net.ConnectException) {
                    chequeReceiptView!!.ErrorSaveDialog("Internet Error", "Please, Check your Internet Connection!")
                } else if (t is SocketTimeoutException) {
                    chequeReceiptView!!.ErrorSaveDialog("Timeout Error", "Please, Try again!")
                } else {
                    chequeReceiptView!!.ErrorSaveDialog("Internet Error", "Please, Check your Internet Connection!")
                }
            }
        })
    }
}
