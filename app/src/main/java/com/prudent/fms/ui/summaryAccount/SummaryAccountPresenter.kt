package com.prudent.fms.ui.summaryAccount

import com.prudent.fms.core.CoreApp
import com.prudent.fms.data.api.model.summaryAccount.DrAc.Request.SummaryAccountDrAcRequest
import com.prudent.fms.data.api.model.summaryAccount.DrAc.Response.SummaryAccountDrAcResponse
import com.prudent.fms.data.api.model.summaryAccount.crAC.Request.SummaryAccountCrAcRequest
import com.prudent.fms.data.api.model.summaryAccount.crAC.Response.SummaryAccountCrAcResponse
import com.prudent.fms.data.api.model.summaryAccount.type.Request.SummaryAccountTypeRequest
import com.prudent.fms.data.api.model.summaryAccount.type.Response.SummaryAccountTypeResponse
import com.prudent.fms.ui.base.Presenter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import com.prudent.fms.data.api.apihelper.ApiConfig
import com.prudent.fms.data.api.model.summaryAccount.save.Request.SummaryAccountSaveRequest
import com.prudent.fms.data.api.model.summaryAccount.save.Response.SummaryAccountSaveResponse


/**
 * Created by Dharmik Patel on 03-Aug-17.
 */
class SummaryAccountPresenter : Presenter<SummaryAccountView> {
    var summaryAccountView: SummaryAccountView? = null

    override fun onAttach(view: SummaryAccountView) {
        summaryAccountView = view
    }

    override fun onDetach() {
        summaryAccountView = null
    }

    fun GetDrAc(corpcentre: String, para2: String, userid: String) {
        //summaryAccountView?.progressShow()
        val DrAC = CoreApp.instance?.getNetworkManager()?.service?.GetSummaryAccountDrAc(
                SummaryAccountDrAcRequest(corpcentre, "", "SummaryAccount",
                        "DrAccount", para2, "Parameter", userid))
        DrAC?.enqueue(object : Callback<SummaryAccountDrAcResponse> {
            override fun onResponse(call: Call<SummaryAccountDrAcResponse>?, response: Response<SummaryAccountDrAcResponse>?) {
              //  summaryAccountView?.progressHide()
                if (response!!.isSuccessful) {
                    var list = response.body()
                    summaryAccountView?.ShowDrAcResponse(list)
                } else {
                    summaryAccountView?.Error("Error", "Something went wrong", 1)
                }
            }

            override fun onFailure(call: Call<SummaryAccountDrAcResponse>?, t: Throwable?) {
               // summaryAccountView?.progressHide()
                if (t is java.net.ConnectException) {
                    summaryAccountView?.Error("Internet Error", "Please, Check your Internet Connection!", 1)
                } else if (t is SocketTimeoutException) {
                    summaryAccountView?.Error("Timeout Error", "Please, Try again!", 1)
                } else {
                    summaryAccountView?.Error("Internet Error", "Please, Check your Internet Connection!", 1)
                }
            }

        })
    }

    fun GetCrAc(corpcentre: String, drXcode: String, voucherXcode: String,userid: String,date : String?) {
       // summaryAccountView?.progressShow()
        val CrAC = CoreApp.instance?.getNetworkManager()?.service?.GetSummaryAccountCrAc(
                SummaryAccountCrAcRequest("",corpcentre,"",drXcode,"",
                        date,"SummaryAccount","CrAccount",voucherXcode,
                        "Parameter",userid))
        CrAC?.enqueue(object : Callback<SummaryAccountCrAcResponse> {
            override fun onResponse(call: Call<SummaryAccountCrAcResponse>?, response: Response<SummaryAccountCrAcResponse>?) {
               // summaryAccountView?.progressHide()
                if (response!!.isSuccessful) {
                    val list = response.body()
                    summaryAccountView?.ShowCrAcResponse(list)
                } else {
                    summaryAccountView?.Error("Error", "Something went wrong", 2)
                }
            }

            override fun onFailure(call: Call<SummaryAccountCrAcResponse>?, t: Throwable?) {
               // summaryAccountView?.progressHide()
                if (t is java.net.ConnectException) {
                    summaryAccountView?.Error("Internet Error", "Please, Check your Internet Connection!", 2)
                } else if (t is SocketTimeoutException) {
                    summaryAccountView?.Error("Timeout Error", "Please, Try again!", 2)
                } else {
                    summaryAccountView?.Error("Internet Error", "Please, Check your Internet Connection!", 2)
                }
            }

        })
    }

