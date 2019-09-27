package com.yh.customviewexample.view

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import com.yh.customviewexample.R

class FailingBallView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var mContext:Context?

    private var mPaint:Paint
    private var bitmap:Bitmap

    private var centerX:Float=0f
    private var centerY:Float=0f

    private var mVelocityX:Float=0f
    private var mVelocityY:Float=0f

    private var mGestureDector:GestureDetector

    private var bitmapX:Float=0f
    private var bitmapY:Float=0f

    companion object{
        const val TAG="FailingBallView"
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX= (w/2).toFloat()
        centerY= (h/2).toFloat()
    }

    init {
        mContext=context

        mPaint= Paint(Paint.ANTI_ALIAS_FLAG)

        var options:BitmapFactory.Options=BitmapFactory.Options()
        options.inSampleSize=8
        bitmap=BitmapFactory.decodeResource(resources, R.drawable.ball,options)

        bitmapX=-bitmap.width/2.toFloat()
        bitmapY=-bitmap.height/2.toFloat()

        mGestureDector=GestureDetector(mContext,object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                changeBallLocAni(velocityX,velocityY)
                return false
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return mGestureDector.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.translate(centerX,centerY)
        bitmapX+=mVelocityX*0.05f
        bitmapY+=mVelocityY*0.05f

        canvas?.drawBitmap(bitmap,bitmapX,bitmapY,mPaint)
    }

    //改变小球位置的动画
    fun changeBallLocAni(velocityX: Float, velocityY: Float){
       val velocityXAni: ValueAnimator =ValueAnimator.ofFloat(velocityX,0.0f)
       val velocityYAni: ValueAnimator =ValueAnimator.ofFloat(velocityY,0.0f)

        velocityXAni.addUpdateListener (object :ValueAnimator.AnimatorUpdateListener{
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                mVelocityX= animation?.animatedValue as Float
                invalidate()
                Log.i(TAG,"mVelocityX:"+mVelocityX)
            }

        } )

        velocityYAni.addUpdateListener (object :ValueAnimator.AnimatorUpdateListener{
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                mVelocityY= animation?.animatedValue as Float
                Log.i(TAG,"mVelocityY:"+mVelocityY)            }

        } )

        //根据速度算出移动的位置

        var animator:AnimatorSet = AnimatorSet()
        animator.playTogether(velocityXAni,velocityYAni)
        animator.interpolator=LinearInterpolator()
        animator.duration=2000

        animator.start()

    }
}