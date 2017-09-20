package com.prudent.fms.data.db.model

import io.realm.RealmObject

/**
 * Created by Dharmik Patel on 21-Jul-17.
 */
open class dbLogin() : RealmObject() {

    var xmaster: String? = null
    var xcode: String? = null
    var xname: String? = null

    constructor(xmaster: String, xcode: String, xname: String) : this() {
        this.xmaster = xmaster
        this.xcode = xcode
        this.xname = xname
    }

}