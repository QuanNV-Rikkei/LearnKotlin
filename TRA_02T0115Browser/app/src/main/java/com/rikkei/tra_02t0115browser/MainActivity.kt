package com.rikkei.tra_02t0115browser

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.rikkei.tra_02t0115browser.adapter.TabLayoutAdapter
import com.rikkei.tra_02t0115browser.adapter.ViewPagerAdapter
import com.rikkei.tra_02t0115browser.data.AppPreferencesHelper
import com.rikkei.tra_02t0115browser.model.TabItemModel

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), TabLayoutAdapter.TabLayoutListener,
    WebFragment.OnFragmentInteractionListener {


    private val TAG = MainActivity::class.java.simpleName

    private var adapter: TabLayoutAdapter? = null
    private var viewPagerAdapter: ViewPagerAdapter? = null
    private lateinit var appPreferences: AppPreferencesHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fakeData()
        setUpViewPager()
        val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        appPreferences = AppPreferencesHelper(this, AppPreferencesHelper.PREF_NAME, gson)

        ivBookmark.setOnClickListener {
            actionSaveBookmark()
        }
        ivSetting.setOnClickListener {
            createPopupMenu()
        }
    }

    private fun createPopupMenu() {
        val popupMenu = PopupMenu(this, ivSetting)
        popupMenu.menu.add(0, 0, 0, "Bookmark")
        popupMenu.menu.add(0, 1, 1, "History")
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem? ->
            val intent = Intent(this, HistoryActivity::class.java)
            if (0 == menuItem!!.itemId) {
                intent.putExtra(INTENT_BOOKMARK, true)
            } else {
                intent.putExtra(INTENT_HISTORY, true)
            }
            startActivity(intent)
            true
        }
        popupMenu.show()
    }

    private fun actionSaveBookmark() {
        if (viewPagerAdapter != null) {
            val currentFragment = viewPagerAdapter!!.getItem(viewPager.currentItem)
            val strUrl = (currentFragment as WebFragment).getUrl()
            if (!TextUtils.isEmpty(strUrl)) {
                appPreferences.setBookmark(strUrl!!)
                Toast.makeText(this, "Lưu bookmark thành công !", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Bạn hãy nhập URL!", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun setUpViewPager() {
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter!!.addFragment(WebFragment(), "WebFragment")
        viewPager.adapter = viewPagerAdapter
    }

    private fun fakeData() {
        val listTab: MutableList<TabItemModel> = mutableListOf(TabItemModel("New tab", true))
        setUpRecyclerView(listTab)
    }

    private fun setUpRecyclerView(listTab: MutableList<TabItemModel>) {
        adapter = TabLayoutAdapter(this, listTab, this)
        rcvTab.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        rcvTab.adapter = adapter
    }

    override fun onDeleteItemListener(position: Int) {
        if (viewPagerAdapter != null) {
            Log.e(TAG, "Remove tab item at position: $position")
            viewPagerAdapter!!.removeFragment(position)
            viewPager.offscreenPageLimit = viewPagerAdapter!!.count
            if (position == viewPagerAdapter!!.count) {
                viewPager.currentItem = position - 1
            } else {
                viewPager.currentItem = position
            }
        }
    }

    override fun onAddItemListener(position: Int) {
        val strData = appPreferences.getBookmark()
        if (!strData.isNullOrEmpty()) {
            Log.e(TAG, "bookmark: " + strData.toString())
        }

        if (viewPagerAdapter != null) {
            Log.e(TAG, "Add tab item at position: $position")
            viewPagerAdapter!!.addFragment(WebFragment(), "WebFragment")
            viewPager.offscreenPageLimit = viewPagerAdapter!!.count
            viewPager.currentItem = position
        }
    }

    override fun onSelectedItemListener(position: Int) {
        viewPager.currentItem = position
    }

    override fun onFragmentInteraction() {
        Log.d(TAG, "Implement OnFragmentInteractionListener")
    }


}
