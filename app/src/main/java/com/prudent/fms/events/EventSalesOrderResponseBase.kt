package com.prudent.fms.events

import com.prudent.fms.data.api.model.salesOrder.Broker.Response.SalesOrderBrokerResponse
import com.prudent.fms.data.api.model.salesOrder.CostCentre.Response.SalesOrderCostCentreResponse
import com.prudent.fms.data.api.model.salesOrder.Item.Response.SalesOrderItemBaseResponse
import com.prudent.fms.data.api.model.salesOrder.PartyName.Base.Response.SalesOrderPartyBaseResponse

/**
 * Created by Dharmik Patel on 05-Aug-17.
 */
open class EventSalesOrderResponseBase() {

    lateinit var response: SalesOrderCostCentreResponse
    lateinit var partyResponse: SalesOrderPartyBaseResponse
    lateinit var brokerResponse: SalesOrderBrokerResponse
    lateinit var itemResponse: SalesOrderItemBaseResponse

    constructor(response: SalesOrderCostCentreResponse,
                PartyNameresponse: SalesOrderPartyBaseResponse,
                brokerResponse: SalesOrderBrokerResponse,
                itemResponse: SalesOrderItemBaseResponse) : this() {
        this.response = response
        this.partyResponse = PartyNameresponse
        this.brokerResponse = brokerResponse
        this.itemResponse = itemResponse
    }

}