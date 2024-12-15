package com.example.recipesapp.network

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val USER_TOKEN = "user_token"
        private const val USER_LOGIN = "user_login"
    }

    fun saveAuthToken(token: String?) {
        prefs.edit().putString(USER_TOKEN, token).apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun clearAuthToken() {
        prefs.edit().remove(USER_TOKEN).apply()
    }

    fun saveLogin(login: String?) {
        prefs.edit().putString(USER_LOGIN, login).apply()
    }

    fun fetchLogin(): String? {
        return prefs.getString(USER_LOGIN, null)
    }
}