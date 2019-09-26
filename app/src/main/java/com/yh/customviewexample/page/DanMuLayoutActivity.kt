package com.yh.customviewexample.page

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.yh.customviewexample.R
import com.yh.customviewexample.view.DanMuInfo
import com.yh.customviewexample.view.DanMuLayout

class DanMuLayoutActivity : AppCompatActivity() {
    lateinit var danmulayout:DanMuLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dan_mu_layout)

        danmulayout=findViewById(R.id.danmulayout)
        var danMuInfo1:DanMuInfo=DanMuInfo("aaa","like","yao")
        var danMuInfo2:DanMuInfo=DanMuInfo("aaa","event","yao")
        var danMuInfo3:DanMuInfo=DanMuInfo("aaa","like","yao")
        var danMuInfo4:DanMuInfo=DanMuInfo("aaa","like","yao")
        var danMuInfos= mutableListOf(danMuInfo1,danMuInfo2,danMuInfo3,danMuInfo4)
        danmulayout.addAllDanMu(danMuInfos)

        danmulayout.setRecycle(true)
    }
}
