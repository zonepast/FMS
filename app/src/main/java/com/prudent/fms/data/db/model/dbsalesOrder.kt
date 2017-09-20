package com.prudent.fms.data.db.model
import io.realm.RealmObject

/**
 * Created by Dharmik Patel on 21-Jul-17.
 */
open class dbsalesOrder() : RealmObject() {

    var ItemName: String? = null
    var ItemCode: String? = null
    var qty: String? = null
    var rate: String? = null
    var amount: String? = null

    constructor(itemName: String,itemCode: String, qty: String, rate: String, amount: String) : this() {
        this.ItemName = itemName
        this.ItemCode = itemCode
        this.qty = qty
        this.rate = rate
        this.amount = amount
    }

}