package com.rikkei.myapplication.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.rikkei.myapplication.R
import com.rikkei.myapplication.model.SharedPreferencesUtils
import com.rikkei.myapplication.model.UserModel

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var preference: SharedPreferencesUtils? = null
    var index = 0
    var userModel: UserModel? = null
    var isUpdate: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        preference = SharedPreferencesUtils(this)
        index = preference!!.readIntPreference(SharedPreferencesUtils.KEY_INDEX, 0)

        Log.d("MainActivity", "index: " + index)

        btnConfirm.setOnClickListener(View.OnClickListener { addUserToList() })

        initData()

    }

    fun initData() {
        if (intent != null) {
            userModel = if (intent.getSerializableExtra(INTENT_DATA) != null) {
                intent.getSerializableExtra(INTENT_DATA) as UserModel
            } else {
                null
            }
            isUpdate = intent.getBooleanExtra(INTENT_UPDATE, false)
        }
        if (supportActionBar != null) {
            if (isUpdate) {
                supportActionBar!!.title = "Cập nhật thông tin người dùng"
                if (userModel != null) {
                    updateViewFromData(userModel!!)
                }
            } else {
                supportActionBar!!.title = "Nhập thông tin người dùng"
            }
        }
    }

    fun updateViewFromData(userModel: UserModel) {
        if (userModel != null) {
            tvName.setText(userModel.name)
            tvGender.setText(userModel.gender)
            tvAge.setText(userModel.age.toString())
            tvAddress.setText(userModel.address)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addUserToList() {
        val name = tvName.text.toString().trim()
        val gender = tvGender.text.toString().trim()
        var age = 0
        if (!TextUtils.isEmpty(tvAge.text.toString().trim())) {
            age = tvAge.text.toString().trim().toInt()
        }

        val address = tvAddress.text.toString().trim()

        if (!TextUtils.isEmpty(name)) {
            val userModel: UserModel
            if (preference != null) {
                if (isUpdate) {
                    userModel = UserModel(this.userModel!!.id, name, gender, age, address)
                    preference!!.updateObjectInArrayUser(userModel)
                } else {
                    userModel = UserModel(index + 1, name, gender, age, address)
                    preference!!.writeObjectToArrayUser(userModel, index + 1)
                }
            }
        } else {
            Toast.makeText(this, "Bạn cần nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
        }
        startActivity(Intent(this, ListUserActivity::class.java))
    }
}
