package com.rikkei.tra_02t0115browser.data

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AppPreferencesHelper {

    companion object{
        val PREF_NAME = "AppBrowser"
    }

    private val PREF_KEY_BOOKMARK = "PREF_KEY_BOOKMARK"
    private val PREF_KEY_HISTORY = "PREF_KEY_HISTORY"

    private lateinit var preferences: SharedPreferences
    private lateinit var gson: Gson

    constructor(context: Context, prefName: String, gson: Gson) {
        preferences = context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
        this.gson = gson
    }

    fun setBookmark(bookmark: String) {
        if (!TextUtils.isEmpty(bookmark)) {
            var data: MutableList<String>
            val strData = preferences.getString(PREF_KEY_BOOKMARK, "")
            data = if (!TextUtils.isEmpty(strData)) {
                gson.fromJson(strData, object : TypeToken<MutableList<String>>() {}.type)
            } else {
                mutableListOf()
            }
            data.add(bookmark)
            preferences.edit().putString(PREF_KEY_BOOKMARK, gson.toJson(data)).apply()
        }
    }

    fun getBookmark(): List<String>? {
        var data: List<String>? = null
        val strData = preferences.getString(PREF_KEY_BOOKMARK, "")
        if (!TextUtils.isEmpty(strData)) {
            data = gson.fromJson(strData, object : TypeToken<List<String>>() {}.type)
        }
        return data
    }

    fun setHistory(history: String) {
        if (!TextUtils.isEmpty(history)) {
            var data: MutableList<String>
            val strData = preferences.getString(PREF_KEY_HISTORY, "")
            data = if (!TextUtils.isEmpty(strData)) {
                gson.fromJson(strData, object : TypeToken<MutableList<String>>() {}.type)
            } else {
                mutableListOf()
            }
            data.add(history)
            preferences.edit().putString(PREF_KEY_HISTORY, gson.toJson(data)).apply()
        }
    }

    fun getHistory(): List<String>? {
        var data: List<String>? = null
        val strData = preferences.getString(PREF_KEY_HISTORY, "")
        if (!TextUtils.isEmpty(strData)) {
            data = gson.fromJson(strData, object : TypeToken<List<String>>() {}.type)
        }
        return data
    }


}