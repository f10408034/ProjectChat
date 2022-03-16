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

    fun loginFunc (account: String, password: String){
        if (account == "jack") {
            if (password == "1234") {
                Log.d(TAG, "Login success")
//                val nickname = pref.getString("nickname", "")
//                if (remember) {
//                    pref.edit()
//                        .putString("account", account)
//                        .apply()
//                }
//                    MainResultLuncher.launch(Intent(this, MainActivity::class.java))
//                    Toast.makeText(this, "welcome $nickname", Toast.LENGTH_LONG).show()
            } else {
//                AlertDialog.Builder(requireContext())
//                    .setTitle("Login")
//                    .setMessage("wrong password")
//                    .setPositiveButton("ok", null)
//                    .show()
            }
        } else {
//            AlertDialog.Builder(requireContext())
//                .setTitle("Login")
//                .setMessage("Login Failed")
//                .setPositiveButton("ok", null)
//                .show()
        }
    }
    
}