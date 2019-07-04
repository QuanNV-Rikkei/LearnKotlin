package com.rikkei.myapplication.view

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rikkei.myapplication.R
import com.rikkei.myapplication.adapter.SpinnerAdapter
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
        setUpSpinner()
        btnConfirm.setOnClickListener { view ->
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    fun setUpSpinner() {
        val listSpin = ArrayList<String>()
        listSpin.add("Tìm kiếm theo độ tuổi")
        listSpin.add("0 - 15 tuổi")
        listSpin.add("16 - 39 tuổi")
        listSpin.add("40 - 59 tuổi")
        listSpin.add("60 - 100 tuổi")
        val spinAdapter = SpinnerAdapter(this, 0, listSpin)
        spnSearch.adapter = spinAdapter
        spnSearch.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                if (preference != null) {
                    listUserModel = preference!!.getListUser()
                }
                var listData: List<UserModel>? = null
                when (position) {
                    0 -> {
                        listData = listUserModel
                    }
                    1 -> {
                        listData =
                            listUserModel!!.filter { userModel -> (userModel.age!! > 0 && userModel.age!! <= 15) }
                    }
                    2 -> {
                        listData =
                            listUserModel!!.filter { userModel -> (userModel.age!! >= 16 && userModel.age!! <= 39) }
                    }
                    3 -> {
                        listData =
                            listUserModel!!.filter { userModel -> (userModel.age!! >= 40 && userModel.age!! <= 59) }
                    }
                    4 -> {
                        listData =
                            listUserModel!!.filter { userModel -> (userModel.age!! >= 60 && userModel.age!! <= 100) }
                    }
                    else -> {
                        listData = listUserModel
                    }
                }
                setUpListUser(listData)

            }

        }
    }

    private fun setUpListUser(listUserModel: List<UserModel>?) {
        if (listUserModel != null && !listUserModel.isEmpty()) {
            rvUser.visibility = View.VISIBLE
            tvEmpty.visibility = View.GONE
            if (adapter != null) {
                adapter!!.updateListData(listUserModel)
                return
            }
            adapter = UserAdapter(this, (listUserModel as ArrayList<UserModel>?)!!)
            adapter!!.listener = this

            rvUser.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            rvUser.addItemDecoration(DividerItemDecoration(this, VERTICAL))
            rvUser.adapter = adapter
        } else {
            rvUser.visibility = View.GONE
            tvEmpty.visibility = View.VISIBLE
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
            builder.setPositiveButton("Có", { dialog, which ->
                dialog.dismiss()
                if (preference != null) {
                    preference!!.deleteObjectFromArrayUser(userModel.id)
                }
                if (adapter != null) {
                    adapter!!.removeItem(userModel)
                }
            })

            builder.setNegativeButton("Không", { dialog, whichButton ->
                dialog.dismiss()
            })
            builder.show()
        }
    }


}
