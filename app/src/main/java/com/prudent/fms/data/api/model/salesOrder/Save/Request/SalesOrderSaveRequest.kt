package com.prudent.fms.data.api.model.salesOrder.Save.Request

data class SalesOrderSaveRequest(
	val area: String? = null,
	val corpcentre: String? = null,
	val ipaddress: String? = null,
	val unit: String? = null,
	val orderNo: String? = null,
	val srno: String? = null,
	val entryDateTime: String? = null,
	val type: String? = null,
	val userId: String? = null,
	val party: String? = null
)
