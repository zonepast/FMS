package com.prudent.fms.extensions

/**
 * Created by Dharmik Patel on 04-Aug-17.
 */
fun Double.format(digits: Int) = java.lang.String.format("%.${digits}f", this)