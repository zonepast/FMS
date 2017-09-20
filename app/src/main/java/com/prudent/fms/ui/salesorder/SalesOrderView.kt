package com.prudent.fms.ui.salesorder

import com.prudent.fms.data.api.model.salesOrder.PartyName.Base.Response.SalesOrderPartyBaseResponse
import com.prudent.fms.data.api.model.salesOrder.Rate.Response.SalesOrderRateResponse
import com.prudent.fms.data.api.model.salesOrder.Save.Response.SalesOrderSaveResponse
import com.prudent.fms.ui.base.View

/**
 * Created by Dharmik Patel on 05-Aug-17.
 */
interface SalesOrderView : View {
    fun progressShow()

    fun progressHide()

    fun Error(title: String, message: String, ref : Int)

    fun ShowPartyResponse(response: SalesOrderPartyBaseResponse)

    fun ShowRateResponse(response: SalesOrderRateResponse)

    fun ShowSaveResponse(response: SalesOrderSaveResponse)
}