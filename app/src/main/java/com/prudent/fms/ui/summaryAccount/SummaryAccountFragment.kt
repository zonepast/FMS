package com.prudent.fms.ui.summaryAccount

import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.prudent.fms.R
import com.prudent.fms.data.api.model.summaryAccount.DrAc.Response.SummaryAccountDrAcResponse
import com.prudent.fms.data.api.model.summaryAccount.base.Response.SummaryAccountBaseResponse
import com.prudent.fms.data.api.model.summaryAccount.crAC.Response.SummaryAccountCrAcResponse
import com.prudent.fms.data.api.model.summaryAccount.type.Response.SummaryAccountTypeResponse
import com.prudent.fms.data.model.SpinnerData
import com.prudent.fms.events.EventSummaryResponseBase
import com.prudent.fms.extensions.*
import kotlinx.android.synthetic.main.fragment_summary_account.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.*
import kotlin.collections.ArrayList
import android.text.TextUtils
import com.prudent.fms.data.api.model.summaryAccount.save.Response.SummaryAccountSaveResponse
import com.prudent.fms.kotlin_extensions.*


class SummaryAccountFragment : Fragment(), SummaryAccountView {

    lateinit var summaryAccountBase: SummaryAccountBaseResponse
    private val voucherList = ArrayList<SpinnerData>()
    private val drACList = ArrayList<SpinnerData>()
    private val crACList = ArrayList<SpinnerData>()
    private val typeList = ArrayList<SpinnerData>()

    var mProgressDialog: ProgressDialog? = null
    lateinit var summaryAccountPresenter: SummaryAccountPresenter
    var voucherPosition: Int = 0
    var drACPosition: Int = 0
    var crACPosition: Int = 0
    var typePosition: Int = 0

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater!!.inflate(R.layout.fragment_summary_account, container, false)

        initPresenter()
        onAttach()

        setProgress()

