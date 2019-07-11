package com.rikkei.tra_02t0103intent_intentfilter

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_c.*

class CActivity : AppCompatActivity() {

    companion object {
        val KEY_DATA = "data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_c)
        btnNext.setOnClickListener(View.OnClickListener { view ->
            /* val intent = Intent(this, DActivity::class.java)
             intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
             startActivity(intent)*/

            val intent = Intent()
            intent.putExtra(KEY_DATA, "Return from C")
            setResult(Activity.RESULT_OK, intent)
            finish()
        })
    }
}
