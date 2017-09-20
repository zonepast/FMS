package com.prudent.fms.data.api.apihelper

import com.google.gson.JsonElement
import com.prudent.fms.data.api.model.Logout.Request.LogoutRequest
import com.prudent.fms.data.api.model.Logout.Response.LogoutResponse
import com.prudent.fms.data.api.model.calender.Request.CalenderRequest
import com.prudent.fms.data.api.model.calender.Response.CalenderResponse
import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptAmountCalc.Request.CheckReceiptAmountRequest
import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptAmountCalc.Response.ChequeReceiptAmountResponse
import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptBase.Request.ChequeReceiptBaseRequest
import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptBase.Response.ChequeReceiptBaseResponse
import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptSave.Request.CheckReceiptSaveRequest
import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptSave.Response.CheckReceiptSaveResponse
import com.prudent.fms.data.api.model.dashboard.Request.DashboardDataRequest
import com.prudent.fms.data.api.model.dashboard.Response.DashboardDataResponse
import com.prudent.fms.data.api.model.login.Request.LoginRequest
import com.prudent.fms.data.api.model.login.Response.LoginResponse
import com.prudent.fms.data.api.model.reportTableView.Request.ReportTableViewRequest
import com.prudent.fms.data.api.model.salesOrder.Broker.Request.SalesOrderBrokerBaseRequest
import com.prudent.fms.data.api.model.salesOrder.Broker.Response.SalesOrderBrokerResponse
import com.prudent.fms.data.api.model.salesOrder.CostCentre.Request.SalesOrderCostCentreRequest
import com.prudent.fms.data.api.model.salesOrder.CostCentre.Response.SalesOrderCostCentreResponse
import com.prudent.fms.data.api.model.salesOrder.Item.Request.SalesOrderItemBaseRequest
import com.prudent.fms.data.api.model.salesOrder.Item.Response.SalesOrderItemBaseResponse
import com.prudent.fms.data.api.model.salesOrder.PartyName.Base.Request.SalesOrderPartyBaseRequest
import com.prudent.fms.data.api.model.salesOrder.PartyName.Base.Response.SalesOrderPartyBaseResponse
import com.prudent.fms.data.api.model.salesOrder.PartyName.Load.Request.SalesOrderPartyLoadRequest
import com.prudent.fms.data.api.model.salesOrder.Rate.Request.SalesOrderRateRequest
import com.prudent.fms.data.api.model.salesOrder.Rate.Response.SalesOrderRateResponse
import com.prudent.fms.data.api.model.salesOrder.Save.Request.SalesOrderSaveRequest
import com.prudent.fms.data.api.model.salesOrder.Save.Response.SalesOrderSaveResponse
import com.prudent.fms.data.api.model.summaryAccount.DrAc.Request.SummaryAccountDrAcRequest
import com.prudent.fms.data.api.model.summaryAccount.DrAc.Response.SummaryAccountDrAcResponse
import com.prudent.fms.data.api.model.summaryAccount.base.Request.SummaryAccountRequest
import com.prudent.fms.data.api.model.summaryAccount.base.Response.SummaryAccountBaseResponse
import com.prudent.fms.data.api.model.summaryAccount.crAC.Request.SummaryAccountCrAcRequest
import com.prudent.fms.data.api.model.summaryAccount.crAC.Response.SummaryAccountCrAcResponse
import com.prudent.fms.data.api.model.summaryAccount.save.Request.SummaryAccountSaveRequest
import com.prudent.fms.data.api.model.summaryAccount.save.Response.SummaryAccountSaveResponse
import com.prudent.fms.data.api.model.summaryAccount.type.Request.SummaryAccountTypeRequest
import com.prudent.fms.data.api.model.summaryAccount.type.Response.SummaryAccountTypeResponse
import com.prudent.fms.data.api.model.uploadDocument.DetailUploadDocument.Request.DetailUploadDocumentRequest
import com.prudent.fms.data.api.model.uploadDocument.DetailUploadDocument.Response.DetailUploadDocumentResponse
import com.prudent.fms.data.api.model.uploadDocument.ShowDocument.Request.ShowDocumentRequest
import com.prudent.fms.data.api.model.uploadDocument.ShowDocument.Response.ShowDocumentResponse
import com.prudent.fms.data.api.model.uploadDocument.TranscationUploadDocument.Request.TranscationUploadDocumentRequest
import com.prudent.fms.data.api.model.uploadDocument.TranscationUploadDocument.Response.TranscationUploadDocumentResponse
import com.prudent.fms.data.api.model.uploadDocument.UploadDocument.Request.UploadDocumentRequest
import com.prudent.fms.data.api.model.uploadDocument.UploadDocument.Response.UploadDocumentResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Dharmik Patel on 20-Jul-17.
 */
