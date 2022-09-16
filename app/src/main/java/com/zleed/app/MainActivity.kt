package com.zleed.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val i1 = Intent()

        i1.setClass(applicationContext, ChannelActivity::class.java)
        startActivity(i1)
    }
}
