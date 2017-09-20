package com.prudent.fms.data.api.model.salesOrder.Item.Request

data class SalesOrderItemBaseRequest(
	val corpcentre: String? = null,
	val control: String? = null,
	val type: String? = null,
	val userid: String? = null
)
