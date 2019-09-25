package com.yh.customviewexample.view

import android.animation.Animator
import android.animation.Animator.*
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View

class SearchView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    var mPaint:Paint

    var centerX:Int=0
    var centerY:Int=0

    var curValue:Float=0f
    var curInValue:Float=0f
    var pos:FloatArray

    init {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.style=Paint.Style.STROKE
        mPaint.strokeWidth=10f
        mPaint.strokeCap=Paint.Cap.ROUND

        pos=FloatArray(2)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX=w/2
        centerY=h/2
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawColor(Color.BLUE)
        canvas?.translate(centerX.toFloat(), centerY.toFloat())

        var pathMeasure:PathMeasure= PathMeasure()

        // 内部path(包括内部圆和延伸出去的线段)
        var inPath:Path=Path()
        var inPathStartPos=FloatArray(2)
        inPath.addArc(-50f,-50f,50f,50f,50f,359.9f)
        pathMeasure.getPosTan(0f,inPathStartPos,FloatArray(2))

        // 外部path
        var outPath:Path=Path()
        var outPathStartPos=FloatArray(2)
        outPath.addArc(-100f,-100f,100f,100f,50f,-359.9f)
        pathMeasure.setPath(outPath,false)
        pathMeasure.getPosTan(0f,outPathStartPos,FloatArray(2))

        inPath.lineTo(outPathStartPos[0],outPathStartPos[1])

        //绘制内部动画
        var intDst=Path()
        pathMeasure.setPath(inPath,false)
        pathMeasure.getSegment(pathMeasure.length*curInValue,pathMeasure.length,intDst,true)

        var outDst=Path()
        pathMeasure.setPath(outPath,false)
        if (curValue <= 0.5f) {
            pathMeasure.getSegment(curValue*pathMeasure.length,curValue*pathMeasure.length+curValue*150f,outDst,true)
        }else{
            pathMeasure.getSegment(curValue*pathMeasure.length,curValue*pathMeasure.length+(1-curValue)*150f,outDst,true)
        }

        mPaint.color= Color.WHITE
        canvas?.drawPath(outDst,mPaint)
        canvas?.drawPath(intDst,mPaint)


        //正在搜索的动画
        if (!isJinxin) {
            startInAnimation(0f,1f)
        }

    }

    var isJinxin:Boolean=false
    private fun startOutAnimation() {
        var valueAnimator:ValueAnimator=ValueAnimator.ofFloat(0f,1f )
        valueAnimator.duration=2000
        valueAnimator.addUpdateListener (object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                curValue= animation?.animatedValue as Float
                Log.d("TAG", "进度:$curValue")

                invalidate()
            }

        })
        valueAnimator.addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                startInAnimation(1f,0f)
            }

        })
        valueAnimator.repeatCount=5
        valueAnimator.repeatMode=ValueAnimator.RESTART
        valueAnimator.start()

    }

    private fun startInAnimation(startValue:Float,endValue:Float) {
        isJinxin=true
        var valueAnimator:ValueAnimator=ValueAnimator.ofFloat(startValue,endValue )
        valueAnimator.duration=2000
        valueAnimator.addUpdateListener (object : ValueAnimator.AnimatorUpdateListener {
            override fun onAnimationUpdate(animation: ValueAnimator?) {
                curInValue= animation?.animatedValue as Float
                Log.d("TAG", "进度:$curInValue")

                invalidate()
            }

        })
        valueAnimator.addListener(object: AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                if (startValue == 0f) {
                    startOutAnimation()
                }else{
                    startInAnimation(0f,1f)
                }

            }

        })
        valueAnimator.start()

    }
}