    fun GetType(corpcentre: String, drXcode: String,crXcode: String, voucherXcode: String,userid: String,date : String?) {
        //summaryAccountView?.progressShow()
        val CrAC = CoreApp.instance?.getNetworkManager()?.service?.GetSummaryAccountType(
                SummaryAccountTypeRequest("",corpcentre,"",drXcode,"",
                        date,"SummaryAccount","AcType",voucherXcode,
                        "Parameter",userid,crXcode))
        CrAC?.enqueue(object : Callback<SummaryAccountTypeResponse> {
            override fun onResponse(call: Call<SummaryAccountTypeResponse>?, response: Response<SummaryAccountTypeResponse>?) {
               // summaryAccountView?.progressHide()
                if (response!!.isSuccessful) {
                    val list = response.body()
                    summaryAccountView?.ShowTypeResponse(list)
                } else {
                    summaryAccountView?.Error("Error", "Something went wrong", 3)
                }
            }

            override fun onFailure(call: Call<SummaryAccountTypeResponse>?, t: Throwable?) {
               // summaryAccountView?.progressHide()
                if (t is java.net.ConnectException) {
                    summaryAccountView?.Error("Internet Error", "Please, Check your Internet Connection!", 3)
                } else if (t is SocketTimeoutException) {
                    summaryAccountView?.Error("Timeout Error", "Please, Try again!", 3)
                } else {
                    summaryAccountView?.Error("Internet Error", "Please, Check your Internet Connection!", 3)
                }
            }

        })
    }

    fun FormSummarySaveData(srno: String, voucher: String, date: String,
                            voucherNo: String, instumentNo: String,
                            drAccount: String, crAccount: String,
                            accountType: String, grossAmount: String,
                            taxRate: String, tax: String, totalAmount: String,
                            remarks: String, isRcm: String, userId: String,
                            ipaddress: String, unit: String, corpcentre: String,
                            entrydatetime: String) {
        summaryAccountView?.progressShow()
        val Save = CoreApp.instance?.getNetworkManager()?.service?.
                SaveSummaryAccount(SummaryAccountSaveRequest( srno, voucher, date, voucherNo,
                        instumentNo, drAccount, crAccount, accountType,
                        grossAmount, taxRate, tax, totalAmount, remarks,
                        isRcm, userId, ipaddress, unit, corpcentre, entrydatetime))
        Save?.enqueue(object : Callback<SummaryAccountSaveResponse> {
            override fun onResponse(call: Call<SummaryAccountSaveResponse>, response: Response<SummaryAccountSaveResponse>) {
                val serverResponse = response.body()
                summaryAccountView?.progressHide()
                if (response.isSuccessful) {
                    summaryAccountView?.ShowSaveResponse(serverResponse)
                } else {
                    summaryAccountView?.Error("Try Again", "Something went wrong",4)
                }
            }

            override fun onFailure(call: Call<SummaryAccountSaveResponse>, t: Throwable) {
                summaryAccountView?.progressHide()
                if (t is java.net.ConnectException) {
                    summaryAccountView?.Error("Internet Error", "Please, Check your Internet Connection!", 4)
                } else if (t is SocketTimeoutException) {
                    summaryAccountView?.Error("Timeout Error", "Please, Try again!", 4)
                } else {
                    summaryAccountView?.Error("Internet Error", "Please, Check your Internet Connection!", 4)
                }
            }
        })
    }
}