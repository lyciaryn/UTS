package com.example.uts.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager190(context: Context) {
    private val pref190: SharedPreferences = context.getSharedPreferences(PREF_NAME_190, Context.MODE_PRIVATE)
    private val editor190: SharedPreferences.Editor = pref190.edit()

    companion object {
        private const val PREF_NAME_190 = "user_session_190"
        private const val KEY_IS_LOGGED_IN_190 = "is_logged_in_190"
        private const val KEY_USER_ID_190 = "user_id_190"
        private const val KEY_USERNAME_190 = "username_190"
    }

    fun createSession190(userId: Int, username: String) {
        editor190.putBoolean(KEY_IS_LOGGED_IN_190, true)
        editor190.putInt(KEY_USER_ID_190, userId)
        editor190.putString(KEY_USERNAME_190, username)
        editor190.apply()
    }

    fun isLoggedIn190(): Boolean {
        return pref190.getBoolean(KEY_IS_LOGGED_IN_190, false)
    }

    fun getUserId190(): Int {
        return pref190.getInt(KEY_USER_ID_190, -1)
    }

    fun getUsername190(): String {
        return pref190.getString(KEY_USERNAME_190, "") ?: ""
    }

    fun logoutUser190() {
        editor190.clear()
        editor190.apply()
    }
}
