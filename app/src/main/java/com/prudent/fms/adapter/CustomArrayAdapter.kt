package com.prudent.fms.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.prudent.fms.R
import io.realm.RealmResults

/**
 * Created by Dharmik Patel on 03-Aug-17.
 */
class CustomArrayAdapter(private val context1: Context, private val data: List<String?>) : ArrayAdapter<String>(context1, R.layout.layout_spinner_data, data) {

    var res: Resources? = null
    var inflater: LayoutInflater

    init {
        inflater = context1
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        val row = inflater.inflate(R.layout.layout_spinner_data, parent, false)
        val tvCategory = row.findViewById(R.id.txt_spinner) as TextView
        tvCategory.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_black, 0)
        tvCategory.text = data[position] ?: ""
        return row
    }
}
