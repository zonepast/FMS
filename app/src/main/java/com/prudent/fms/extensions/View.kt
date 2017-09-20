package com.prudent.fms.extensions

import android.view.View

/**
 * Created by Dharmik Patel on 24-Jul-17.
 */
fun <T: View> View.bindView(id: Int): T {
    val view = findViewById(id) ?:
            throw IllegalArgumentException("Given ID $id could not be found in $this!")
    return view as T
}