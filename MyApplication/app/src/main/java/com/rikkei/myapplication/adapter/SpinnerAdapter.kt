package com.rikkei.myapplication.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.rikkei.myapplication.R

class SpinnerAdapter(context: Context?, resource: Int, objects: MutableList<String>?) :
    ArrayAdapter<String>(context, resource, objects) {
    val listData : MutableList<String>?
    init {
        listData = objects;
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return getCustomView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getCustomView(position, convertView, parent)
    }

    fun getCustomView(position: Int, convertView: View?, parent: ViewGroup?) : View {
        val infalte:LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val row: View = infalte.inflate(R.layout.item_spinner, parent, false)
        val label: TextView = row.findViewById(R.id.tvItem)
        if (listData != null && !listData.isEmpty()) {
            label.setText(listData.get(position))
        }
        return row;
    }

}