package com.prudent.fms.adapter

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.prudent.fms.R
import com.prudent.fms.data.db.model.dbLogin
import com.prudent.fms.extensions.bindView
import com.prudent.fms.kotlin_extensions.inflateLayout
import io.realm.RealmResults

/**
 * Created by Dharmik Patel on 24-Jul-17.
 */

class BranchArrayAdapter(private val mContext: Context, private val data: List<dbLogin>) : ArrayAdapter<dbLogin>(mContext, R.layout.spinner_content_layout, data) {

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = mContext.inflateLayout(R.layout.spinner_content_layout, parent, false)
        val txt_spinner_content = row.findViewById(R.id.txt_spinner_content) as TextView
        txt_spinner_content.text = data[position].xname
        txt_spinner_content.tag = data[position].xcode
        return row
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val row = mContext.inflateLayout(R.layout.spinner_title_layout, parent, false)
        val txt_spinner_title = row.findViewById(R.id.txt_spinner_title) as TextView
        txt_spinner_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_white, 0)
        txt_spinner_title.text = data[position].xname
        txt_spinner_title.tag = data[position].xcode
        return row
    }
}


