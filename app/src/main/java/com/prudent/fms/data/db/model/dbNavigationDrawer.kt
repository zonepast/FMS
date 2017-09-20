package com.prudent.fms.data.db.model

import android.os.Debug
import io.realm.RealmObject

/**
 * Created by Dharmik Patel on 25-Jul-17.
 */

open class dbNavigationDrawer() : RealmObject() {

    var no: Int? = 0
    var category: String? = null
    var subcategory: String? = null
    var classname: String? = null
    var count: Int? = 0

    constructor(no: Int, category: String, subcategory: String, classname: String, count: Int) : this() {
        this.no = no
        this.category = category
        this.subcategory = subcategory
        this.classname = classname
        this.count = count
    }

}