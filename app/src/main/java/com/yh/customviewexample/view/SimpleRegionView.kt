package com.yh.customviewexample.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.Toast

class SimpleRegionView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    var mPaint:Paint

    lateinit var circlePath:Path
    lateinit var circleRegion:Region
    init {
        mPaint= Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint.color= Color.RED

        circlePath = Path()
        circleRegion= Region()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        circlePath.addCircle((w / 2).toFloat(), (h / 2).toFloat(), 300f, Path.Direction.CW)

        var clipRegion: Region = Region(0,0,w,h)

        circleRegion.setPath(circlePath,clipRegion)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        var path: Path = circlePath

        canvas?.drawPath(path, mPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_UP ->
                if (circleRegion.contains(event.x.toInt(), event.y.toInt())) {
                    Toast.makeText(context,"点击了区域",Toast.LENGTH_LONG).show()
                }
        }
        return true
    }
}