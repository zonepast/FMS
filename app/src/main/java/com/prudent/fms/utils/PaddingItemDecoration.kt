package com.prudent.fms.utils

import android.app.Activity
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * Created by Dharmik Patel on 25-Jul-17.
 */
class PaddingItemDecoration(activity: Activity, private val mPaddingPx: Int, private val mPaddingEdgesPx: Int) : RecyclerView.ItemDecoration() {

    init {
        val resources = activity.resources
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)

        val itemPosition = parent.getChildAdapterPosition(view)
        if (itemPosition == RecyclerView.NO_POSITION) {
            return
        }
        val orientation = getOrientation(parent)
        val itemCount = state!!.itemCount

        var left = 0
        var top = 0
        var right = 0
        var bottom = 0

        /** HORIZONTAL  */
        if (orientation == LinearLayoutManager.HORIZONTAL) {
            /** all positions  */
            left = 0
            right = 0

            /** first position  */
            if (itemPosition == 0) {
                left += mPaddingEdgesPx
            } else if (itemCount > 0 && itemPosition == itemCount - 1) {
                right += mPaddingEdgesPx
            }
            /** last position  */
        } else {
            /** all positions  */
            top = mPaddingPx
            bottom = mPaddingPx

            /** first position  */
            if (itemPosition == 0) {
                top += mPaddingEdgesPx
            } else if (itemCount > 0 && itemPosition == itemCount - 1) {
                bottom += mPaddingEdgesPx
            }
            /** last position  */
        }
        /** VERTICAL  */

        if (!isReverseLayout(parent)) {
            outRect.set(left, top, right, bottom)
        } else {
            outRect.set(right, bottom, left, top)
        }
    }

    private fun isReverseLayout(parent: RecyclerView): Boolean {
        if (parent.layoutManager is LinearLayoutManager) {
            val layoutManager = parent.layoutManager as LinearLayoutManager
            return layoutManager.reverseLayout
        } else {
            throw IllegalStateException("PaddingItemDecoration can only be used with a LinearLayoutManager.")
        }
    }

    private fun getOrientation(parent: RecyclerView): Int {
        if (parent.layoutManager is LinearLayoutManager) {
            val layoutManager = parent.layoutManager as LinearLayoutManager
            return layoutManager.orientation
        } else {
            throw IllegalStateException("PaddingItemDecoration can only be used with a LinearLayoutManager.")
        }
    }
}