interface ApiService {

    // Login
    @POST("api/LoginIDocs")
    fun Login(@Body body: LoginRequest): Call<LoginResponse>

    // Logout
    @POST("api/LoginIDocs")
    fun Logout(@Body body: LogoutRequest): Call<LogoutResponse>

    // Calender
    @POST("api/ScheduledTask")
    fun Calender(@Body body: CalenderRequest): Call<CalenderResponse>

    // Dashboard
    @POST("api/DashboardReport")
    fun GetDashboard(@Body body: DashboardDataRequest): Call<DashboardDataResponse>

    // TableView Detail Report
    @POST("api/DashboardReport")
    fun GetReportTableViewDetail(@Body body: ReportTableViewRequest): Call<JsonElement>


    /* Summary Account */

    // Base
    @POST("api/FormSummary")
    fun GetSummaryAccountBase(@Body body: SummaryAccountRequest): Call<SummaryAccountBaseResponse>

    // DrAc Account
    @POST("api/FormSummary")
    fun GetSummaryAccountDrAc(@Body body: SummaryAccountDrAcRequest): Call<SummaryAccountDrAcResponse>

    // CrAc Account
    @POST("api/FormSummary")
    fun GetSummaryAccountCrAc(@Body body: SummaryAccountCrAcRequest): Call<SummaryAccountCrAcResponse>

    // Type
    @POST("api/FormSummary")
    fun GetSummaryAccountType(@Body body: SummaryAccountTypeRequest): Call<SummaryAccountTypeResponse>

    // Save
    @POST("api/SummaryAccount")
    fun SaveSummaryAccount(@Body body: SummaryAccountSaveRequest): Call<SummaryAccountSaveResponse>


    /* Sales Order */

    // CostCentre
    @POST("api/FormSalesOrder")
    fun GetSalesOrderBase(@Body body: SalesOrderCostCentreRequest): Call<SalesOrderCostCentreResponse>

    // Party
    @POST("api/FormSalesOrder")
    fun GetSalesOrderPartyBase(@Body body: SalesOrderPartyBaseRequest): Call<SalesOrderPartyBaseResponse>

    // Broker
    @POST("api/FormSalesOrder")
    fun GetSalesOrderBrokerBase(@Body body: SalesOrderBrokerBaseRequest): Call<SalesOrderBrokerResponse>

    // ItemName
    @POST("api/FormSalesOrder")
    fun GetSalesOrderItemBase(@Body body: SalesOrderItemBaseRequest): Call<SalesOrderItemBaseResponse>

    // Party Load
    @POST("api/FormSalesOrder")
    fun GetSalesOrderPartyLoad(@Body body: SalesOrderPartyLoadRequest): Call<SalesOrderPartyBaseResponse>

    // Rate
    @POST("api/FormSalesOrder")
    fun GetSalesOrderRate(@Body body: SalesOrderRateRequest): Call<SalesOrderRateResponse>

    // Save
    @POST("api/SalesOrder")
    fun SaveSalesOrder(@Body body: SalesOrderSaveRequest): Call<SalesOrderSaveResponse>


    /* ChequeReceipt */

    @POST("api/ChequeReceipt")
    fun GetChequeReceiptBase(@Body body: ChequeReceiptBaseRequest): Call<ChequeReceiptBaseResponse>

    @POST("api/ChequeReceipt")
    fun GetChequeReceiptAmount(@Body body: CheckReceiptAmountRequest): Call<ChequeReceiptAmountResponse>

    @POST("api/SaveChequeReceipt")
    fun SaveChequeReceiptAmount(@Body body: CheckReceiptSaveRequest): Call<CheckReceiptSaveResponse>


    /* Upload Document */

    // Transaction Dropdown
    @POST("api/UploadDocument")
    fun LoadUploadTransactionBase(@Body body: TranscationUploadDocumentRequest): Call<TranscationUploadDocumentResponse>

    // Detail Dropdown
    @POST("api/UploadDocument")
    fun LoadDetailTransaction(@Body body: DetailUploadDocumentRequest): Call<DetailUploadDocumentResponse>

    // ShowDocument
    @POST("api/UploadDocument")
    fun ShowDocument(@Body body: ShowDocumentRequest): Call<ShowDocumentResponse>

    // UploadDocument
    @POST("api/SaveDocumentUpload")
    fun UploadDocument(@Body body: UploadDocumentRequest): Call<UploadDocumentResponse>

}