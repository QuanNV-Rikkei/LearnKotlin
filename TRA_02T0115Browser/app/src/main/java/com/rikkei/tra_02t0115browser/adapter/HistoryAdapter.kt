package com.rikkei.tra_02t0115browser.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rikkei.tra_02t0115browser.R
import kotlinx.android.synthetic.main.item_history.view.*

class HistoryAdapter : RecyclerView.Adapter<HistoryAdapter.MyViewHolder>{

    private var context: Context
    private var listData : List<String>? = null

    constructor(context: Context, listData: List<String>?) {
        this.context = context
        this.listData = listData
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_history, parent, false))
    }

    override fun getItemCount(): Int {
        if (!listData.isNullOrEmpty()) {
            return listData!!.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (!listData.isNullOrEmpty()) {
            val item = listData!![position]
            if (!TextUtils.isEmpty(item)) {
                holder.tvItem.text = item
            }
            if (position + 1 == itemCount) {
                holder.divider.visibility = View.GONE
            } else {
                holder.divider.visibility = View.VISIBLE
            }
        }
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvItem = itemView.tv_item
        val divider = itemView.divider
    }
}