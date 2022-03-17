package com.vangood.projectchat

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class LoginViewModel : ViewModel() {
    private val TAG = LoginViewModel::class.java.simpleName
    val login = MutableLiveData<Boolean>()

    fun loginFunc (account: String, password: String, username: String, pass: String) :Boolean {
        if (account == username && password == pass) {
            Log.d(TAG, "Login success")
            return true
        } else return false
    }


}