package com.yh.customviewexample.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.yh.customviewexample.R

class RotationArrowView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    val paint:Paint
    var centerX:Float=0f
    var centerY:Float=0f
    var radius:Float=200f

    var curValue:Float=0f

    var bitmap:Bitmap
    var pos:FloatArray
    var tan:FloatArray

    init {
        paint=Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color=Color.RED
        paint.style=Paint.Style.STROKE

        var options:BitmapFactory.Options=BitmapFactory.Options()
        options.inSampleSize=8
        bitmap=BitmapFactory.decodeResource(resources, R.drawable.arrow,options)

        pos=FloatArray(2)
        tan=FloatArray(2)

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        centerX= (w/2).toFloat()
        centerY= (h/2).toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (curValue >= 1f) {
            curValue=0f
        }else {
            curValue+=0.005f
        }

        canvas?.translate(centerX, centerY)

        var path=Path()
        path.addCircle(0f,0f,radius,Path.Direction.CW)

        canvas?.drawPath(path, paint)


        //获取当前位置的坐标值

        var pathMeasure:PathMeasure=PathMeasure(path,false)
        pathMeasure.getPosTan(pathMeasure.length*curValue,pos,tan)

        var matrix:Matrix=Matrix()

        var degrees:Float= ((Math.atan2(tan[1].toDouble(), tan[0].toDouble())*180)/Math.PI).toFloat()

        matrix.postRotate(degrees, (bitmap.width/2).toFloat(), (bitmap.height/2).toFloat())
        matrix.postTranslate(pos[0]-bitmap.width/2,pos[1]-bitmap.height/2)

        canvas?.drawBitmap(bitmap,matrix,paint)

        invalidate()
    }
}