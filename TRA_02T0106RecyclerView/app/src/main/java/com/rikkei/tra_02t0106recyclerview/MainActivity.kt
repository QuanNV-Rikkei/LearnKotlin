package com.rikkei.tra_02t0106recyclerview

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {


    private var listData: ArrayList<DataModel> = ArrayList()
    private var adapter: RecyclerViewAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        btnLinear.setOnClickListener(this)
        btnGrid.setOnClickListener(this)
        btnStaggered.setOnClickListener(this)
        fakeData()
        setUpRecyclerView()

    }

    private fun fakeData(){
        listData.add(DataModel(R.drawable.hv1, "Image 1"))
        listData.add(DataModel(R.drawable.hv2, "Image 2"))
        listData.add(DataModel(R.drawable.hv3, "Image 3"))
        listData.add(DataModel(R.drawable.hv4, "Image 4"))
        listData.add(DataModel(R.drawable.hv5, "Image 5"))
        listData.add(DataModel(R.drawable.hv6, "Image 6"))
        listData.add(DataModel(R.drawable.hv1, "Image 7"))
    }

    private fun setUpRecyclerView() {
        adapter = RecyclerViewAdapter(this, listData)
        rcvView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        rcvView.adapter = adapter
    }



    override fun onClick(p0: View?) {
        when(p0!!.id) {
            R.id.btnLinear -> {
                rcvView.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            }

            R.id.btnGrid -> {
                rcvView.layoutManager = GridLayoutManager(this, 2, RecyclerView.VERTICAL, false)
            }

            R.id.btnStaggered -> {
                rcvView.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            }
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
}
