package com.prudent.fms.data.api.model.summaryAccount.save.Request

data class SummaryAccountSaveRequest(private val srno: String, private val voucher: String, private val date: String, private val voucherNo: String, private val instumentNo: String, private val drAccount: String, private val crAccount: String, private val accountType: String, private val grossAmount: String, private val taxRate: String, private val tax: String, private val totalAmount: String, private val remarks: String, private val isRcm: String, private val userId: String, private val ipaddress: String, private val unit: String, private val corpcentre: String, private val entrydatetime: String)
