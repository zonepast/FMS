package com.prudent.fms.utils.TableView

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.codecrafters.tableview.TableHeaderAdapter

/**
 * Created by Dharmik Patel on 29-Jul-17.
 */
class CustomTableHeaderAdapter : TableHeaderAdapter {

    private val headers: MutableList<String>
    private var paddingLeft = 20
    private var paddingTop = 30
    private var paddingRight = 20
    private var paddingBottom = 30
    private var textSize = 14
    private var typeface = Typeface.BOLD
    private var textColor = 0x99000000.toInt()

    constructor(context: Context, headers: MutableList<String>) : super(context) {
        this.headers = headers
    }

    fun setPaddings(left: Int, top: Int, right: Int, bottom: Int) {
        paddingLeft = left
        paddingTop = top
        paddingRight = right
        paddingBottom = bottom
    }

    fun setPaddingLeft(paddingLeft: Int) {
        this.paddingLeft = paddingLeft
    }

    fun setPaddingTop(paddingTop: Int) {
        this.paddingTop = paddingTop
    }

    fun setPaddingRight(paddingRight: Int) {
        this.paddingRight = paddingRight
    }

    fun setPaddingBottom(paddingBottom: Int) {
        this.paddingBottom = paddingBottom
    }

    fun setTextSize(textSize: Int) {
        this.textSize = textSize
    }

    fun setTypeface(typeface: Int) {
        this.typeface = typeface
    }

    fun setTextColor(textColor: Int) {
        this.textColor = textColor
    }

    override fun getHeaderView(columnIndex: Int, parentView: ViewGroup): View {
        val textView = TextView(context)

        if (columnIndex < headers.size) {
            textView.text = headers[columnIndex]
        }

        textView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        textView.setTypeface(textView.typeface, typeface)
        textView.textSize = textSize.toFloat()
        textView.setTextColor(Color.WHITE)
        textView.setSingleLine()
        textView.setAllCaps(true)
        textView.ellipsize = TextUtils.TruncateAt.END

        return textView
    }
}
