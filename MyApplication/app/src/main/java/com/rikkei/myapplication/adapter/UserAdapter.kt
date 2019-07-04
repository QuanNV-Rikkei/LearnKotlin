package com.rikkei.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rikkei.myapplication.R
import com.rikkei.myapplication.model.UserModel
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter : RecyclerView.Adapter<UserAdapter.MyViewHolder> {

    var context: Context? = null
    var listUserModel: ArrayList<UserModel>? = null
    var listener: UserActionListener? = null

    public interface UserActionListener {
        fun onUpdateUser(userModel: UserModel)
        fun onDeleteUser(userModel: UserModel)
    }


    constructor(context: Context, listUserModel: ArrayList<UserModel>) {
        this.context = context
        this.listUserModel = listUserModel
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (listUserModel != null && !listUserModel!!.isEmpty()) {
            return listUserModel!!.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (listUserModel != null && !listUserModel!!.isEmpty()) {
            val user = listUserModel!!.get(position)
            holder.tvName.setText(user.name)
            holder.tvGender.setText(user.gender)
            holder.tvAddress.setText(user.address)
            holder.tvAge.setText("" + user.age)
            holder.btnUpdate.setOnClickListener(View.OnClickListener {
                if (listener != null) {
                    listener!!.onUpdateUser(user)
                }
            })
            holder.btnDelete.setOnClickListener(View.OnClickListener {
                if (listener != null) {
                    listener!!.onDeleteUser(user)
                }
            })
        }
    }

    fun removeItem(userModel: UserModel) {
        if (userModel != null && listUserModel != null && !listUserModel!!.isEmpty()) {
            listUserModel!!.remove(userModel)
            notifyDataSetChanged()
        }
    }

    fun updateListData(listData : List<UserModel>) {
        if (listData != null && !listData.isEmpty()
            && listUserModel != null && !listUserModel!!.isEmpty()) {
            listUserModel!!.clear()
            listUserModel!!.addAll(listData)
            notifyDataSetChanged()
        }
    }


    class MyViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val tvName: TextView = v.findViewById(R.id.tvName)
        val tvGender: TextView = v.findViewById(R.id.tvGender)
        val tvAddress: TextView = v.findViewById(R.id.tvAddress)
        val tvAge: TextView = v.findViewById(R.id.tvAge)
        val btnUpdate: Button = v.btnUpdate
        val btnDelete: Button = v.btnDelete

    }
}