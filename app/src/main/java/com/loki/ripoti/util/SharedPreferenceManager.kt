package com.loki.ripoti.util

import android.content.Context

object SharedPreferenceManager {

    fun saveAccessToken(context: Context?, token: String) {

        val sharedPreferences = context?.getSharedPreferences(Constants.TOKEN_KEY, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        editor?.putString(Constants.ACCESS_TOKEN, token)
        editor?.apply()

    }

     fun getToken(context: Context?) : String {

        val shared = context?.getSharedPreferences(Constants.TOKEN_KEY, Context.MODE_PRIVATE)
        val initialToken = shared?.getString(Constants.ACCESS_TOKEN, "")

        return initialToken!!
    }
}