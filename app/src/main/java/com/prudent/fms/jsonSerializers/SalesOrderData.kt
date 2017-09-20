package com.prudent.fms.jsonSerializers

/**
 * Created by Dharmik Patel on 09-Aug-17.
 */
class SalesOrderData {

    constructor(Srno: String?, Sr: String?, ItemName: String?, Qty: String?, Rate: String?, Amount: String?) {
        this.Srno = Srno
        this.Sr = Sr
        this.ItemName = ItemName
        this.Qty = Qty
        this.Rate = Rate
        this.Amount = Amount
    }

    var Srno : String ?= null
    var Sr : String ?= null
    var ItemName : String ?= null
    var Qty : String ?= null
    var Rate : String ?= null
    var Amount : String ?= null
}