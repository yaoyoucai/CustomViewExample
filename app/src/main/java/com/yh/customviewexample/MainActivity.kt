package com.yh.customviewexample

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.yh.customviewexample.page.*

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


    fun jumpSearch(view: View) {
        var intent= Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }


    fun jumpDanmu(view: View) {
        var intent= Intent(this, DanMuLayoutActivity::class.java)
        startActivity(intent)
    }

    fun jumpFallingBall(view: View) {
        var intent= Intent(this, FailingBallActivity::class.java)
        startActivity(intent)
    }

    fun jumpScroller(view: View) {
        var intent= Intent(this, ScrollerActivity::class.java)
        startActivity(intent)
    }

    fun jumpJelly(view: View) {
        var intent= Intent(this, JellyTextActivity::class.java)
        startActivity(intent)
    }
}
