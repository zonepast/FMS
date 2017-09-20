package com.prudent.fms.ui.chequeReceipt

import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptAmountCalc.Response.ChequeReceiptAmountResponse
import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptBase.Response.ChequeReceiptBaseResponse
import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptSave.Response.CheckReceiptSaveResponse
import com.prudent.fms.ui.base.View

/**
 * Created by Dharmik Patel on 13-Jul-17.
 */

interface ChequeReceiptView : View {

    fun ShowProgress()

    fun HideProgress()

    fun ShowBottomSheetProgress()

    fun HideBottomSheetProgress()

    fun ErrorAmountDialog(Title: String, Message: String)

    fun ErrorSaveDialog(Title: String, Message: String)

    fun ShowChequeReceiptAmountResponse(response: ChequeReceiptAmountResponse)

    fun ShowChequeReceiptSaveResponse(response: CheckReceiptSaveResponse)
}
