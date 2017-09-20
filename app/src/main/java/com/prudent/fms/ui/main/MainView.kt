package com.prudent.fms.ui.main

import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptBase.Response.ChequeReceiptBaseResponse
import com.prudent.fms.data.api.model.salesOrder.Broker.Response.SalesOrderBrokerResponse
import com.prudent.fms.data.api.model.salesOrder.CostCentre.Response.SalesOrderCostCentreResponse
import com.prudent.fms.data.api.model.salesOrder.Item.Response.SalesOrderItemBaseResponse
import com.prudent.fms.data.api.model.salesOrder.PartyName.Base.Response.SalesOrderPartyBaseResponse
import com.prudent.fms.data.api.model.summaryAccount.base.Response.SummaryAccountBaseResponse
import com.prudent.fms.data.api.model.uploadDocument.TranscationUploadDocument.Response.TranscationUploadDocumentResponse
import com.prudent.fms.ui.base.View

/**
 * Created by Dharmik Patel on 02-Aug-17.
 */
interface MainView : View {
    fun progressbarShow()

    fun progressbarHide()

    fun Error(title: String, message: String)

    fun ShowSummaryAccountBaseResponse(response: SummaryAccountBaseResponse)

    fun ShowSalesOrderBaseResponse(response: SalesOrderCostCentreResponse)

    fun ShowSalesOrderPartyBaseResponse(response: SalesOrderPartyBaseResponse)

    fun ShowSalesOrderBrokerBaseResponse(response: SalesOrderBrokerResponse)

    fun ShowSalesOrderItemBaseResponse(response: SalesOrderItemBaseResponse)

    fun ShowChequeReceiptResponse(response: ChequeReceiptBaseResponse)

    fun ShowUploadDocumentResponse(response: TranscationUploadDocumentResponse)
}