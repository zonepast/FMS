package com.prudent.fms.jsonSerializers

import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

/**
 * Created by Dharmik Patel on 09-Aug-17.
 */
class SalesOrderSerializer : JsonSerializer<SalesOrderData> {

    override fun serialize(foo: SalesOrderData, type: Type, context: JsonSerializationContext): JsonElement {
        val `object` = JsonObject()
        `object`.add("Srno", context.serialize(foo.Srno))
        `object`.add("Sr", context.serialize(foo.Sr))
        `object`.add("ItemName", context.serialize(foo.ItemName))
        `object`.add("Qty", context.serialize(foo.Qty))
        `object`.add("Rate", context.serialize(foo.Rate))
        `object`.add("Amount", context.serialize(foo.Amount))
        return `object`
    }

}