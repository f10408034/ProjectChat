package com.vangood.projectchat

import androidx.lifecycle.ViewModel

class SignUpViewModel: ViewModel() {

    fun accountverify (loginId : String) : Boolean{
    //帳號至少6碼，第1碼為英文，
    if (loginId.length >= 4 && loginId.length <= 20) return true
        else return false
    }

    fun passwordverify (loginPw : String) : Boolean{
        if (loginPw.length >= 6 && loginPw.length <= 12) return true
        else return false
    }
}