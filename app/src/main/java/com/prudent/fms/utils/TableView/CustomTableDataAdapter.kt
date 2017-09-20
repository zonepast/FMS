package com.prudent.fms.utils.TableView

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.codecrafters.tableview.TableDataAdapter
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter

/**
 * Created by Dharmik Patel on 26-Jul-17.
 */
class CustomTableDataAdapter(context: Context, data: Array<Array<String?>>) : TableDataAdapter<Array<String?>>(context, data) {

    private var paddingLeft = 15
    private var paddingTop = 10
    private var paddingRight = 15
    private var paddingBottom = 10
    private var textSize = 12
    private var typeface = Typeface.NORMAL
    private var textColor = 0x99000000.toInt()


    override fun getCellView(rowIndex: Int, columnIndex: Int, parentView: ViewGroup): View {
        val textView = TextView(context)
        textView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom)
        textView.setTypeface(textView.typeface, typeface)
        textView.textSize = textSize.toFloat()
        textView.setTextColor(textColor)
        //textView.setSingleLine();
        //textView.setEllipsize(TextUtils.TruncateAt.END);

        try {
            val textToShow = getItem(rowIndex)!![columnIndex]
            textView.text = textToShow
        } catch (e: IndexOutOfBoundsException) {
            Log.w(LOG_TAG, "No Sting given for row " + rowIndex + ", column " + columnIndex + ". "
                    + "Caught exception: " + e.toString())
            // Show no text
        }

        return textView
    }

    /**
     * Sets the padding that will be used for all table cells.

     * @param left   The padding on the left side.
     * *
     * @param top    The padding on the top side.
     * *
     * @param right  The padding on the right side.
     * *
     * @param bottom The padding on the bottom side.
     */
    fun setPaddings(left: Int, top: Int, right: Int, bottom: Int) {
        paddingLeft = left
        paddingTop = top
        paddingRight = right
        paddingBottom = bottom
    }

    /**
     * Sets the padding that will be used on the left side for all table cells.

     * @param paddingLeft The padding on the left side.
     */
    fun setPaddingLeft(paddingLeft: Int) {
        this.paddingLeft = paddingLeft
    }

    /**
     * Sets the padding that will be used on the top side for all table cells.

     * @param paddingTop The padding on the top side.
     */
    fun setPaddingTop(paddingTop: Int) {
        this.paddingTop = paddingTop
    }

    /**
     * Sets the padding that will be used on the right side for all table cells.

     * @param paddingRight The padding on the right side.
     */
    fun setPaddingRight(paddingRight: Int) {
        this.paddingRight = paddingRight
    }

    /**
     * Sets the padding that will be used on the bottom side for all table cells.

     * @param paddingBottom The padding on the bottom side.
     */
    fun setPaddingBottom(paddingBottom: Int) {
        this.paddingBottom = paddingBottom
    }

    /**
     * Sets the text size that will be used for all table cells.

     * @param textSize The text size that shall be used.
     */
    fun setTextSize(textSize: Int) {
        this.textSize = textSize
    }

    /**
     * Sets the typeface that will be used for all table cells.

     * @param typeface The type face that shall be used.
     */
    fun setTypeface(typeface: Int) {
        this.typeface = typeface
    }

    /**
     * Sets the text color that will be used for all table cells.

     * @param textColor The text color that shall be used.
     */
    fun setTextColor(textColor: Int) {
        this.textColor = textColor
    }

    companion object {
        private val LOG_TAG = SimpleTableDataAdapter::class.java.name
    }

}
