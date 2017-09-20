package com.prudent.fms.ui.main

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.app.AppCompatActivity
import android.transition.Explode
import android.view.*
import com.prudent.fms.R
import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptBase.Response.ChequeReceiptBaseResponse
import com.prudent.fms.data.api.model.salesOrder.Broker.Response.SalesOrderBrokerResponse
import com.prudent.fms.data.api.model.salesOrder.CostCentre.Response.SalesOrderCostCentreResponse
import com.prudent.fms.data.api.model.salesOrder.Item.Response.SalesOrderItemBaseResponse
import com.prudent.fms.data.api.model.salesOrder.PartyName.Base.Response.SalesOrderPartyBaseResponse
import com.prudent.fms.data.api.model.summaryAccount.base.Response.SummaryAccountBaseResponse
import com.prudent.fms.data.api.model.uploadDocument.TranscationUploadDocument.Response.TranscationUploadDocumentResponse
import com.prudent.fms.events.EventChequeReceiptBaseResponse
import com.prudent.fms.events.EventSalesOrderResponseBase
import com.prudent.fms.events.EventSummaryResponseBase
import com.prudent.fms.events.EventUploadDocumentBase
import com.prudent.fms.extensions.GetCorpCode
import com.prudent.fms.extensions.GetUserCode
import com.prudent.fms.extensions.dateNow
import com.prudent.fms.kotlin_extensions.alert
import com.prudent.fms.kotlin_extensions.lollipopOrNewer
import com.prudent.fms.ui.chequeReceipt.ChequeReceiptFragment
import com.prudent.fms.ui.salesorder.SalesOrderFragment
import com.prudent.fms.ui.summaryAccount.SummaryAccountFragment
import com.prudent.fms.ui.uploadDocument.UploadDocumentFragment
import com.prudent.fms.utils.Config
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.greenrobot.eventbus.EventBus

class MainActivity : AppCompatActivity(), MainView {

    lateinit var mainPresenter: MainPresenter
    lateinit var SalesOrderCostCentreResponse: SalesOrderCostCentreResponse
    lateinit var SalesOrderPartyNameResponse: SalesOrderPartyBaseResponse
    lateinit var SalesOrderBrokerResponse: SalesOrderBrokerResponse
    lateinit var SalesOrderItemBaseResponse: SalesOrderItemBaseResponse

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        SetTransition()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = GetCategory()

        initPresenter()
        onAttach()

        SetFragment()
    }

    private fun SetFragment() {
        if (GetCategory().equals("Summary Account")) {
            mainPresenter.LoadSummaryAccountBase(GetCorpCode()!!, GetUserCode()!!)
        }
        if (GetCategory().equals("Sales Order")) {
            mainPresenter.LoadSalesOrderBase(GetCorpCode()!!, GetUserCode()!!, dateNow("dd-MM-yyyy"))
        }
        if (GetCategory().equals("Cheque Receipt")) {
            mainPresenter.ChequeReceiptBase("base", GetCorpCode()!!, GetUserCode()!!)
        }
        if (GetCategory().equals("Upload Document")) {

            mainPresenter.LoadUploadDocumentTransactionBase(GetCorpCode()!!, GetUserCode()!!)

        }
    }

    private fun GetCategory(): String? {
        return intent.extras.getString(Config().KEY_CATEGORYNAME)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun SetTransition() {
        if (lollipopOrNewer()) {
            window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            window.allowEnterTransitionOverlap = true
            window.returnTransition = Explode();
            // window.enterTransition = Fade()
        }
    }

    private fun initPresenter() {
        mainPresenter = MainPresenter()
    }

    override fun onDestroy() {
        onDetach()
        super.onDestroy()
    }

    override fun onAttach() {
        mainPresenter.onAttach(this)
    }

    override fun onDetach() {
        mainPresenter.onDetach()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun Error(title: String, message: String) {
        val dialog = alert(message, title)
        dialog.negativeButton("OK") {

        }
        dialog.positiveButton("RETRY") {
            SetFragment()
        }
        dialog.show()
    }

    override fun progressbarShow() {
        main_progressBar.visibility = View.VISIBLE
    }

    override fun progressbarHide() {
        main_progressBar.visibility = View.GONE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }

    }

    override fun ShowSummaryAccountBaseResponse(response: SummaryAccountBaseResponse) {

        frame_layout_main.visibility = View.VISIBLE

        EventBus.getDefault().postSticky(EventSummaryResponseBase(response))

        val detail = SummaryAccountFragment()
        val t = supportFragmentManager.beginTransaction()
        t.replace(R.id.frame_layout_main, detail)
        t.commit()
    }

    override fun ShowSalesOrderBaseResponse(response: SalesOrderCostCentreResponse) {

        SalesOrderCostCentreResponse = response
        //frame_layout_main.visibility = View.VISIBLE

        mainPresenter.LoadSalesOrderPartyBase(GetCorpCode()!!, GetUserCode()!!)
        //EventBus.getDefault().postSticky(EventSalesOrderResponseBase(response))

        /*val detail = SalesOrderFragment()
        val t = supportFragmentManager.beginTransaction()
        t.replace(R.id.frame_layout_main, detail)
        t.commit()*/
    }

    override fun ShowSalesOrderPartyBaseResponse(response: SalesOrderPartyBaseResponse) {

        SalesOrderPartyNameResponse = response

        mainPresenter.LoadSalesOrderBrokerBase(GetCorpCode()!!, GetUserCode()!!)

    }

    override fun ShowSalesOrderBrokerBaseResponse(response: SalesOrderBrokerResponse) {

        SalesOrderBrokerResponse = response

        mainPresenter.LoadSalesOrderItemBase(GetCorpCode()!!, GetUserCode()!!)
    }

    override fun ShowSalesOrderItemBaseResponse(response: SalesOrderItemBaseResponse) {

        SalesOrderItemBaseResponse = response

        frame_layout_main.visibility = View.VISIBLE

        EventBus.getDefault().postSticky(EventSalesOrderResponseBase(SalesOrderCostCentreResponse, SalesOrderPartyNameResponse, SalesOrderBrokerResponse, SalesOrderItemBaseResponse))

        val detail = SalesOrderFragment()
        val t = supportFragmentManager.beginTransaction()
        t.replace(R.id.frame_layout_main, detail)
        t.commit()
    }

    override fun ShowChequeReceiptResponse(response: ChequeReceiptBaseResponse) {
        frame_layout_main.visibility = View.VISIBLE

        EventBus.getDefault().postSticky(EventChequeReceiptBaseResponse(response))

        val detail = ChequeReceiptFragment()
        val t = supportFragmentManager.beginTransaction()
        t.replace(R.id.frame_layout_main, detail)
        t.commit()
    }

    override fun ShowUploadDocumentResponse(response: TranscationUploadDocumentResponse) {
        frame_layout_main.visibility = View.VISIBLE

        EventBus.getDefault().postSticky(EventUploadDocumentBase(response))

        val detail = UploadDocumentFragment()
        val t = supportFragmentManager.beginTransaction()
        t.replace(R.id.frame_layout_main, detail)
        t.commit()
    }
}