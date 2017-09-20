package com.prudent.fms.ui.tableview

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.prudent.fms.R
import com.prudent.fms.events.EventTableViewJson
import com.prudent.fms.extensions.jsonKey
import com.prudent.fms.extensions.jsonValues
import com.prudent.fms.kotlin_extensions.colorRes
import com.prudent.fms.kotlin_extensions.d
import com.prudent.fms.kotlin_extensions.e
import com.prudent.fms.utils.TableView.CustomTableDataAdapter
import com.prudent.fms.utils.TableView.CustomTableHeaderAdapter
import de.codecrafters.tableview.toolkit.TableDataRowBackgroundProviders

import kotlinx.android.synthetic.main.activity_table_view.*
import kotlinx.android.synthetic.main.content_table_view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.ArrayList

class TableViewActivity : AppCompatActivity() {

    var key_list: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table_view)
        setSupportActionBar(toolbar)

        fab_close_tableView.setOnClickListener { onBackPressed() }

    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun OnTransactionEvent(jsonElement: EventTableViewJson) {
        val data = jsonElement.response

        GetKeys(data)
    }

    private fun GetKeys(jsonElement: JsonElement){
        val objectWhichYouNeed = jsonElement.asJsonObject
        val jsonArray = objectWhichYouNeed.getAsJsonArray("table")

        setupTableView(jsonArray)

    }

    private fun setupTableView(jsonArray: JsonArray){
        key_list = jsonKey(jsonArray)!!
        tableView_report_detail.columnCount = key_list.size

        val colorEvenRows = colorRes(R.color.white)
        val colorOddRows = colorRes(R.color.grey)
        tableView_report_detail.setDataRowBackgroundProvider(TableDataRowBackgroundProviders.alternatingRowColors(colorEvenRows, colorOddRows))

        val headerDataAdapter = CustomTableHeaderAdapter(applicationContext, key_list)
        tableView_report_detail.headerAdapter = headerDataAdapter

        val contentDataAdapter = CustomTableDataAdapter(applicationContext, jsonValues(jsonArray,key_list))
        tableView_report_detail.dataAdapter = contentDataAdapter

    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
