package com.yh.customviewexample.page

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import com.yh.customviewexample.R

class ScrollerActivity : AppCompatActivity() {

    companion object{
        var TAG="ScrollerActivity"
    }
    private lateinit var mLlContent:LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scroller)
        mLlContent=findViewById(R.id.ll_content)
    }

    fun scroll1(view: View) {
        Log.i(TAG,"view.left:"+view.left)
        mLlContent.scrollTo(-300,-500)
        view.invalidate()
    }

    fun scroll2(view: View) {
        Log.i(TAG,"view.left:"+view.left)

        mLlContent.scrollBy(-30,-50)

    }
}
