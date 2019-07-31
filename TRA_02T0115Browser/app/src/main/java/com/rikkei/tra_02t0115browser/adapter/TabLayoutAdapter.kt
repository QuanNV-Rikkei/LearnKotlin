package com.rikkei.tra_02t0115browser.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rikkei.tra_02t0115browser.R
import com.rikkei.tra_02t0115browser.model.TabItemModel
import kotlinx.android.synthetic.main.item_tab.view.*

class TabLayoutAdapter : RecyclerView.Adapter<TabLayoutAdapter.MyViewHolder> {
    private val context: Context
    private var listTab: MutableList<TabItemModel>? = mutableListOf()
    var listener: TabLayoutListener

    interface TabLayoutListener {
        fun onDeleteItemListener(position: Int)
        fun onAddItemListener(position: Int)
        fun onSelectedItemListener(position: Int)
    }

    constructor(context: Context, listTab: MutableList<TabItemModel>?, listener: TabLayoutListener) {
        this.context = context
        this.listTab = listTab
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_tab, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        if (!listTab.isNullOrEmpty()) {
            return listTab!!.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (!listTab.isNullOrEmpty()) {
            val item = listTab!!.get(position)
            if (!TextUtils.isEmpty(item.title)) {
                holder.tvTitle.setText(item.title)
            }

            if (position + 1 == itemCount) {
                holder.ivAdd.visibility = View.VISIBLE
            } else {
                holder.ivAdd.visibility = View.GONE
            }

            holder.ivAdd.setOnClickListener(View.OnClickListener {
                if (listener != null) {
                    listener.onAddItemListener(position + 1)
                }
                addItem(TabItemModel("New tab ${position + 1}"))
            })
            holder.ivClose.setOnClickListener(View.OnClickListener {
                if (listener != null) {
                    listener.onDeleteItemListener(position)
                }
                deleteItem(position)
            })
            holder.itemView.setOnClickListener(View.OnClickListener {
                selectedItem(position)
                if (listener != null) {
                    listener.onSelectedItemListener(position)
                }
            })
            if (item.isSelected) {
                holder.itemView.setBackgroundResource(android.R.color.white)
            } else {
                holder.itemView.setBackgroundResource(R.color.bg_tab_item)
            }
        }
    }

    private fun deleteItem(position: Int) {
        if (!listTab.isNullOrEmpty() && listTab!!.size > 1) {
            listTab!!.removeAt(position)
            if (position == itemCount) {
                selectedItem(position - 1)
            } else {
                selectedItem(position)
            }
            notifyDataSetChanged()
            Log.e("TabLayoutAdapter", "size: " + itemCount)
        }
    }

    private fun selectedItem(position: Int) {
        if (!listTab.isNullOrEmpty()) {
            for ((i, value) in listTab!!.withIndex()) {
                value.isSelected = position == i
            }
            notifyDataSetChanged()
        }
    }

    private fun addItem(item: TabItemModel) {
        listTab!!.add(item)
        selectedItem(itemCount - 1)
        notifyDataSetChanged()
        Log.e("TabLayoutAdapter", "size: " + itemCount)
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layoutTab = itemView.layoutTab
        val ivClose = itemView.ivClose
        val ivAdd = itemView.ivAdd
        val tvTitle = itemView.tvTitle
        val divider = itemView.divider
    }
}


