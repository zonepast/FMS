package com.prudent.fms.data.db.dbhelper

import com.prudent.fms.data.db.model.dbsalesOrder
import com.prudent.fms.extensions.realm.delete
import com.prudent.fms.extensions.realm.query
import com.prudent.fms.extensions.realm.save

/**
 * Created by Dharmik Patel on 08-Aug-17.
 */
public fun SaveSalesOrder(itemName: String,itemCode: String, qty: String, rate: String, amount: String): Boolean? {
  //  if (isAlreadySalesOrder(itemCode)!!){
        dbsalesOrder(itemName,itemCode,qty,rate,amount).save()
        return true
    /*}
    return false*/
}

public fun GetSalesOrderCount(): Int? {
    val user = dbsalesOrder().query {
        realmQuery ->
        realmQuery.findAll()
    }
    return user.size
}

public fun GetSalesOrder(): List<dbsalesOrder> {
    val user = dbsalesOrder().query {
        realmQuery ->
        realmQuery.findAll()
    }
    return user
}

public fun DeleteAllSalesOrder() {
    dbsalesOrder().delete {
        realmQuery ->
        realmQuery.findAll()
    }
}

public fun DeleteSalesOrderAt(xcode : String) {
    dbsalesOrder().delete {
        realmQuery ->
        realmQuery.equalTo("ItemCode",xcode)
    }
}