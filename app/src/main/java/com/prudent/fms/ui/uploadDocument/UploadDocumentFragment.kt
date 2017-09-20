package com.prudent.fms.ui.uploadDocument


import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.RadioButton
import android.widget.RadioGroup
import com.codekidlabs.storagechooser.StorageChooser
import com.prudent.fms.R
import com.prudent.fms.data.api.model.uploadDocument.DetailUploadDocument.Response.DetailUploadDocumentResponse
import com.prudent.fms.data.api.model.uploadDocument.ShowDocument.Response.ShowDocumentResponse
import com.prudent.fms.data.api.model.uploadDocument.UploadDocument.Response.UploadDocumentResponse
import com.prudent.fms.data.model.SpinnerData
import com.prudent.fms.events.EventUploadDocumentBase
import com.prudent.fms.extensions.*
import com.prudent.fms.kotlin_extensions.alert
import com.prudent.fms.kotlin_extensions.progressDialog
import com.prudent.fms.kotlin_extensions.toast
import gun0912.tedbottompicker.TedBottomPicker
import kotlinx.android.synthetic.main.fragment_upload_document.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.*

class UploadDocumentFragment : Fragment(), UploadDocumentView {

    var mProgressDialog: ProgressDialog? = null
    lateinit var uploadDocumentPresenter: UploadDocumentPresenter

    private val transcationList = ArrayList<SpinnerData>()
    private val detailList = ArrayList<SpinnerData>()

    private var length: Long = 0
    private var strFileName = ""
    private var extension: String? = null
    private var file: File? = null
    private var chooser: StorageChooser? = null
    private var tedBottomPicker: TedBottomPicker? = null

