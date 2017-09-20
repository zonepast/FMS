package com.prudent.fms.ui.chequeReceipt

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialog
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*

import com.prudent.fms.R
import com.prudent.fms.core.CoreApp
import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptAmountCalc.Response.ChequeReceiptAmountResponse
import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptBase.Response.ChequeReceiptBaseResponse
import com.prudent.fms.data.api.model.chequeReceipt.CheckReceiptSave.Response.CheckReceiptSaveResponse
import com.prudent.fms.data.model.SpinnerData
import com.prudent.fms.events.EventChequeReceiptBaseResponse
import com.prudent.fms.extensions.GetCorpCode
import com.prudent.fms.extensions.GetUserCode
import com.prudent.fms.extensions.SearchAdapter
import com.prudent.fms.extensions.dateNow
import com.prudent.fms.kotlin_extensions.progressDialog
import com.prudent.fms.kotlin_extensions.toast
import kotlinx.android.synthetic.main.fragment_cheque_receipt.*
import kotlinx.android.synthetic.main.fragment_sales_order.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ChequeReceiptFragment : Fragment(),ChequeReceiptView {

    var mBottomSheetDialog: BottomSheetDialog? = null
    var mProgressDialog: ProgressDialog? = null
    lateinit var ChequeReceiptPresenter: ChequeReceiptPresenter

    private val partyNameList = ArrayList<SpinnerData>()
    private val RateList = ArrayList<SpinnerData>()
    lateinit var linearLayout_bottom_sheet: LinearLayout
    lateinit var progressBar_bottom_sheet: ProgressBar
    lateinit var btn_save: Button
    lateinit var editText_interest: EditText
    lateinit var editText_net_amount:EditText
    lateinit var editText_payable:EditText
    lateinit var editText_adjustable:EditText

    private val bank = arrayOf("ABHYUDAYA COOPERATIVE BANK LIMITED", "ABU DHABI COMMERCIAL BANK", "AHMEDABAD MERCANTILE COOPERATIVE BANK", "AIRTEL PAYMENTS BANK LIMITED", "AKOLA JANATA COMMERCIAL COOPERATIVE BANK", "ALLAHABAD BANK", "ALMORA URBAN COOPERATIVE BANK LIMITED", "ANDHRA BANK", "ANDHRA PRAGATHI GRAMEENA BANK", "APNA SAHAKARI BANK LIMITED", "AUSTRALIA AND NEW ZEALAND BANKING GROUP LIMITED", "AXIS BANK", "B N P PARIBAS", "BANDHAN BANK LIMITED", "BANK INTERNASIONAL INDONESIA", "BANK OF AMERICA", "BANK OF BAHARAIN AND KUWAIT BSC", "BANK OF BARODA", "BANK OF CEYLON", "BANK OF INDIA", "BANK OF MAHARASHTRA", "BANK OF TOKYO MITSUBISHI LIMITED", "BARCLAYS BANK", "BASSEIN CATHOLIC COOPERATIVE BANK LIMITED", "BHARAT COOPERATIVE BANK MUMBAI LIMITED", "CANARA BANK", "CAPITAL SMALL FINANCE BANK LIMITED", "CATHOLIC SYRIAN BANK LIMITED", "CENTRAL BANK OF INDIA", "CHINATRUST COMMERCIAL BANK LIMITED", "CITI BANK", "CITIZEN CREDIT COOPERATIVE BANK LIMITED", "CITY UNION BANK LIMITED", "COMMONWEALTH BANK OF AUSTRALIA", "CORPORATION BANK", "CREDIT AGRICOLE CORPORATE AND INVESTMENT BANK CALYON BANK", "CREDIT SUISEE AG", "DCB BANK LIMITED", "DENA BANK", "DEOGIRI NAGARI SAHAKARI BANK LTD. AURANGABAD", "DEPOSIT INSURANCE AND CREDIT GUARANTEE CORPORATION", "DEUSTCHE BANK", "DEVELOPMENT BANK OF SINGAPORE", "DHANALAKSHMI BANK", "DOHA BANK", "DOHA BANK QSC", "DOMBIVLI NAGARI SAHAKARI BANK LIMITED", "EQUITAS SMALL FINANCE BANK LIMITED", "ESAF SMALL FINANCE BANK LIMITED", "EXPORT IMPORT BANK OF INDIA", "FEDERAL BANK", "FIRSTRAND BANK LIMITED", "G P PARSIK BANK", "GURGAON GRAMIN BANK", "HDFC BANK", "HIMACHAL PRADESH STATE COOPERATIVE BANK LTD", "HSBC BANK", "HSBC BANK OMAN SAOG", "ICICI BANK LIMITED", "IDBI BANK", "IDFC BANK LIMITED", "IDUKKI DISTRICT CO OPERATIVE BANK LTD", "INDIAN BANK", "INDIAN OVERSEAS BANK", "INDUSIND BANK", "INDUSTRIAL AND COMMERCIAL BANK OF CHINA LIMITED", "INDUSTRIAL BANK OF KOREA", "JALGAON JANATA SAHAKARI BANK LIMITED", "JAMMU AND KASHMIR BANK LIMITED")

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_cheque_receipt, container, false)

        initPresenter()
        onAttach()

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProgress()
        SetCurrentDate()
        SetAutoTextBank()

        SetBottomSheetCalculate()

        txt_cheque_receipt_date.setOnClickListener {
            ShowDate(txt_cheque_receipt_date)
        }
        txt_cheque_receipt_cheque_date.setOnClickListener {
            ShowDate(txt_cheque_receipt_cheque_date)
        }
        btn_cheque_receipt_reset.setOnClickListener {
            Reset()
        }
        btn_cheque_receipt_calculate.setOnClickListener {
            ShowBottomSheetCalculate()
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun OnTransactionEvent(response1: EventChequeReceiptBaseResponse) {
        val response = response1.response

        for (i in 0..response.table!!.size - 1) {
            if (response.table[i].master.equals("Party")) {
                partyNameList.add(SpinnerData(response.table[i].xname, response.table[i].xcode))
            }
            if (response.table[i].master.equals("CHRATE")) {
                RateList.add(SpinnerData(response.table[i].xname, response.table[i].xcode))
            }
        }

        SetPartyAdapter()
        SetRateAdapter()
    }

    private fun Reset() {
        SetCurrentDate()
        SetAutoTextBank()
        spinner_party_name_cheque_receipt.setSelection(0,true)
        spinner_rate_cheque_receipt.setSelection(0,true)
        txt_cheque_receipt_cheque_no.setText("")
        txt_cheque_receipt_bank_name.setText("")
        edt_amount_cheque_receipt.setText("")
    }

    private fun ChangeReceiptDays() {
        txt_cheque_receipt_date.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                val cheque_date = txt_cheque_receipt_cheque_date.tag.toString()
                val receipt_date = txt_cheque_receipt_date.tag.toString()

                txt_cheque_receipt_days.text = "${getDaysBetweenDates(receipt_date, cheque_date, "yyyy-MM-dd")} ${" Days"}"
                txt_cheque_receipt_days.tag = getDaysBetweenDates(receipt_date, cheque_date, "yyyy-MM-dd")

                cheque_receipt_cheque_date.text = txt_cheque_receipt_date.getText().toString()
            }
        })
    }

    private fun ChangeChequeDays() {
        txt_cheque_receipt_cheque_date.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun afterTextChanged(editable: Editable) {
                val cheque_date = txt_cheque_receipt_cheque_date.tag.toString()
                val receipt_date = txt_cheque_receipt_date.tag.toString()

                txt_cheque_receipt_days.text = "${getDaysBetweenDates(receipt_date, cheque_date, "yyyy-MM-dd")} ${" Days"}"
                txt_cheque_receipt_days.tag = getDaysBetweenDates(receipt_date, cheque_date, "yyyy-MM-dd")

                cheque_receipt_date.text = txt_cheque_receipt_cheque_date.text.toString()
            }
        })
    }

    private fun SetCurrentDate() {
        txt_cheque_receipt_cheque_date.text = dateNow("dd/MM/yyyy")
        txt_cheque_receipt_cheque_date.tag = dateNow("yyyy-MM-dd")

        txt_cheque_receipt_date.text = dateNow("dd/MM/yyyy")
        txt_cheque_receipt_date.tag = dateNow("yyyy-MM-dd")

        val cheque_date = txt_cheque_receipt_cheque_date.tag.toString()
        val receipt_date = txt_cheque_receipt_date.tag.toString()

        txt_cheque_receipt_days.text = "${getDaysBetweenDates(receipt_date, cheque_date, "yyyy-MM-dd")} ${" Days"}"
        txt_cheque_receipt_days.tag = getDaysBetweenDates(receipt_date, cheque_date, "yyyy-MM-dd")

        cheque_receipt_cheque_date.text = dateNow("dd/MM/yyyy")
        cheque_receipt_date.text = dateNow("dd/MM/yyyy")

        ChangeReceiptDays()
        ChangeChequeDays()
    }

    private fun SetAutoTextBank() {
        val adapter = ArrayAdapter<String>(activity, R.layout.layout_autotextview, bank)
        txt_cheque_receipt_bank_name.threshold = 1
        txt_cheque_receipt_bank_name.setAdapter(adapter)

    }

    private fun initPresenter() {
        ChequeReceiptPresenter = ChequeReceiptPresenter()
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
        ChequeReceiptPresenter.onAttach(this)
    }

    private fun SetPartyAdapter() {
        spinner_party_name_cheque_receipt.adapter = SearchAdapter(activity, partyNameList)
        spinner_party_name_cheque_receipt.setTitle("Select Party")
    }

    private fun SetRateAdapter() {
        spinner_rate_cheque_receipt.adapter = SearchAdapter(activity, RateList)
        spinner_rate_cheque_receipt.setTitle("Select Rate")
    }

    override fun ShowProgress() {
        mProgressDialog?.show()
    }

    override fun HideProgress() {
        mProgressDialog?.dismiss()
    }

    override fun ShowBottomSheetProgress() {
        linearLayout_bottom_sheet.visibility = View.GONE
        progressBar_bottom_sheet.visibility = View.VISIBLE
    }

    override fun HideBottomSheetProgress() {
        linearLayout_bottom_sheet.visibility = View.VISIBLE
        progressBar_bottom_sheet.visibility = View.GONE
    }

    override fun ErrorAmountDialog(Title: String, Message: String) {

    }

    override fun ErrorSaveDialog(Title: String, Message: String) {

    }

    override fun ShowChequeReceiptAmountResponse(response: ChequeReceiptAmountResponse) {
        editText_interest.setText(String.format("%.2f", response.table!![0].column1))
        editText_net_amount.setText(String.format("%.2f", response.table!![0].column2))

        editText_payable.setText(String.format("%.2f", response.table!![0].column2))
        editText_adjustable.setText(GetAdjustable(String.format("%.2f", response.table!![0].column2), String.format("%.2f", response.table!![0].column2)))
    }

    override fun ShowChequeReceiptSaveResponse(response: CheckReceiptSaveResponse) {
        if (response.table!!.isNotEmpty()) {
            if (response.table[0].success == 1) {
                mBottomSheetDialog?.dismiss()
                activity.toast(response.table[0].message!!)
                Reset()
            } else {
                activity.toast(response.table[0].message!!)
            }
        } else {
            activity.toast("Something went wrong!")
        }
    }

    private fun ShowBottomSheetCalculate() {
        GetAmountInterest()
    }

    private fun SetBottomSheetCalculate() {
        mBottomSheetDialog = BottomSheetDialog(activity)
        val sheetView = this.layoutInflater.inflate(R.layout.layout_bottom_sheet_cheque_receipt, null)
        mBottomSheetDialog?.setContentView(sheetView)

        linearLayout_bottom_sheet = sheetView.findViewById(R.id.layout_cheque_receipt) as LinearLayout
        progressBar_bottom_sheet = sheetView.findViewById(R.id.layout_cheque_receipt_progressbar) as ProgressBar
        btn_save = sheetView.findViewById(R.id.btn_cheque_receipt_save) as Button
        editText_interest = sheetView.findViewById(R.id.txt_cheque_receipt_interest) as EditText
        editText_net_amount = sheetView.findViewById(R.id.txt_cheque_receipt_net_amount) as EditText
        editText_payable = sheetView.findViewById(R.id.txt_cheque_receipt_payable) as EditText
        editText_adjustable = sheetView.findViewById(R.id.txt_cheque_receipt_adjustable) as EditText

        editText_payable.addTextChangedListener(object : TextWatcher {


            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                //handler.removeCallbacks(runnable);
                editText_adjustable.setText(GetAdjustable(editText_net_amount.getText().toString(), charSequence.toString()))

            }

            override fun afterTextChanged(editable: Editable) {
                /*  runnable = new Runnable() {
                    @Override
                    public void run() {
                    }
                };
                handler.postDelayed(runnable, 500);*/
            }
        })

        btn_save.setOnClickListener { SaveChequeReceipt() }
    }

    private fun GetAmountInterest() {
        val amount = edt_amount_cheque_receipt.text.toString()
        val date1 = txt_cheque_receipt_date.tag.toString()
        val date2 = txt_cheque_receipt_cheque_date.tag.toString()
        val bankName = txt_cheque_receipt_bank_name.text.toString()
        val days = txt_cheque_receipt_days.tag.toString()
        val chequeNo = txt_cheque_receipt_cheque_no.text.toString()

        var validation: Boolean? = true
        if (amount.isEmpty()) {
            edt_amount_cheque_receipt.error = "Amount cannot be empty!"
            validation = false
        }
        if (chequeNo.isEmpty()) {
            txt_cheque_receipt_cheque_no.error = "Cheque No. cannot be empty!"
            validation = false
        }
        if (bankName.isEmpty()) {
            txt_cheque_receipt_bank_name.error = "Bank Name cannot be empty!"
            validation = false
        }
        if (validation!!) {
            mBottomSheetDialog?.show()
            ChequeReceiptPresenter.AmountCalc("amount", date1,
                    partyNameList[spinner_party_name_cheque_receipt.selectedItemPosition].xCode!!,
                    RateList[spinner_rate_cheque_receipt.selectedItemPosition].xCode!!,
                    bankName, date2, days, amount)
        }
    }

    private fun GetAdjustable(NetAmount: String, Payable: String): String {
        var netAmount: Float
        var payable: Float
        try {
            netAmount = java.lang.Float.parseFloat(NetAmount)
        } catch (e: Exception) {
            netAmount = 0.00f
        }

        try {
            payable = java.lang.Float.parseFloat(Payable)
        } catch (e: Exception) {
            payable = 0.00f
        }

        val adjustable = netAmount - payable
        return String.format("%.2f", adjustable)
    }

    fun getDaysBetweenDates(start: String, end: String, format: String): Long {
        val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
        val startDate: Date
        val endDate: Date
        var numberOfDays: Long = 0
        try {
            startDate = dateFormat.parse(start)
            endDate = dateFormat.parse(end)
            numberOfDays = getUnitBetweenDates(startDate, endDate, TimeUnit.DAYS)
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return numberOfDays
    }

    private fun ShowDate(textView: TextView) {
        val mYear: Int
        val mMonth: Int
        val mDay: Int
        val c = Calendar.getInstance()
        mYear = c.get(Calendar.YEAR)
        mMonth = c.get(Calendar.MONTH)
        mDay = c.get(Calendar.DAY_OF_MONTH)

        // Launch Date Picker Dialog
        val dpd = DatePickerDialog(activity,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    val month = monthOfYear + 1
                    var formattedMonth = "" + month
                    var formattedDayOfMonth = "" + dayOfMonth

                    if (month < 10) {

                        formattedMonth = "0" + month
                    }
                    if (dayOfMonth < 10) {

                        formattedDayOfMonth = "0" + dayOfMonth
                    }
                    textView.tag = year.toString() + "-" + formattedMonth + "-" + formattedDayOfMonth
                    textView.text = "$formattedDayOfMonth/$formattedMonth/$year"
                }, mYear, mMonth, mDay)
        dpd.show()
    }

    private fun getUnitBetweenDates(startDate: Date, endDate: Date, unit: TimeUnit): Long {
        val timeDiff = endDate.time - startDate.time
        return unit.convert(timeDiff, TimeUnit.MILLISECONDS)
    }

    private fun SaveChequeReceipt() {
        val srNo = ""
        val date1 = txt_cheque_receipt_date.getTag().toString()
        val partyName = partyNameList[spinner_party_name_cheque_receipt.selectedItemPosition].xCode!!
        val Rate = RateList[spinner_rate_cheque_receipt.selectedItemPosition].xCode!!
        val chequeNo = txt_cheque_receipt_cheque_no.text.toString()
        val bankName = txt_cheque_receipt_bank_name.text.toString()
        val date2 = txt_cheque_receipt_cheque_date.tag.toString()
        val days = txt_cheque_receipt_days.tag.toString()
        val amount = edt_amount_cheque_receipt.text.toString()
        val interest = editText_interest.text.toString()
        val netamt = editText_net_amount.text.toString()
        val adjamt = editText_adjustable.text.toString()
        val payable = editText_payable.text.toString()
        val userId = GetUserCode()!!
        val entrydatetime = ""
        val editedby = ""
        val editdatetime = ""
        val corpcentre = GetCorpCode()!!
        val unit_corp = CoreApp.instance?.getSessionManager()!!.branchcode
        val terminal = ""

        ChequeReceiptPresenter.SaveChequeReceipt(srNo, date1, partyName, Rate, chequeNo,
                bankName, date2, days, amount, interest, netamt, adjamt, payable, userId,
                entrydatetime, editedby, editdatetime, corpcentre, unit_corp, terminal)
    }
}