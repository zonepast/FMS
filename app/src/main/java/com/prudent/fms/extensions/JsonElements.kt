package com.prudent.fms.extensions

import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

/**
 * Created by Dharmik Patel on 29-Jul-17.
 */

public fun jsonKey(jsonArray: JsonArray) : MutableList<String>? {
    val key_list: MutableList<String> = ArrayList()

    if (jsonArray.size() > 0) {
        val object3 = jsonArray.get(0).asJsonObject
        val yourJson = object3.toString()
        val parser = JsonParser()
        val element = parser.parse(yourJson)
        val obj = element.asJsonObject //since you know it's a JsonObject
        val entries = obj.entrySet()//will return members of your object
        for ((key) in entries) {
            key_list.add(key)
        }
        return key_list
    }
    return null
}

public fun jsonValues(jsonArray: JsonArray,mutableList: MutableList<String>) : Array<Array<String?>>{

    val array2d = Array2D<String>(jsonArray.size(),mutableList.size)
    for (i in 0..jsonArray.size() - 1) {
        val json_object = jsonArray.get(i).asJsonObject
        try {
            val jo2 = JSONObject(json_object.toString())
            for (k in mutableList.indices) {
                val valueString = jo2.getString(mutableList[k])
                array2d[i, k] = valueString
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    return array2d.array
}

class Array2D<T> (val xSize: Int, val ySize: Int, val array: Array<Array<T>>) {

    companion object {

        inline operator fun <reified T> invoke() = Array2D(0, 0, Array(0, { emptyArray<T>() }))

        inline operator fun <reified T> invoke(xWidth: Int, yWidth: Int) =
                Array2D(xWidth, yWidth, Array(xWidth, { arrayOfNulls<T>(yWidth) }))

        inline operator fun <reified T> invoke(xWidth: Int, yWidth: Int, operator: (Int, Int) -> (T)): Array2D<T> {
            val array = Array(xWidth, {
                val x = it
                Array(yWidth, {operator(x, it)})})
            return Array2D(xWidth, yWidth, array)
        }
    }

    operator fun get(x: Int, y: Int): T {
        return array[x][y]
    }

    operator fun set(x: Int, y: Int, t: T) {
        array[x][y] = t
    }

    inline fun forEach(operation: (T) -> Unit) {
        array.forEach { it.forEach { operation.invoke(it) } }
    }

    inline fun forEachIndexed(operation: (x: Int, y: Int, T) -> Unit) {
        array.forEachIndexed { x, p -> p.forEachIndexed { y, t -> operation.invoke(x, y, t) } }
    }
}