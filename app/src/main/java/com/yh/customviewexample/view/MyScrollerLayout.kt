package com.yh.customviewexample.view

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewConfigurationCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.Scroller

class MyScrollerLayout(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    private var mScroller: Scroller?=null

    private var minScrollSlop=0//判断滑动的最小距离

    var mLastX=0
    var mDownX=0

    var leftBorder=0
    var rightBorder=0

    init {
        mScroller=Scroller(context)

        var viewConfiguration=ViewConfiguration.get(context)
        minScrollSlop= ViewConfigurationCompat.getScaledPagingTouchSlop(viewConfiguration)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        when (ev?.action) {
            MotionEvent.ACTION_DOWN->
                mDownX= ev.rawX.toInt()
            MotionEvent.ACTION_MOVE->
            {
                var x=ev.rawX.toInt()
                var dx=Math.abs(x-mDownX)
                if (dx >= minScrollSlop) {
                    return true
                }
            }
        }
        return true;
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN->
            {
                mLastX= event.rawX.toInt()
            }
            MotionEvent.ACTION_MOVE->
            {
                var x=event.rawX.toInt()
                var scrollX=mLastX-x

                scrollBy(scrollX,0)
                mLastX= x
            }
        }
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        measureChildren(widthMeasureSpec,heightMeasureSpec)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (index in 0 until childCount) {
            var child: View =getChildAt(index)
            child.layout(index*child.measuredWidth,0,(index+1)*child.measuredWidth,child.measuredHeight)
        }

        leftBorder=getChildAt(0).left
        rightBorder=getChildAt(childCount-1).right
    }


}