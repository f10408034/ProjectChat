package com.vangood.projectchat

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.vangood.projectchat.databinding.FragmentChatroomsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okio.ByteString
import java.net.URL
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

class ChatRoomsFragment : Fragment() {
    private val TAG = FragmentChatroomsBinding::class.java.simpleName
    private lateinit var adapter: ChatRoomAdapter

    lateinit var binding: FragmentChatroomsBinding
    lateinit var websocket: WebSocket
    val viewModel by viewModels<ChatViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentChatroomsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // media
        var mediaController = MediaController(requireContext())
        mediaController.setAnchorView(binding.videoView)
        val offlineUri = Uri.parse("android.resource://${requireActivity().packageName}/${R.raw.hime3}")
        binding.videoView.setMediaController(mediaController)
        binding.videoView.setVideoURI(offlineUri)
        binding.videoView.requestFocus()
        binding.videoView.start()

        val pref = requireContext().getSharedPreferences("check", Context.MODE_PRIVATE)
        val user : String
        if (pref.getBoolean("loginstate",false) == false) {
            user = "Guess"
        } else {
            user = pref.getString("nickname","").toString()
        }


        //websocket
        val client = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url("wss://lott-dev.lottcube.asia/ws/chat/chat:app_test?nickname=$user")
            .build()

        websocket = client.newWebSocket(request, object : WebSocketListener(){
            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
            }
            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
            }
            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                val json = text
                val chatData = Gson().fromJson(json,ChatData::class.java)
                if (chatData.event == "default_message") {
                    viewModel.getMessage("${chatData.body.nickname} : ${chatData.body.text}")
                }
            }
            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                super.onMessage(webSocket, bytes)
            }

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
            }
        })

        //RecyclerView's Adapter
        binding.chat.setHasFixedSize(true)
        binding.chat.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,true)
        adapter = ChatRoomAdapter()
        binding.chat.adapter = adapter
        viewModel.message.observe(viewLifecycleOwner) { rooms ->
            adapter.submitRooms(rooms)
        }

        binding.send.setOnClickListener {
            thread {
                val json = URL("https://api.jsonserve.com/hQAtNk").readText()
                val msg = Gson().fromJson(json, ChatData::class.java)
                val message = binding.sendmessage.text.toString()
                websocket.send(Gson().toJson(Message("N", message)))
                binding.sendmessage.setText("")
            }
        }
        binding.exit.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Leave")
                .setMessage("Are you sure you want to leave")
                .setPositiveButton("Exit",{ d , w ->
                    loadFragment(HomeFragment())
                })
                .show()
        }
    }

    inner class ChatRoomAdapter : RecyclerView.Adapter<ChatRoomViewHolder>(){
        val chatRooms = mutableListOf<String>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
            val view = layoutInflater.inflate(
                R.layout.row_chatmessage, parent, false)
            return ChatRoomViewHolder(view)
        }//return 一個 View

        override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
            val message = chatRooms[position]
            holder.tv_message.setText(message)
        }//在這裡取得元件的控制（每個item內的控制）

        override fun getItemCount(): Int {
            return chatRooms.size
        }
        fun submitRooms(message : String) {
            chatRooms.add(0,message)
            notifyDataSetChanged()
        }
    }

    inner class ChatRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tv_message = view.findViewById<TextView>(R.id.tv_message)
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

}