    private val DIR = "FMS/DOCUMENT/PDF"
    private val IMG_DIR = "FMS/DOCUMENT/IMAGE"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_upload_document, container, false)

        initPresenter()
        onAttach()

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProgress()
        setRadioButton()

        txt_date_upload_document.text = dateNow("dd-MM-yyyy")
        txt_date_upload_document.setOnClickListener {
            ShowDatePicker(txt_date_upload_document, activity)
        }
        btn_browse_document.setOnClickListener {
            ShowAlertDialogWithListView()
        }
        btn_save_document.setOnClickListener {
            ValidationUpload()
        }
        btn_show_document.setOnClickListener {
            LoadShowDocument()
        }
        FileChooser()
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun OnTransactionEvent(response1: EventUploadDocumentBase) {
        val response = response1.response

        (0..response.table!!.size - 1).mapTo(transcationList) { SpinnerData(response.table!![it].xname, response.table!![it].xcode) }

        SetTransactionAdapter()
        spinner_transcation.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(adapter: AdapterView<*>, v: View,
                                        position: Int, id: Long) {
                LoadDetail()
            }

            override fun onNothingSelected(arg0: AdapterView<*>) {

            }
        }
    }

    private fun FileChooser() {
        tedBottomPicker = TedBottomPicker.Builder(activity)
                .setOnImageSelectedListener { uri ->
                    val path = uri.path
                    if (path == "") {

                    } else {
                        file = File(path)
                        strFileName = file!!.getName()
                        txt_file_name.setText(strFileName)
                        extension = file!!.absolutePath.substring(file!!.getAbsolutePath().lastIndexOf("."))
                        length = file!!.length()
                        length /= 1024
                    }
                }
                .create()
        chooser = StorageChooser.Builder()
                .withActivity(activity)
                .withFragmentManager(activity.supportFragmentManager)
                .allowCustomPath(true)
                .setType(StorageChooser.FILE_PICKER)
                .build()
        chooser?.onSelectListener = StorageChooser.OnSelectListener { path ->
            if (path == "") {

            } else {
                file = File(path)
                strFileName = file!!.getName()
                txt_file_name.setText(strFileName)
                extension = file!!.absolutePath.substring(file!!.absolutePath.lastIndexOf("."))
                length = file!!.length()
                length /= 1024
            }
        }
    }

    fun ShowAlertDialogWithListView() {
        val mAnimals = java.util.ArrayList<String>()
        mAnimals.add("Image")
        mAnimals.add("Document")
        val Animals = mAnimals.toTypedArray()
        val dialogBuilder = AlertDialog.Builder(activity)
        dialogBuilder.setTitle("Select Type")
        dialogBuilder.setItems(Animals) { dialog, item ->
            if (item == 0) {
                tedBottomPicker?.show(activity.supportFragmentManager)
            }
            if (item == 1) {
                chooser?.show()
            }
        }
        val alertDialogObject = dialogBuilder.create()
        alertDialogObject.show()
    }

    private fun setRadioButton() {
        radioGroup_upload_type.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            val selectedId = radioGroup_upload_type.checkedRadioButtonId
            val radioButton = activity.findViewById(selectedId) as RadioButton
            if (radioButton.text == "Upload") {
                btn_show_document.visibility = View.GONE
                line.visibility = View.GONE
            } else {
                line.visibility = View.VISIBLE
                btn_show_document.visibility = View.VISIBLE
            }
            LoadDetail()
        })
    }

    private fun initPresenter() {
        uploadDocumentPresenter = UploadDocumentPresenter()
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
        uploadDocumentPresenter.onAttach(this)
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
                LoadDetail()
            }
        }
        if (ref == 2) {
            dialog.positiveButton("RETRY") {
                SaveDocument()
            }
        }
        if (ref == 3) {
            dialog.positiveButton("RETRY") {
                LoadShowDocument()
            }
        }
        dialog.show()
    }

    private fun SetTransactionAdapter() {
        spinner_transcation.adapter = SearchAdapter(activity, transcationList)
        spinner_transcation.setTitle("Select Transaction")
    }

    private fun SetDetailAdapter() {
        spinner_detail.adapter = SearchAdapter(activity, detailList)
        spinner_detail.setTitle("Select Detail")
    }

    private fun LoadDetail() {
        val radio: String = if (radio_Upload.isChecked) {
            "Upload"
        } else {
            "Update"
        }
        uploadDocumentPresenter.LoadDetailTransaction(radio, GetCorpCode()!!, GetUserCode()!!,
                transcationList[spinner_transcation.selectedItemPosition].xCode!!)
    }

    override fun ShowTransactionDetailResponse(response: DetailUploadDocumentResponse) {
        if (detailList.size > 0) {
            detailList.clear()
        }
        (0..response.table!!.size - 1).mapTo(detailList) { SpinnerData(response.table!![it].xname, response.table!![it].xcode) }
        SetDetailAdapter()
    }

    override fun ShowShowDocumentResponse(response: ShowDocumentResponse) {
        if (response.table!!.isEmpty()) {

        } else {
            val base64 = response.table!![0].docValue
            val name = response.table!![0].docName
            val type = response.table!![0].docExt
            if (base64 == null) {
                activity.toast("No Documents Available...!")
            } else if (type == null) {
                try {
                    ConvertToPdf(base64, name!!)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            } else if (type == ".jpg" || type == ".png" || type == ".jpeg") {
                OpenImage(base64,name!!)
            } else if (type == ".pdf") {
                try {
                    ConvertToPdf(base64, name!!)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            } else {
                try {
                    ConvertToPdf(base64, name!!)
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }

    override fun UploadDocumentResponse(response: UploadDocumentResponse) {
        if (response.table!![0].success == 1) {
            val message = response.table!![0].messsage
            activity.toast(message!!)
            // Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show()
            //startActivity(Intent(getApplicationContext(), Das::class.java))
        } else {
            val message = response.table!![0].messsage
            activity.toast(message!!)
        }
    }

    private fun ValidationUpload() {
        if (strFileName == "") {
            activity.toast("Please, Select image or pdf Document")
        } else {
            if (extension == ".pdf" || extension == ".png" || extension == ".jpg"
                    || extension == ".gif" || extension == ".jpeg") {
                SaveDocument()
            } else {
                activity.toast("Please, Select file in image or pdf Document")
            }
        }
    }

    private fun SaveDocument() {
        val date = ConvertDateFormat(txt_date_upload_document.text.toString(), "dd-MM-yyyy", "yyyy-MM-dd")
        val remark = edt_remark_upload_document.getText().toString()
        val base64 = getStringFile(file!!)
        val filesize = java.lang.Long.toString(length)
        uploadDocumentPresenter.UploadDocument(transcationList[spinner_transcation.selectedItemPosition].xCode!!,
                detailList[spinner_detail.selectedItemPosition].xCode!!, date!!, remark, base64,
                strFileName, extension!!, filesize, GetCorpCode()!!, GetUserCode()!!,
                dateNow("yyyy-MM-dd"))
    }

    fun getStringFile(f: File): String {
        var byteArray: ByteArray? = null
        try {
            val inputStream = FileInputStream(f)
            val bos = ByteArrayOutputStream()
            val b = ByteArray(1024 * 11)
            var byteCount = 0
            while (byteCount != -1) {
                bos.write(b, 0, byteCount)
                byteCount = inputStream.read(b)
            }

            byteArray = bos.toByteArray()

        } catch (e: IOException) {
            e.printStackTrace()
        }

        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    private fun LoadShowDocument() {
        uploadDocumentPresenter.ShowDocument(GetCorpCode()!!, GetUserCode()!!, transcationList[spinner_transcation.selectedItemPosition].xCode!!,
                detailList[spinner_detail.selectedItemPosition].xCode!!)
    }

    /* Pdf */

    @Throws(IOException::class)
    private fun ConvertToPdf(base64: String, filename: String) {
        getFileFromBase64AndSaveInSDCard(base64, filename)
        OpenPdf(filename)
    }

    fun getFileFromBase64AndSaveInSDCard(base64: String, filename: String): GetFilePathAndStatus {
        val getFilePathAndStatus = GetFilePathAndStatus()
        try {
            val pdfAsBytes = Base64.decode(base64, Base64.NO_WRAP)
            val os: FileOutputStream
            os = FileOutputStream(getReportPath(filename))
            os.write(pdfAsBytes)
            os.flush()
            os.close()
            getFilePathAndStatus.filStatus = true
            getFilePathAndStatus.filePath = getReportPath(filename)
            return getFilePathAndStatus
        } catch (e: IOException) {
            e.printStackTrace()
            getFilePathAndStatus.filStatus = false
            getFilePathAndStatus.filePath = getReportPath(filename)
            return getFilePathAndStatus
        }

    }

    fun getReportPath(filename: String): String {
        val file = File(Environment.getExternalStorageDirectory().path, DIR)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file.absolutePath + "/" + filename
    }

    private fun OpenPdf(filename: String) {
        val pdfFile = File(Environment.getExternalStorageDirectory().toString() + "/" + DIR, filename)//File path
        if (pdfFile.exists()) {
            val path = Uri.fromFile(pdfFile)
            val target = Intent(Intent.ACTION_VIEW)
            target.setDataAndType(path, "application/pdf")
            target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY

            val intent = Intent.createChooser(target, "Open File")
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                activity.toast("Please , Install Atleast one Pdf Viewer to Open Pdf")
            }
        } else {
            activity.toast("The file not exists! ")
        }
    }

    class GetFilePathAndStatus {
        var filStatus: Boolean = false
        var filePath: String? = null
    }

    private fun OpenImage(base64: String, filename: String) {
        val imgBytesData = android.util.Base64.decode(base64,
                android.util.Base64.DEFAULT)
        val bitmap = BitmapFactory.decodeByteArray(imgBytesData, 0, imgBytesData.size)
        val myDir = File(Environment.getExternalStorageDirectory().path, IMG_DIR);
        if (!myDir.exists()) {
            myDir.mkdirs()
        }
        val fileopen = File(myDir.absolutePath + "/" + filename + "." + "jpg")
        if (fileopen.exists()) {
            fileopen.delete()
        }
        try {
            val out = FileOutputStream(myDir.absolutePath + "/" + filename + "." + "jpg");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush()
            out.close()

        } catch (e: Exception) {
            //  e.printStackTrace();
        }

        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(Uri.fromFile(fileopen), "image/*")
        startActivity(intent)
    }
}
