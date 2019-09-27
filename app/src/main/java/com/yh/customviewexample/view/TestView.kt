package com.yh.customviewexample.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Path
import android.graphics.PathMeasure
import android.nfc.Tag
import android.util.AttributeSet
import android.util.Log
import android.util.Log.i
import android.view.View
import java.util.*

class TestView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    var TAG:String="TestView"
    init {

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        var pts= floatArrayOf(80f,100f)

        var matrix= canvas?.getMatrix()

        matrix?.postScale(0.5f,1f)

        i(TAG,"matrix:"+matrix.toString())

        i(TAG,"before"+Arrays.toString(pts))

        matrix?.mapPoints(pts)

        i(TAG,"after"+Arrays.toString(pts))

        var radius:Float=50f
        //半径
        i(TAG,"before radius"+radius)

        radius= matrix?.mapRadius(radius)!!

        i(TAG,"after radius"+radius)

    }
}