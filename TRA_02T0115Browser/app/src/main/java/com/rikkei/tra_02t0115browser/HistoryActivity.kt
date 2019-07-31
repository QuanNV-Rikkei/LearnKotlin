package com.rikkei.tra_02t0115browser

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.rikkei.tra_02t0115browser.adapter.HistoryAdapter
import com.rikkei.tra_02t0115browser.data.AppPreferencesHelper

import kotlinx.android.synthetic.main.activity_history.*

val INTENT_BOOKMARK = "bookmark"
val INTENT_HISTORY = "history"

class HistoryActivity : AppCompatActivity() {

    private var isBookmark = false
    private var isHistory = false
    private lateinit var appPreferences: AppPreferencesHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        if (intent != null) {
            isBookmark = intent.getBooleanExtra(INTENT_BOOKMARK, false)
            isHistory = intent.getBooleanExtra(INTENT_HISTORY, false)
            if (isBookmark) {
                toolbar.title = "Bookmark browser"
            } else if (isHistory) {
                toolbar.title = "History browser"
            }
        }
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowTitleEnabled(true)
        }
        val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        appPreferences = AppPreferencesHelper(this, AppPreferencesHelper.PREF_NAME, gson)

        initView()
    }

    private fun initView() {
        if (isBookmark) {
            val listBookmark = appPreferences.getBookmark()
            setUpRecyclerView(listBookmark)
        } else if (isHistory) {
            val listHistory = appPreferences.getHistory()
            setUpRecyclerView(listHistory)
        }
    }

    private fun setUpRecyclerView(listData: List<String>?) {
        if (listData != null && !listData.isNullOrEmpty()) {
            rcvContent.visibility = View.VISIBLE
            tvMessage.visibility = View.GONE
            val adapter = HistoryAdapter(this, listData)
            rcvContent.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            rcvContent.adapter = adapter
        } else {
            rcvContent.visibility = View.GONE
            tvMessage.visibility = View.VISIBLE
        }
    }

}
