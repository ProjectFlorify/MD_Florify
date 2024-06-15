package com.example.florify.preferences

import android.content.Context
import android.content.SharedPreferences

class PreferencesHelper(context: Context) {
    private val preferences: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun getSession(): String? {
        return preferences.getString("userid", null)
    }

    fun saveSession(userid: String) {
        val editor = preferences.edit()
        editor.putString("userid", userid)
        editor.apply()
    }

    fun removeSession() {
        val editor = preferences.edit()
        editor.remove("userid")
        editor.apply()
    }
}