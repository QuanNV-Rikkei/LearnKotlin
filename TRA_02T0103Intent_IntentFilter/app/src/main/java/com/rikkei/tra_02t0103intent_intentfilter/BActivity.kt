package com.rikkei.tra_02t0103intent_intentfilter

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_b.*

class BActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b)
        btnNext.setOnClickListener({ view ->
            val intent = Intent(this, CActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT)
            startActivity(intent)
        })
    }
}