        return view
    }

    private fun EditTextListeners(){
        summary_account_gross_amount.textWatcher {
            onTextChanged {
                text, start, before, count ->
                if (text?.length!! > 0){
                    summary_account_tax.setText(GetTaxValue().format(2))
                    summary_account_net_amount.setText(GetNetAmount().format(2))
                }
            }
        }
        summary_account_tax_rate.textWatcher {
            onTextChanged {
                text, start, before, count ->
                if (text?.length!! > 0) {
                    summary_account_tax.setText(GetTaxValue().format(2))
                    summary_account_net_amount.setText(GetNetAmount().format(2))
                }
            }
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        summary_account_date.text = dateNow("dd-MM-yyyy")
        summary_account_date.setOnClickListener {
            ShowDatePicker(summary_account_date, activity)
        }
        summary_account_save.setOnClickListener {
            checkValidity()
        }
        summary_account_reset.setOnClickListener {
            Reset()
        }

        EditTextListeners()
    }

    private fun Reset(){
        SetAllAdapter()
        summary_account_net_amount.setText("0.00")
        summary_account_tax_rate.setText("0.00")
        summary_account_tax.setText("0.00")
        summary_account_gross_amount.setText("0.00")
        summary_account_voucher_no.setText("")
        summary_account_party_ref.setText("")
        summary_account_remark.setText("")
        summary_account_chk_rcm.isChecked = false
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun OnTransactionEvent(response1: EventSummaryResponseBase) {
        summaryAccountBase = response1.response

        for (i in 0..summaryAccountBase.table!!.size - 1) {
            if (summaryAccountBase.table!![i]!!.control.equals("VoucherType")) {
                voucherList.add(SpinnerData(summaryAccountBase.table!![i]!!.xname, summaryAccountBase.table!![i]!!.xcode))
            }
            if (summaryAccountBase.table!![i]!!.control.equals("Account")) {
                crACList.add(SpinnerData(summaryAccountBase.table!![i]!!.xname, summaryAccountBase.table!![i]!!.xcode))
                drACList.add(SpinnerData(summaryAccountBase.table!![i]!!.xname, summaryAccountBase.table!![i]!!.xcode))
            }
            if (summaryAccountBase.table!![i]!!.control.equals("AcType")) {
                typeList.add(SpinnerData(summaryAccountBase.table!![i]!!.xname, summaryAccountBase.table!![i]!!.xcode))
            }
        }

        SetAllAdapter()
    }

    private fun SetAllAdapter() {
        SetVoucherAdapter()
        SetDRACAdapter()
        SetCRACAdapter()
        SetTYPEAdapter()

        setSpinnersListeners()

    }

    private fun setSpinnersListeners() {
        Handler().postDelayed(Runnable {
            summary_account_voucher.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(adapter: AdapterView<*>, v: View,
                                            position: Int, id: Long) {
                    voucherPosition = position
                    LoadDrAc()
                }

                override fun onNothingSelected(arg0: AdapterView<*>) {

                }
            }

        }, 1)

        summary_account_dr_ac.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapter: AdapterView<*>, v: View,
                                        position: Int, id: Long) {
                drACPosition = position
                LoadCrAc()
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {

            }
        }

        summary_account_cr_ac.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapter: AdapterView<*>, v: View,
                                        position: Int, id: Long) {
                crACPosition = position
                LoadType()
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {

            }
        }

        summary_account_type.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapter: AdapterView<*>, v: View,
                                        position: Int, id: Long) {
                typePosition = position
                //LoadType()
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {

            }
        }
    }

    private fun SetVoucherAdapter() {
        summary_account_voucher.adapter = SearchAdapter(activity, voucherList)
        summary_account_voucher.setTitle("Select Voucher")
    }

    private fun SetDRACAdapter() {
        summary_account_dr_ac.adapter = SearchAdapter(activity, drACList)
        summary_account_dr_ac.setTitle("Select Dr A/C")
       // summary_account_dr_ac.isSelected = false;
        //summary_account_dr_ac.setSelection(0, true);
    }

    private fun SetCRACAdapter() {
        summary_account_cr_ac.adapter = SearchAdapter(activity, crACList)
        summary_account_cr_ac.setTitle("Select Cr A/C")
        summary_account_cr_ac.isSelected = false;
        summary_account_cr_ac.setSelection(0, true);
    }

    private fun SetTYPEAdapter() {
        summary_account_type.adapter = SearchAdapter(activity, typeList)
        summary_account_type.setTitle("Select Type")
        /*summary_account_type.isSelected = false;
        summary_account_type.setSelection(0, true);*/
    }

    private fun LoadDrAc() {
        summaryAccountPresenter.GetDrAc(GetCorpCode()!!, voucherList[voucherPosition].xCode!!, GetUserCode()!!)
    }

    private fun LoadCrAc() {
        summaryAccountPresenter.GetCrAc(GetCorpCode()!!, drACList[drACPosition].xCode!!,
                voucherList[voucherPosition].xCode!!, GetUserCode()!!,
                ConvertDateFormat(summary_account_date.text.toString(), "dd-MM-yyyy", "yyyy-MM-dd"))
    }

    private fun LoadType() {
        summaryAccountPresenter.GetType(GetCorpCode()!!, drACList[drACPosition].xCode!!,
                crACList[crACPosition].xCode!!, voucherList[voucherPosition].xCode!!,GetUserCode()!!,
                ConvertDateFormat(summary_account_date.text.toString(), "dd-MM-yyyy", "yyyy-MM-dd"))
    }

    private fun initPresenter() {
        summaryAccountPresenter = SummaryAccountPresenter()
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
        summaryAccountPresenter.onAttach(this)
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
                LoadDrAc()
            }
        }
        if (ref == 2) {
            dialog.positiveButton("RETRY") {
                LoadCrAc()
            }
        }
        if (ref == 3) {
            dialog.positiveButton("RETRY") {
                LoadType()
            }
        }
        dialog.show()
    }

    override fun ShowDrAcResponse(response: SummaryAccountDrAcResponse) {
        if (drACList.size > 0) {
            drACList.clear()
        }
        (0..response.table!!.size - 1).mapTo(drACList) {
            SpinnerData(response.table[it]!!.xname, response.table[it]!!.xcode)
        }
        SetDRACAdapter()
    }

    override fun ShowCrAcResponse(response: SummaryAccountCrAcResponse) {
        if (crACList.size > 0) {
            crACList.clear()
        }
        (0..response.table!!.size - 1).mapTo(crACList) {
            SpinnerData(response.table[it]!!.xname, response.table[it]!!.xcode)
        }
        SetCRACAdapter()
    }

    override fun ShowTypeResponse(response: SummaryAccountTypeResponse) {
        if (typeList.size > 0) {
            typeList.clear()
        }
        (0..response.table!!.size - 1).mapTo(typeList) {
            SpinnerData(response.table[it]!!.xname, response.table[it]!!.xcode)
        }
        SetTYPEAdapter()
    }

    override fun ShowSaveResponse(response: SummaryAccountSaveResponse) {
        if (response.table!![0]!!.success == 1){
            activity.toast(response.table[0]!!.message!!)
            Reset()
        }
    }

    private fun GetTaxValue() : Double {

        val GrossAmount = summary_account_gross_amount.text ?: "0.00"
        val TaxRate = summary_account_tax_rate.text ?: "0.00"
        val Total = GrossAmount.toString().toDouble() * TaxRate.toString().toDouble()
        return Total * 0.01f
    }

    private fun GetNetAmount() : Double {

        val GrossAmount = summary_account_gross_amount.text ?: "0.00"
        val TaxValue = summary_account_tax.text ?: "0.00"
        val Total = GrossAmount.toString().toDouble() + TaxValue.toString().toDouble()
        return Total
    }

    private fun checkValidity() {
        val srno = ""
        val voucher = voucherList[voucherPosition].xCode
        val date = ConvertDateFormat(summary_account_date.text.toString(), "dd-MM-yyyy", "yyyy-MM-dd")
        val voucherNo = summary_account_voucher_no.text.toString()
        val instumentNo = summary_account_party_ref.text.toString()
        val drAccount = drACList[drACPosition].xCode
        val crAccount = crACList[crACPosition].xCode
        val accountType = typeList[typePosition].xCode
        val grossAmount = summary_account_gross_amount.text.toString()
        val taxRate = summary_account_tax_rate.text.toString()
        val tax = summary_account_tax.text.toString()
        val totalAmount = summary_account_net_amount.text.toString()
        val remarks = summary_account_remark.text.toString()
        val isRcm: String
        val userId = GetUserCode()
        val ipaddress = ""
        val unit = ""
        val corpcentre = GetCorpCode()
        val entrydatetime = ""

        if (TextUtils.isEmpty(voucherNo)) {
            summary_account_voucher_no.error = "Enter voucher No."
            return
        }
        if (TextUtils.isEmpty(instumentNo)) {
            summary_account_party_ref.error = "Enter Party Reference"
            return
        }
        if (TextUtils.isEmpty(grossAmount)) {
            summary_account_gross_amount.error = "Enter Gross Amount"
            return
        }
        if (TextUtils.isEmpty(tax)) {
            summary_account_tax.error = "Enter Tax Amount"
            return
        }
        if (summary_account_chk_rcm.isChecked) {
            isRcm = "1"
        } else { isRcm = "0" }

        summaryAccountPresenter.FormSummarySaveData(srno, voucher!!, date!!, voucherNo, instumentNo, drAccount!!, crAccount!!,
                accountType!!, grossAmount, taxRate, tax, totalAmount, remarks, isRcm, userId!!,
                ipaddress, unit, corpcentre!!, entrydatetime)

    }

}
