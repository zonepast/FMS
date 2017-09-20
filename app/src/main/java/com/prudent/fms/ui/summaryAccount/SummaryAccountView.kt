package com.prudent.fms.ui.summaryAccount

import com.prudent.fms.data.api.model.summaryAccount.DrAc.Response.SummaryAccountDrAcResponse
import com.prudent.fms.data.api.model.summaryAccount.crAC.Response.SummaryAccountCrAcResponse
import com.prudent.fms.data.api.model.summaryAccount.save.Response.SummaryAccountSaveResponse
import com.prudent.fms.data.api.model.summaryAccount.type.Response.SummaryAccountTypeResponse
import com.prudent.fms.ui.base.View

/**
 * Created by Dharmik Patel on 03-Aug-17.
 */
interface SummaryAccountView : View{
    fun progressShow()

    fun progressHide()

    fun Error(title: String, message: String, ref : Int)

    fun ShowDrAcResponse(response: SummaryAccountDrAcResponse)

    fun ShowCrAcResponse(response: SummaryAccountCrAcResponse)

    fun ShowTypeResponse(response: SummaryAccountTypeResponse)

    fun ShowSaveResponse(response: SummaryAccountSaveResponse)
}