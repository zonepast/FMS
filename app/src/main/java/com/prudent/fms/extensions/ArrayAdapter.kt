package com.prudent.fms.extensions

import android.R
import android.content.Context
import android.widget.ArrayAdapter
import com.prudent.fms.adapter.CustomArrayAdapter
import com.prudent.fms.data.model.SpinnerData

/**
 * Created by Dharmik Patel on 03-Aug-17.
 */
public fun SearchAdapter(context: Context,list: ArrayList<SpinnerData>) : ArrayAdapter<String>?{
    val VoucherAdapter = CustomArrayAdapter(context, (0..list.size - 1).map { list[it].xName!! })
    return VoucherAdapter
}