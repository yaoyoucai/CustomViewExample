package com.yh.customviewexample.view.flowlayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup

class FlowLayout(context: Context?, attrs: AttributeSet?) : ViewGroup(context, attrs) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthSize=MeasureSpec.getSize(widthMeasureSpec)
        var widthMode=MeasureSpec.getMode(widthMeasureSpec)
        var heightSize=MeasureSpec.getSize(heightMeasureSpec)
        var heightMode=MeasureSpec.getMode(heightMeasureSpec)

        var width:Int=0
        var height:Int=0

        var lineWidth:Int=0
        var lineheight:Int=0

        measureChildren(widthMeasureSpec,heightMeasureSpec)

        for (index in 0 until childCount) {
            var child :View=getChildAt(index)

            var params:MarginLayoutParams= child.layoutParams as MarginLayoutParams
            var childHeight=child.measuredHeight+params.topMargin+params.bottomMargin
            var childWidth=child.measuredWidth+params.leftMargin+params.rightMargin

            if (lineWidth + childWidth>widthSize) {
                width=Math.max(lineWidth,width)
                height+=lineheight

                lineWidth=childWidth
                lineheight=childHeight
            }else{
                lineWidth+=childWidth
                lineheight=Math.max(lineheight,childHeight)
            }

            if (index == childCount - 1) {
                width=Math.max(lineWidth,width)
                height+=lineheight
            }

        }

        if (widthMode == MeasureSpec.EXACTLY) {
            width=widthSize
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height=heightSize
        }

         setMeasuredDimension(width,height)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        var left=0
        var top=0

        var lineHeight=0
        var lineWidth=0

        for (index in 0 until childCount) {
            var child=getChildAt(index)

            var params:MarginLayoutParams= child.layoutParams as MarginLayoutParams
            var childHeight=child.measuredHeight+params.topMargin+params.bottomMargin
            var childWidth=child.measuredWidth+params.leftMargin+params.rightMargin

            if (lineWidth + childWidth <= measuredWidth) {
                lineWidth+= childWidth
                lineHeight=Math.max(lineHeight,childHeight)
            }else{
                left=0
                top+=lineHeight

                lineWidth=childWidth
                lineHeight=childHeight
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
}