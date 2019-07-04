package com.rikkei.myapplication.view

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rikkei.myapplication.R
import com.rikkei.myapplication.adapter.UserAdapter
import com.rikkei.myapplication.model.SharedPreferencesUtils
import com.rikkei.myapplication.model.UserModel
import kotlinx.android.synthetic.main.activity_list_user.*

const val INTENT_DATA: String = "data"
const val INTENT_UPDATE: String = "update"

class ListUserActivity : AppCompatActivity(), UserAdapter.UserActionListener {

    var preference: SharedPreferencesUtils? = null
    var listUserModel: List<UserModel>? = null
    var adapter: UserAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Danh sách người dùng"
        preference = SharedPreferencesUtils(this)
        listUserModel = preference!!.getListUser()
        setUpListUser(listUserModel)
    }

    private fun setUpListUser(listUserModel: List<UserModel>?) {
        if (listUserModel != null && !listUserModel.isEmpty()) {
            adapter = UserAdapter(this, (listUserModel as ArrayList<UserModel>?)!!)
            adapter!!.listener = this
            rvUser.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            rvUser.addItemDecoration(DividerItemDecoration(this, VERTICAL))
            rvUser.adapter = adapter
        }
    }

    override fun onUpdateUser(userModel: UserModel) {
        if (userModel != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra(INTENT_UPDATE, true)
            intent.putExtra("data", userModel)
            startActivity(intent)
        }
    }

    override fun onDeleteUser(userModel: UserModel) {
        if (userModel != null) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Xóa dữ liệu!")
            builder.setMessage("Bạn có chắc chắn muốn xóa người dùng: " + userModel.name)
            builder.setPositiveButton("Có", {dialog, which ->
                dialog.dismiss()
                if (preference != null) {
                    preference!!.deleteObjectFromArrayUser(userModel.id)
                }
                if(adapter != null) {
                    adapter!!.removeItem(userModel)
                }
            })

            builder.setNegativeButton("Không", {dialog, whichButton ->
                dialog.dismiss()
            })
            builder.show()
        }
    }


}
