package com.rikkei.tra_02t0106recyclerview

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(context: Context?, listData: List<DataModel>?) :
    RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>() {

    private val context: Context?
    private val listData: List<DataModel>?

    init {
        this.context = context
        this.listData = listData
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (listData != null && !listData.isEmpty()) {
            return listData.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (listData != null && !listData.isEmpty()) {
            val item = listData.get(position);
            holder.ivItem.setImageResource(item.image)
            if (!TextUtils.isEmpty(item.name)) {
                holder.tvItem.setText(item.name)
            }
        }
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivItem = itemView.findViewById<ImageView>(R.id.ivItem)
        val tvItem = itemView.findViewById<TextView>(R.id.tvItem)
    }
}