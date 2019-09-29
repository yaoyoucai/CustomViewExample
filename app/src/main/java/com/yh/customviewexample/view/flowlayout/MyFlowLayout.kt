package com.yh.customviewexample.view.flowlayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

/**
 * 我自己写的flowLayout
 */
class MyFlowLayout(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {
    //控件算出的最终宽高
    var finalWidth:Int=0
    var finalHeight:Int=0

    //当前的行宽和行高
    var lineWidth:Int=0
    var lineHeight:Int=0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMode= MeasureSpec.getMode(widthMeasureSpec)
        var widthSize= MeasureSpec.getSize(widthMeasureSpec)

        var heightMode= MeasureSpec.getMode(heightMeasureSpec)
        var heightSize= MeasureSpec.getSize(heightMeasureSpec)

        measureChildren(widthMeasureSpec,heightMeasureSpec)

        //onmeasure方法可能执行多次，因此需要重新初始化宽高
        lineWidth = 0
        lineHeight = 0
        finalHeight = 0
        finalWidth = 0

        for (index in 0 until childCount){
            var child=getChildAt(index)
            var childparams :MarginLayoutParams = child.layoutParams as MarginLayoutParams
            var childWidth=child.measuredWidth+childparams.leftMargin+childparams.rightMargin
            var childHeight=child.measuredHeight+childparams.topMargin+childparams.bottomMargin

            if (lineWidth+childWidth<=widthSize){
                lineWidth+=childWidth
                lineHeight=Math.max(lineHeight,childHeight)
            }else{
                finalWidth=Math.max(lineWidth,finalWidth)
                finalHeight+=lineHeight

                lineWidth=childWidth
                lineHeight=childHeight
            }

            //记录最后一行
            if (index==childCount-1){
                finalWidth=Math.max(lineWidth,finalWidth)
                finalHeight+=lineHeight
            }

        }

        if (widthMode == MeasureSpec.EXACTLY) {
            finalWidth=widthSize
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            finalHeight=heightSize
        }
        setMeasuredDimension(finalWidth,finalHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left:Int=0
        var top:Int=0
        for (index in 0 until childCount) {
            var child=getChildAt(index)
            var childparams :MarginLayoutParams = child.layoutParams as MarginLayoutParams

            child.layout(left+childparams.leftMargin,top+childparams.topMargin,left+childparams.leftMargin+ child.measuredWidth,top+childparams.topMargin+child.measuredHeight)

            if (left+childparams.leftMargin + child.measuredWidth+childparams.rightMargin>width){
                left=0
                top+=childparams.topMargin+child.measuredHeight+childparams.bottomMargin
            }else{
                left +=(childparams.leftMargin + child.measuredWidth+childparams.rightMargin)
            }



        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return  MarginLayoutParams(context,attrs)
    }
}