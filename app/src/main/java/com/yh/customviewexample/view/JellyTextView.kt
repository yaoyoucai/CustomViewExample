package com.yh.customviewexample.view

import android.content.Context
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.animation.BounceInterpolator
import android.widget.OverScroller
import android.widget.TextView

class JellyTextView(context: Context?, attrs: AttributeSet?) : TextView(context, attrs) {

    companion object{
        var TAG="JellyTextView"
    }

    var mScroller:OverScroller?=null
    init {
        mScroller=OverScroller(context,BounceInterpolator())
    }

    //手指上一次的触摸点
    var LastX=0
    var LastY=0

    var startX=0
    var startY=0


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        startX= x.toInt()
        startY= y.toInt()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN->
            {
                LastX= event.rawX.toInt()
                LastY= event.rawY.toInt()
            }

            MotionEvent.ACTION_MOVE->
            {
                var x= event.rawX.toInt()
                var y= event.rawY.toInt()

                var offsetX=x-LastX
                var offsetY=y-LastY

//                translationX+= (offsetX).toFloat()
//                translationY+= (offsetY).toFloat()
                ViewCompat.offsetLeftAndRight(this,offsetX)
                ViewCompat.offsetTopAndBottom(this,offsetY)

                LastX=x
                LastY=y
            }

            MotionEvent.ACTION_UP->
            {
                mScroller?.startScroll(x.toInt(), y.toInt(), (startX-x).toInt(), (startY-y).toInt())
                invalidate()
            }
        }
        return true
    }

    override fun computeScroll() {
        if (mScroller?.computeScrollOffset()!!) {
            x= mScroller!!.currX.toFloat()
            y= mScroller!!.currY.toFloat()
            invalidate()
        }else{
            Log.i(TAG,"final left:"+left+"final translationX"+translationX)
        }
    }
}