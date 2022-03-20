package com.vangood.projectchat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class ChatViewModel : ViewModel() {
    val chatRooms = MutableLiveData<String>()

    fun getNickname(Text: String){
        chatRooms.postValue(Text)
    }
    fun getMessage(Text: String) {
        chatRooms.postValue(Text)
    }
}