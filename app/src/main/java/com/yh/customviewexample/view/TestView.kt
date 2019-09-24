package com.yh.customviewexample.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Path
import android.graphics.PathMeasure
import android.nfc.Tag
import android.util.AttributeSet
import android.util.Log
import android.view.View

class TestView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    var TAG:String="TestView"
    init {

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var path= Path()
        path.addRect(0f,0f,200f,200f,Path.Direction.CW)

        var pathMeasure:PathMeasure=PathMeasure()
        pathMeasure.setPath(path,false)
        var matrix:Matrix=Matrix()

        pathMeasure.getMatrix(400f,matrix,PathMeasure.TANGENT_MATRIX_FLAG or PathMeasure.POSITION_MATRIX_FLAG)
        Log.i(TAG,"matrix.value${matrix.toString()}")


        var matrix1:Matrix= Matrix()
        matrix1.postScale(0.5f,0.8f)
        matrix1.preTranslate(1000f,1000f)

        Log.i(TAG,"matrix1.value${matrix1.toString()}")

    }
}