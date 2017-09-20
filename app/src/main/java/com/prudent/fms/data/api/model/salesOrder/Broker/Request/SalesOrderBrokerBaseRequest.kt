package com.prudent.fms.data.api.model.salesOrder.Broker.Request

data class SalesOrderBrokerBaseRequest(
	val corpcentre: String? = null,
	val control: String? = null,
	val type: String? = null,
	val userid: String? = null
)
