package com.yh.customviewexample.view.flowlayout

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class MyFlowLayout(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthMode =MeasureSpec.getMode(widthMeasureSpec)
        var widthSize =MeasureSpec.getSize(widthMeasureSpec)

        var heightMode =MeasureSpec.getMode(heightMeasureSpec)
        var heightSize =MeasureSpec.getSize(heightMeasureSpec)

        //最终宽高
       var finalWidth=0
       var finalHeight=0

        //行宽和行高
       var lineWidth=0
       var lineHeight=0

       measureChildren(widthMeasureSpec,heightMeasureSpec)

        for (index in 0 until childCount) {
            var child = getChildAt(index)
            var params:MarginLayoutParams= child.layoutParams as MarginLayoutParams
            var childWidth=child.measuredWidth+params.leftMargin+params.rightMargin
            var childHeight=child.measuredHeight+params.topMargin+params.bottomMargin

            if (lineWidth+childWidth>widthSize){
                finalWidth=Math.max(finalWidth,lineWidth)
                finalHeight+=lineHeight

                lineWidth=childWidth
                lineHeight=childHeight
            }else{
                lineWidth+=childWidth
                lineHeight=Math.max(lineHeight,childHeight)
            }

            //最后一行需要单独处理
            if (index == childCount - 1) {
                finalWidth=Math.max(finalWidth,lineWidth)
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

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        var lineHeight=0
        var lineWidth=0

        var left=0
        var top=0

        for (index in 0 until childCount) {
            var child=getChildAt(index)
            var params:MarginLayoutParams= child.layoutParams as MarginLayoutParams
            var childWidth=child.measuredWidth+params.leftMargin+params.rightMargin
            var childHeight=child.measuredHeight+params.topMargin+params.bottomMargin

            if (lineWidth + childWidth > measuredWidth) {
                left=0
                top+=lineHeight

                lineWidth=childWidth
                lineHeight=childHeight
            }
            else{
                lineWidth+=childWidth
                lineHeight=Math.max(lineHeight,childHeight)
            }

            var childLeft=left+params.leftMargin
            var childRight=childLeft+child.measuredWidth
            var childTop=top+params.topMargin
            var childBottom=childTop+child.measuredHeight

            child.layout(childLeft,childTop,childRight,childBottom)

            left=lineWidth
        }
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context,attrs)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        super.dispatchDraw(canvas)
    }
}