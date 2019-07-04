package com.rikkei.myapplication.model

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.GsonBuilder

class SharedPreferencesUtils {

    private val SHARED_PREFERENCE_NAME: String = "sharepreference"
//    public val KEY = "key"

    companion object Key {
        val KEY_DATA: String = "key_data"
        val KEY_INDEX: String = "key_index"
    }

    var preferences: SharedPreferences? = null
    var gson: Gson? = null

    constructor(context: Context) {
        if (preferences == null && context != null) {
            preferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        }
        if (gson == null) {
            gson = GsonBuilder().create()
        }
    }

    fun writeStringPreference(key: String, value: String?) {
        if (preferences != null && !TextUtils.isEmpty(key)) {
            var editor: SharedPreferences.Editor = preferences!!.edit()
            editor.putString(key, value)
            editor.commit()
        }
    }

    fun readStringPreference(key: String, defaultValue: String): String {
        var result = ""
        if (preferences != null && !TextUtils.isEmpty(key)) {
            result = preferences!!.getString(key, defaultValue)
        }
        return result;
    }

    fun writeIntPreference(key: String, value: Int) {
        if (preferences != null && !TextUtils.isEmpty(key)) {
            var editor: SharedPreferences.Editor = preferences!!.edit()
            editor.putInt(key, value)
            editor.commit()
        }
    }

    fun readIntPreference(key: String, defaultValue: Int): Int {
        var result = 0
        if (preferences != null && !TextUtils.isEmpty(key)) {
            result = preferences!!.getInt(key, defaultValue)
        }
        return result
    }

    fun writeObjectToArrayUser(userModel: UserModel, index: Int) {
        if (userModel != null) {
            var strData: String = readStringPreference(KEY_DATA, "")
            var listUser: MutableList<UserModel>
            val outData: String
            if (!TextUtils.isEmpty(strData)) {
                listUser = gson!!.fromJson(strData, Array<UserModel>::class.java).toMutableList()
                listUser.add(userModel)
            } else {
                listUser = ArrayList()
                listUser.add(userModel)
            }
            outData = gson!!.toJson(listUser)
            writeStringPreference(KEY_DATA, outData)
            writeIntPreference(KEY_INDEX, index)
        }
    }

    fun deleteObjectFromArrayUser(id : Int) {
        var strData: String = readStringPreference(KEY_DATA, "")
        var listUser: MutableList<UserModel>
        if (!TextUtils.isEmpty(strData)) {
            listUser = gson!!.fromJson(strData, Array<UserModel>::class.java).toMutableList()
            for (user in listUser) {
                if (id == user.id) {
                    listUser.remove(user)
                    val outData = gson!!.toJson(listUser)
                    writeStringPreference(KEY_DATA, outData)
                    break
                }
            }

        }
    }

    fun updateObjectInArrayUser(userUpdate: UserModel) {
        if (userUpdate != null) {
            var strData: String = readStringPreference(KEY_DATA, "")
            var listUser: MutableList<UserModel>
            if (!TextUtils.isEmpty(strData)) {
                listUser = gson!!.fromJson(strData, Array<UserModel>::class.java).toMutableList()
                for (user in listUser) {
                    if (userUpdate.id == user.id) {
                        user.name = userUpdate.name
                        user.gender = userUpdate.gender
                        user.age = userUpdate.age
                        user.address = userUpdate.address
                        val outData = gson!!.toJson(listUser)
                        writeStringPreference(KEY_DATA, outData)
                        break
                    }
                }
            }
        }
    }

    fun getListUser(): List<UserModel>? {
        var listUser: List<UserModel>? = null
        var strData: String = readStringPreference(KEY_DATA, "")
        if (!TextUtils.isEmpty(strData)) {
            listUser = gson!!.fromJson(strData, Array<UserModel>::class.java).toList()
        }
        return listUser
    }


}