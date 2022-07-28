package com.tanker.capitalwatersupplycustomer

import android.content.Context
import android.content.SharedPreferences

class Utils(context: Context) {
    var context = context;
    fun getPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences("tanker", Context.MODE_PRIVATE)
    }

    fun addData(key: String, value: String) {
        getPreference(context).edit().putString(key, value).apply()
    }

    fun getData(key: String): String {
        return getPreference(context).getString(key, "").toString()
    }

    fun logoutUser() {
        getPreference(context).edit().clear().apply()
    }
}