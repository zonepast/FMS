package com.prudent.fms.ui.salesorder

import android.app.AlertDialog
import android.app.ProgressDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.RelativeLayout
import android.widget.TextView
import com.google.gson.GsonBuilder
import com.prudent.fms.R
import com.prudent.fms.adapter.SalesOrderAdapter
import com.prudent.fms.adapter.SalesOrderAdapterInterface
import com.prudent.fms.core.CoreApp
import com.prudent.fms.data.api.model.salesOrder.PartyName.Base.Response.SalesOrderPartyBaseResponse
import com.prudent.fms.data.api.model.salesOrder.Rate.Response.SalesOrderRateResponse
import com.prudent.fms.data.api.model.salesOrder.Save.Response.SalesOrderSaveResponse
import com.prudent.fms.data.db.dbhelper.*
import com.prudent.fms.data.model.SpinnerData
import com.prudent.fms.events.EventSalesOrderResponseBase
import com.prudent.fms.extensions.*
import com.prudent.fms.jsonSerializers.SalesOrderData
import com.prudent.fms.jsonSerializers.SalesOrderSerializer
import com.prudent.fms.kotlin_extensions.*
import com.prudent.fms.utils.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_sales_order.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class SalesOrderFragment : Fragment(), SalesOrderView, SalesOrderAdapterInterface {

    var mProgressDialog: ProgressDialog? = null
    lateinit var salesOrderPresenter: SalesOrderPresenter
    lateinit var response1: EventSalesOrderResponseBase
    lateinit var adapter: SalesOrderAdapter
    lateinit var Dialog: AlertDialog

    private var productLists: MutableList<SalesOrderData> = java.util.ArrayList<SalesOrderData>()
    private val costCentreList = ArrayList<SpinnerData>()
    private val partyNameList = ArrayList<SpinnerData>()
    private val brokerList = ArrayList<SpinnerData>()
    private val itemList = ArrayList<SpinnerData>()
    var costCentrePosition: Int = 0
    var partyNamePosition: Int = 0
    var brokerPosition: Int = 0
    var itemPosition: Int = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_sales_order, container, false)

        initPresenter()
        onAttach()

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        EditTextListener()

        setProgress()

        SetCountNo()

        sales_order_show.setOnClickListener {
            if (GetSalesOrderCount()!! > 0) {
                ShowBottomDialog()
            } else {
                activity.toast("Please, Add Atleast one item!")
            }
        }

        sales_order_add.setOnClickListener {
            val itemname = itemList[sales_order_item_type.selectedItemPosition].xName
            val itemcode = itemList[sales_order_item_type.selectedItemPosition].xCode

            var failFlag = false
            if (sales_order_qty.text.toString().trim({ it <= ' ' }).isEmpty()) {
                failFlag = true
                sales_order_qty.error = "Qty Cannot be Empty"
            }
            /*if (sales_order_amount.text.toString().trim({ it <= ' ' }).isEmpty()) {
                failFlag = true
                sales_order_amount.error = "Amount Cannot be Empty"
            }
            */
            if (!failFlag) {
                SaveSalesOrder(itemname!!, itemcode!!, sales_order_qty.text.toString(), sales_order_tax_rate.text.toString(), sales_order_amount.text.toString())
                Reset()
            }
        }

        sales_order_save.setOnClickListener {
            if (GetSalesOrderCount()!! > 0) {
                Validation()
            } else {
                activity.toast("Please, Add Atleast one item!")
            }
        }
    }

    private fun Reset() {
        SetCountNo()

        /*costCentreList.clear()
        partyNameList.clear()
        brokerList.clear()
        itemList.clear()

        LoadContent()*/

        sales_order_no.setText("")
        sales_order_tax_rate.setText("0.00")
        sales_order_tax_rate_charge.setText("0.00")
        sales_order_net_rate.setText("0.00")
        sales_order_amount.setText("")
        sales_order_qty.setText("")
    }

    private fun SetCountNo() {
        if (GetSalesOrderCount()!! > 0) {
            sales_order_show.text = "${"Show - "} ${GetSalesOrderCount()} ${" Items"}"
        } else {
            sales_order_show.text = "${"Show"}"
        }
    }

    private fun EditTextListener() {
        sales_order_tax_rate.textWatcher {
            onTextChanged {
                text, start, before, count ->
                if (text?.length!! > 0) {
                    sales_order_net_rate.setText(multipication());
                }
            }
        }
        sales_order_qty.textWatcher {
            onTextChanged {
                text, start, before, count ->
                if (text?.length!! > 0) {
                    sales_order_amount.setText(amountCalculation());
                }
            }
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun OnTransactionEvent(response: EventSalesOrderResponseBase) {

        response1 = response

        LoadContent()
    }

    private fun LoadContent() {
        val summaryAccountBase = response1.response
        val PartyName = response1.partyResponse
        val Broker = response1.brokerResponse
        val item = response1.itemResponse

        (0..summaryAccountBase.table!!.size - 1).mapTo(costCentreList) {
            SpinnerData(summaryAccountBase.table[it]!!.xname, summaryAccountBase.table[it]!!.xcode)
        }

        (0..PartyName.table!!.size - 1).mapTo(partyNameList) {
            SpinnerData(PartyName.table[it]!!.xname, PartyName.table[it]!!.xcode)
        }

        (0..Broker.table!!.size - 1).mapTo(brokerList) {
            SpinnerData(Broker.table[it]!!.xname, Broker.table[it]!!.xcode)
        }

        (0..item.table!!.size - 1).mapTo(itemList) {
            SpinnerData(item.table[it]!!.xname, item.table[it]!!.xcode)
        }

        SetAllAdapter()
    }

    private fun SetAllAdapter() {
        SetCostCentreAdapter()
        SetPartyNameAdapter()
        SetBrokerAdapter()
        SetItemAdapter()

        setSpinnersListeners()
    }

    private fun setSpinnersListeners() {
        Handler().postDelayed(Runnable {
            sales_order_cost_centre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(adapter: AdapterView<*>, v: View,
                                            position: Int, id: Long) {
                    costCentrePosition = position
                    LoadParty()
                }

                override fun onNothingSelected(arg0: AdapterView<*>) {

                }
            }

        }, 1)

        Handler().postDelayed(Runnable {
            sales_order_item_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(adapter: AdapterView<*>, v: View,
                                            position: Int, id: Long) {
                    itemPosition = position
                    LoadRate()
                }

                override fun onNothingSelected(arg0: AdapterView<*>) {

                }
            }
        }, 2)
    }

    private fun LoadParty() {
        /*if (partyNameList.size > 0){
            partyNameList.clear()
        }
        */salesOrderPresenter.GetPartyName(GetCorpCode()!!, costCentreList[partyNamePosition].xCode!!, GetUserCode()!!, dateNow("dd-MM-yyyy"))
    }

    private fun LoadRate() {
        salesOrderPresenter.GetRate(GetCorpCode()!!, partyNameList[sales_order_party_name.selectedItemPosition].xCode!!,
                brokerList[sales_order_broker.selectedItemPosition].xCode!!, GetUserCode()!!, dateNow("dd-MM-yyyy"))
    }

    private fun SetCostCentreAdapter() {
        sales_order_cost_centre.adapter = SearchAdapter(activity, costCentreList)
        sales_order_cost_centre.setTitle("Select CostCentre")
    }

    private fun SetPartyNameAdapter() {
        sales_order_party_name.adapter = SearchAdapter(activity, partyNameList)
        sales_order_party_name.setTitle("Select Party")
    }

    private fun SetBrokerAdapter() {
        sales_order_broker.adapter = SearchAdapter(activity, brokerList)
        sales_order_broker.setTitle("Select Broker")
    }

    private fun SetItemAdapter() {
        sales_order_item_type.adapter = SearchAdapter(activity, itemList)
        sales_order_item_type.setTitle("Select Item Type")
        /*if (itemList.size > 0){
            itemList.clear()
        }*/
    }

    private fun initPresenter() {
        salesOrderPresenter = SalesOrderPresenter()
    }

    private fun setProgress() {
        mProgressDialog = activity.progressDialog("Please Wait", "Loading")
    }

    override fun onDestroy() {
        //onDetach()
        super.onDestroy()
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onAttach() {
        salesOrderPresenter.onAttach(this)
    }

    override fun progressShow() {
        mProgressDialog?.show()
    }

    override fun progressHide() {
        mProgressDialog?.dismiss()
    }

    override fun Error(title: String, message: String, ref: Int) {
        val dialog = activity.alert(message, title)
        if (ref == 1) {
            dialog.positiveButton("RETRY") {
                LoadParty()
            }
        }
        if (ref == 2) {
            dialog.positiveButton("RETRY") {
                LoadRate()
            }
        }
        if (ref == 3) {
            dialog.positiveButton("RETRY") {
                Validation()
            }
        }
        dialog.show()
    }

    override fun ShowPartyResponse(response: SalesOrderPartyBaseResponse) {
        if (partyNameList.size > 0) {
            partyNameList.clear()
        }
        (0..response.table!!.size - 1).mapTo(partyNameList) {
            SpinnerData(response.table[it]!!.xname, response.table[it]!!.xcode)
        }
        SetPartyNameAdapter()
    }

    override fun ShowRateResponse(response: SalesOrderRateResponse) {
        sales_order_tax_rate_charge.setText(response.table!![0]!!.rate!!.format(2))
    }

    private fun multipication(): String {
        var taxRate = 0.0f
        var taxValue = 0.0f
        var rateCharge = 0.0f
        var netRate = 0.0f
        try {
            taxRate = java.lang.Float.parseFloat(sales_order_tax_rate.text.toString())
        } catch (e: NumberFormatException) {
        }

        try {
            rateCharge = java.lang.Float.parseFloat(sales_order_tax_rate_charge.text.toString())
        } catch (e: NumberFormatException) {
        }

        taxValue = taxRate + 100
        netRate = (rateCharge.toDouble() * taxValue.toDouble() * 0.01).toFloat()
        return String.format("%.2f", netRate)
    }

    private fun amountCalculation(): String {
        var quantity = 0.0f
        var netAmount = 0.0f
        var rate = 0.0f
        try {
            quantity = java.lang.Float.parseFloat(sales_order_qty.text.toString())
            rate = java.lang.Float.parseFloat(multipication())

        } catch (e: NumberFormatException) {
        }

        netAmount = quantity * rate
        return String.format("%.2f", netAmount)

    }

    private fun ShowBottomDialog() {
        val alertdialog = AlertDialog.Builder(activity, R.style.DialogTheme)

        val sheetView = this.layoutInflater.inflate(R.layout.layout_sales_order_realm, null)
        alertdialog.setView(sheetView)

        Dialog = alertdialog.create()
        val width = (resources.displayMetrics.widthPixels * 0.80).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.70).toInt()

        Dialog.window.setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
        Dialog.window.setGravity(Gravity.CENTER)
        Dialog.window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val txt_saved_item = sheetView.findViewById(R.id.sales_order_txt_saved_items) as TextView
        val txt_delete_all_item = sheetView.findViewById(R.id.sales_order_txt_delete_all) as TextView
        val recyclerView = sheetView.findViewById(R.id.sales_order_recyclerView) as RecyclerView
        val btn_close = sheetView.findViewById(R.id.sales_order_btn_close) as ImageButton

        txt_saved_item.text = "${"Saved Items ("}${GetSalesOrderCount()}${")"}"

        txt_delete_all_item.setOnClickListener {
            DeleteAllSalesOrder()
            Dialog.dismiss()
            SetCountNo()
        }

        btn_close.setOnClickListener {
            Dialog.dismiss()
        }

        val LayoutManager = LinearLayoutManager(activity)
        recyclerView.layoutManager = LayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(DividerItemDecoration(activity.drawableRes(R.drawable.dr_divider), true, false))

        adapter = SalesOrderAdapter(GetSalesOrder().toMutableList(), activity, this)
        recyclerView.adapter = adapter

        Dialog.show()
    }

    override fun DeleteSalesOrder(xcode: String) {
        DeleteSalesOrderAt(xcode)
        SetCountNo()
        if (GetSalesOrderCount()!! == 0) {
            Dialog.dismiss()
        }
    }

    private fun SaveType() {
        productLists.clear()
        val realmResults = GetSalesOrder()
        for (i in 0..realmResults.size - 1) {
            productLists.add(SalesOrderData("", (i + 1).toString(), realmResults[i].ItemCode,
                    realmResults[i].qty, realmResults[i].rate, realmResults[i].amount))
        }
    }

    private fun GetType(): String {
        val gson = GsonBuilder().registerTypeAdapter(SalesOrderData::class.java, SalesOrderSerializer()).create()
        return gson.toJson(productLists)
    }

    override fun ShowSaveResponse(response: SalesOrderSaveResponse) {
        if (response.table!![0]!!.success!! == 1) {
            activity.toast(response.table[0]!!.message!!)
        } else {
            activity.toast(response.table[0]!!.message!!)
            DeleteAllSalesOrder()
            Reset()
            sales_order_no.setText("")
        }
    }

    private fun Validation() {
        var failFlag = false
        if (sales_order_no.text.toString().trim({ it <= ' ' }).isEmpty()) {
            failFlag = true
            sales_order_no.error = "Order No. Cannot be Empty"
        }
        if (!failFlag) {
            SaveType()
            val areacode = costCentreList[sales_order_cost_centre.selectedItemPosition].xCode
            val partycode = partyNameList[sales_order_party_name.selectedItemPosition].xCode
            val order = sales_order_no.text
            salesOrderPresenter.Save(areacode!!, GetCorpCode()!!, order.toString(), partycode!!,
                    GetType(), CoreApp.instance?.getSessionManager()!!.branchcode, GetUserCode()!!)
        }
        e("type", GetType() + "")
    }

}