package com.loki.ripoti.util

import android.content.Context
import com.auth0.android.jwt.JWT

object GetUserInitials {

     fun initials(context: Context? = null, username: String? = null): String {

        val nameArr =
            if (context == null) getUserName(username) else getUserNameContext(context)

        var firstName = ""
        var secondName = ""
        for (i in nameArr) {
            firstName = nameArr[0]
            secondName = nameArr[1]
        }

        val c1: Char = firstName[0]
        val c2: Char = secondName[0]
        return c1.toString() + c2.toString()
    }

    private fun getUserName (username: String?): List<String> {
        return username?.split(" ") ?: emptyList()
    }

    private fun getUserNameContext(context: Context?): List<String> {
        val token = SharedPreferenceManager.getToken(context)
        val jwt = JWT(token)

        val localUserName = jwt.getClaim("name").asString()!!

        return localUserName.split(" ")
    }

}