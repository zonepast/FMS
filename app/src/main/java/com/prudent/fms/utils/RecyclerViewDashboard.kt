package com.prudent.fms.utils

import android.content.Context
import android.graphics.Canvas
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.View

/**
 * Created by Dharmik Patel on 25-Jul-17.
 */
class RecyclerViewDashboard : RecyclerView {

    private var mScalePivot = SCALE_PIVOT_BOTTOM
    private var mSelectedScale = 0.8f

    private val viewLocation = IntArray(2)

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun drawChild(canvas: Canvas?, child: View?, drawingTime: Long): Boolean {
        /* Center Zoom out */
        //        if (mSelectedScale != 1.f) {
        //            int childWidth = child.getWidth();
        //            int childHeight = child.getHeight();
        //
        //            final int center = getCenterOfGallery();
        //            final int childCenter = child.getLeft() + childWidth / 2;
        //            final int offsetCenter = Math.abs(center - childCenter);
        //            final float offsetScale = (childWidth - offsetCenter) * -1.f / childWidth;
        //            if (offsetCenter < childWidth) {
        //                if (mSelectedScale != 1.f) {
        //                    float pivotY = 0;
        //                    if (mScalePivot == SCALE_PIVOT_CENTER) {
        //                        pivotY = (childHeight + child.getPaddingTop() - child.getPaddingBottom()) / 2;
        //                    } else if (mScalePivot == SCALE_PIVOT_TOP) {
        //                        pivotY = child.getPaddingTop();
        //                    } else if (mScalePivot == SCALE_PIVOT_BOTTOM) {
        //                        pivotY = childHeight - child.getPaddingBottom();
        //                    }
        //                    float scale = 1 + (mSelectedScale - 1) * offsetScale;
        //                    child.setPivotX(childWidth / 2.f);
        //                    child.setPivotY(pivotY);
        //                    child.setScaleX(scale);
        //                    child.setScaleY(scale);
        //                }
        //            } else {
        //                if (mSelectedScale != 1.f) {
        //                    child.setScaleX(1.f);
        //                    child.setScaleY(1.f);
        //                }
        //            }
        //        }

        /* Center Zoom in */
        // use this
        //        child.getLocationInWindow(viewLocation);
        //        int x = viewLocation[0];
        // or this is ok
        val x = child!!.left

        // view's width and height
        val vWidth = child.width - child.paddingLeft - child.paddingRight
        val vHeight = child.height - child.paddingTop - child.paddingBottom
        // device's width
        val dWidth = resources.displayMetrics.widthPixels

        if (vWidth >= dWidth) {
            // Do nothing if imageView's width is bigger than device's width.
            Log.d(TAG, "return")
            return super.drawChild(canvas, child, drawingTime)
        }

        val scale: Float
        val pivot = (dWidth - vWidth) / 2
        if (x <= pivot) {
            scale = 2f * (1 - mSelectedScale) * (x + vWidth).toFloat() / (dWidth + vWidth) + mSelectedScale
        } else {
            scale = 2f * (1 - mSelectedScale) * (dWidth - x).toFloat() / (dWidth + vWidth) + mSelectedScale
        }

        child.pivotX = (vWidth / 2).toFloat()
        child.pivotY = (vHeight / 2).toFloat()
        child.scaleX = scale
        child.scaleY = scale

        return super.drawChild(canvas, child, drawingTime)
    }

    /**
     * @return The center of this Gallery.
     */
    protected val centerOfGallery: Int
        get() {
            val paddingLeft = paddingLeft
            return (width - paddingLeft - paddingRight) / 2 + paddingLeft
        }

    /**
     * 设置选中的 Item 缩放的比例。

     * @param scale 缩放的比例
     */
    fun setSelectedScale(scale: Float) {
        this.mSelectedScale = scale
        invalidate()
    }

    /**
     * 设置缩放的中心点位置。

     * @param pivot 缩放的中心点位置
     * *
     * @see RecyclerGallery.SCALE_PIVOT_CENTER

     * @see RecyclerGallery.SCALE_PIVOT_TOP

     * @see RecyclerGallery.SCALE_PIVOT_BOTTOM
     */
    fun setScalePivot(pivot: Int) {
        if (pivot != SCALE_PIVOT_BOTTOM && pivot != SCALE_PIVOT_CENTER && pivot != SCALE_PIVOT_TOP) {
            throw RuntimeException("The scale pivot must be one of SCALE_PIVOT_BOTTOM、" + "SCALE_PIVOT_CENTER or SCALE_PIVOT_TOP")
        }

        this.mScalePivot = pivot
        invalidate()
    }

    companion object {

        private val TAG = RecyclerViewDashboard::class.java.simpleName

        val SCALE_PIVOT_CENTER = 0
        val SCALE_PIVOT_TOP = 1
        val SCALE_PIVOT_BOTTOM = 2
    }
}
