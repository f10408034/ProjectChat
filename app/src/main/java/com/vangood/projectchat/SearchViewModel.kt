package com.vangood.projectchat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URL


class SearchViewModel : ViewModel() {
    val chatRooms = MutableLiveData<List<Lightyear>>()
    val searchRooms = MutableLiveData<List<Lightyear>>()
    val rooms = mutableListOf<Lightyear>()

    var keysList = mutableListOf<String>()

    fun getAllRooms() {
        // viewModel裡專用的協程方法
        // 因為這次的耗時工作是「存取」網路資料，所以情境物件設定「IO」
        viewModelScope.launch(Dispatchers.IO) {
            // 先將回應的json轉成字串
            val json = URL("https://api.jsonserve.com/qHsaqy").readText()
            // 在這前提要先解析過一次他回應的 json結構
            // 並新增一個接收他 json結構的類別
            // 將 json字串建立成一個類別物件
            val response = Gson().fromJson(json, ChatRoomList::class.java)
            chatRooms.postValue(response.result.lightyear_list)
        }
    }

    fun getSearchRooms(keywords : String) {
        viewModelScope.launch(Dispatchers.IO) {
            val json = URL("https://api.jsonserve.com/qHsaqy").readText()
            val response = Gson().fromJson(json, ChatRoomList::class.java)
            val searchKeyMap = mutableMapOf<String, Lightyear>()
            response.result.lightyear_list.forEach {
                searchKeyMap.put(it.nickname, it)
                searchKeyMap.put(it.stream_title, it)
                keysList.add(it.nickname)
                keysList.add(it.stream_title)
            }
        }
//        searchRooms.postValue(resultRoomsSet.toList())
    }
}