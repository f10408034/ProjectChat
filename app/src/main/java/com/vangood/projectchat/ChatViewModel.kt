package com.vangood.projectchat

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL

class ChatViewModel : ViewModel() {
    val chatRooms = MutableLiveData<List<ChatData>>()
    fun getAllChat() {
        viewModelScope.launch(Dispatchers.IO) {
            val json = URL("https://api.jsonserve.com/hQAtNk").readText()
            val response = Gson().fromJson(json, ChatRoomList::class.java)
            chatRooms.postValue(response.result.chatdata_list)
        }
    }
}