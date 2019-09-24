package com.yh.customviewexample

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.yh.customviewexample.page.RotationArrowActivity
import com.yh.customviewexample.page.TestActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun jumpRotationArrow(view: View) {
        var intent= Intent(this,RotationArrowActivity::class.java)
        startActivity(intent)
    }


    fun jumpTest(view: View) {
        var intent= Intent(this, TestActivity::class.java)
        startActivity(intent)
    }
}
