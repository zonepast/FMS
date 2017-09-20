package com.prudent.fms.ui.salesorder

import com.prudent.fms.core.CoreApp
import com.prudent.fms.data.api.model.salesOrder.PartyName.Base.Response.SalesOrderPartyBaseResponse
import com.prudent.fms.data.api.model.salesOrder.PartyName.Load.Request.SalesOrderPartyLoadRequest
import com.prudent.fms.data.api.model.salesOrder.Rate.Request.SalesOrderRateRequest
import com.prudent.fms.data.api.model.salesOrder.Rate.Response.SalesOrderRateResponse
import com.prudent.fms.data.api.model.salesOrder.Save.Request.SalesOrderSaveRequest
import com.prudent.fms.data.api.model.salesOrder.Save.Response.SalesOrderSaveResponse
import com.prudent.fms.extensions.dateNow
import com.prudent.fms.ui.base.Presenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

/**
 * Created by Dharmik Patel on 05-Aug-17.
 */
class SalesOrderPresenter : Presenter<SalesOrderView> {

    var salesOrderView: SalesOrderView? = null

    override fun onAttach(view: SalesOrderView) {
        salesOrderView = view
    }

    override fun onDetach() {
        salesOrderView = null
    }

    fun GetPartyName(corpcentre: String, para4: String, userid: String,date: String) {
        salesOrderView?.progressShow()
        val DrAC = CoreApp.instance?.getNetworkManager()?.service?.GetSalesOrderPartyLoad(
                SalesOrderPartyLoadRequest(corpcentre, "", "drpParty",
                        date, "WSALEORDER", para4, "Parameter",userid))
        DrAC?.enqueue(object : Callback<SalesOrderPartyBaseResponse> {
            override fun onResponse(call: Call<SalesOrderPartyBaseResponse>?, response: Response<SalesOrderPartyBaseResponse>?) {
                salesOrderView?.progressHide()
                if (response!!.isSuccessful) {
                    val list = response.body()
                    salesOrderView?.ShowPartyResponse(list)
                } else {
                    salesOrderView?.Error("Error", "Something went wrong", 1)
                }
            }

            override fun onFailure(call: Call<SalesOrderPartyBaseResponse>?, t: Throwable?) {
                salesOrderView?.progressHide()
                if (t is java.net.ConnectException) {
                    salesOrderView?.Error("Internet Error", "Please, Check your Internet Connection!", 1)
                } else if (t is SocketTimeoutException) {
                    salesOrderView?.Error("Timeout Error", "Please, Try again!", 1)
                } else {
                    salesOrderView?.Error("Internet Error", "Please, Check your Internet Connection!", 1)
                }
            }

        })
    }

    fun GetRate(corpcentre: String, party: String,broker: String, userid: String,date: String) {
        salesOrderView?.progressShow()
        val DrAC = CoreApp.instance?.getNetworkManager()?.service?.GetSalesOrderRate(
                SalesOrderRateRequest(corpcentre,"","","","drpItemname",
                        date,"WSALEORDER","Parameter",userid,"","",
                        party,broker))
        DrAC?.enqueue(object : Callback<SalesOrderRateResponse> {
            override fun onResponse(call: Call<SalesOrderRateResponse>?, response: Response<SalesOrderRateResponse>?) {
                salesOrderView?.progressHide()
                if (response!!.isSuccessful) {
                    val list = response.body()
                    salesOrderView?.ShowRateResponse(list)
                } else {
                    salesOrderView?.Error("Error", "Something went wrong", 2)
                }
            }

            override fun onFailure(call: Call<SalesOrderRateResponse>?, t: Throwable?) {
                salesOrderView?.progressHide()
                if (t is java.net.ConnectException) {
                    salesOrderView?.Error("Internet Error", "Please, Check your Internet Connection!", 2)
                } else if (t is SocketTimeoutException) {
                    salesOrderView?.Error("Timeout Error", "Please, Try again!", 2)
                } else {
                    salesOrderView?.Error("Internet Error", "Please, Check your Internet Connection!", 2)
                }
            }

        })
    }

    fun Save(area : String, corpcentre: String, orderNo: String, party: String,
             type : String, unit: String,userid: String) {
        salesOrderView?.progressShow()
        val DrAC = CoreApp.instance?.getNetworkManager()?.service?.SaveSalesOrder(
                SalesOrderSaveRequest(area, corpcentre, "", unit, orderNo,
                        "", "", type,userid,party))
        DrAC?.enqueue(object : Callback<SalesOrderSaveResponse> {
            override fun onResponse(call: Call<SalesOrderSaveResponse>?, response: Response<SalesOrderSaveResponse>?) {
                salesOrderView?.progressHide()
                if (response!!.isSuccessful) {
                    val list = response.body()
                    salesOrderView?.ShowSaveResponse(list)
                } else {
                    salesOrderView?.Error("Error", "Something went wrong", 3)
                }
            }

            override fun onFailure(call: Call<SalesOrderSaveResponse>?, t: Throwable?) {
                salesOrderView?.progressHide()
                if (t is java.net.ConnectException) {
                    salesOrderView?.Error("Internet Error", "Please, Check your Internet Connection!", 3)
                } else if (t is SocketTimeoutException) {
                    salesOrderView?.Error("Timeout Error", "Please, Try again!", 3)
                } else {
                    salesOrderView?.Error("Internet Error", "Please, Check your Internet Connection!", 3)
                }
            }

        })
    }
}