package com.vangood.projectchat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class LoginViewModel : ViewModel() {
    private val TAG = LoginViewModel::class.java.simpleName
    val remember = MutableLiveData<Boolean>()

    fun loginFunc (account: String, password: String, username: String, pass: String) :Boolean {
        if (account == username && password == pass) {
            Log.d(TAG, "Login success")
            return true
        } else return false
    }


}