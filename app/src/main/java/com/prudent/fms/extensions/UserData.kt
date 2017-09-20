package com.prudent.fms.extensions

import com.prudent.fms.data.db.model.dbLogin
import com.prudent.fms.data.db.model.dbNavigationDrawer
import com.prudent.fms.extensions.realm.delete
import com.prudent.fms.extensions.realm.query
import com.prudent.fms.extensions.realm.save

/**
 * Created by Dharmik Patel on 21-Jul-17.
 */
public fun GetCorpName(): String? {
    val corp = dbLogin().query {
        realmQuery ->
        realmQuery.equalTo("xmaster", "CORPCENTRE")
    }
    return corp[0].xname
}

public fun GetCorpCode(): String? {
    val corp = dbLogin().query {
        realmQuery ->
        realmQuery.equalTo("xmaster", "CORPCENTRE")
    }
    return corp[0].xcode
}

public fun GetUserName(): String? {
    val user = dbLogin().query {
        realmQuery ->
        realmQuery.equalTo("xmaster", "USER")
    }
    return user[0].xname
}

public fun GetUserCode(): String? {
    val user = dbLogin().query {
        realmQuery ->
        realmQuery.equalTo("xmaster", "USER")
    }
    return user[0].xcode
}

public fun GetRealmCount(): Int? {
    val user = dbLogin().query {
        realmQuery ->
        realmQuery.findAll()
    }
    return user.size
}

public fun DeletedbLogin() {
    dbLogin().delete {
        realmQuery ->
        realmQuery.findAll()
    }
}

public fun GetRealmData(): List<dbLogin> {
    val user = dbLogin().query {
        realmQuery ->
        realmQuery.equalTo("xmaster", "UNIT_CORP")
    }
    return user
}

public fun SaveDBNavigation(){
    if (GetDBNavigationCount() == 0){
        dbNavigationDrawer(0,"Receivables","Sales Order","",1).save()
        dbNavigationDrawer(0,"Account","Summary Account","",1).save()
        dbNavigationDrawer(0,"Forms","Cheque Receipt","",1).save()
        dbNavigationDrawer(0,"Document","Upload Document","",1).save()
    }else{
        DeleteDBNavigation()
        dbNavigationDrawer(0,"Receivables","Sales Order","",1).save()
        dbNavigationDrawer(0,"Account","Summary Account","",1).save()
        dbNavigationDrawer(0,"Forms","Cheque Receipt","",1).save()
        dbNavigationDrawer(0,"Document","Upload Document","",1).save()
    }
}

public fun DeleteDBNavigation() {
    dbNavigationDrawer().delete {
        realmQuery ->
        realmQuery.findAll()
    }
}

private fun GetDBNavigationCount(): Int? {
    val user = dbNavigationDrawer().query {
        realmQuery ->
        realmQuery.findAll()
    }
    return user.size
}

public fun GetDBNavigation1(): List<dbNavigationDrawer> {
    val user = dbNavigationDrawer().query {
        realmQuery ->
        realmQuery.equalTo("no",0)
    }
    return user
}

public fun GetDBNavigation2(string: String): List<dbNavigationDrawer> {
    val user = dbNavigationDrawer().query {
        realmQuery ->
        realmQuery.equalTo("category",string)
    }
    return user
}