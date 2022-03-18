package com.vangood.projectchat

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
    lateinit var binding: FragmentChatroomsBinding
    lateinit var websocket: WebSocket
    val viewModel by viewModels<ChatViewModel>()
    val chatrooms = mutableListOf<Message>()

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

        //websocket
        val client = OkHttpClient.Builder()
            .readTimeout(3, TimeUnit.SECONDS)
            .build()
        val request = Request.Builder()
            .url("wss://lott-dev.lottcube.asia/ws/chat/chat:app_test?nickname=liming")
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
                Log.d(TAG, ": onMessage $text");
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
        binding.chat.layoutManager =LinearLayoutManager(requireContext())
        val adapter = ChatRoomAdapter()
        binding.chat.adapter = adapter


//        //ViewModel
//        viewModel.chatRooms.observe(viewLifecycleOwner) { rooms ->
//           adapter.submitRooms(rooms)
//        }
//        viewModel.getAllChat()

        CoroutineScope(Dispatchers.IO)
            .launch {
                val json = URL("https://api.jsonserve.com/hQAtNk").readText()
                val msg = Gson().fromJson(json, ChatData::class.java)
                Log.d(TAG, "msg : ${msg.event}");
                //Toast.makeText(context, "msg", Toast.LENGTH_SHORT).show()
            }

        //test chatroomlist
//        thread {
//            val json = URL("https://api.jsonserve.com/hQAtNk").readText()
//            val chatRooms = Gson().fromJson(json,ChatRoomList::class.java)
//            Log.d(TAG, "content: ${chatRooms.result.message_list}")
//            //fill list with new coming data
//            chatrooms.clear()
//            chatrooms.addAll(chatRooms.result.message_list)
//            //List<Message>
//            activity?.runOnUiThread {
//                adapter.notifyDataSetChanged()
//            }
//        }

        binding.send.setOnClickListener {
            thread {
                val json = URL("https://api.jsonserve.com/hQAtNk").readText()
                val msg = Gson().fromJson(json, ChatData::class.java)
                Log.d(TAG, "msg : ${msg.body.text}");
                val message = binding.sendmessage.text.toString()
                websocket.send(Gson().toJson(Message("N", "464564564564564")))

            }
        }

        binding.bExit.setOnClickListener {
            loadFragment(HomeFragment())
        }
    }
    inner class ChatRoomAdapter : RecyclerView.Adapter<ChatRoomViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
            val view = layoutInflater.inflate(
                R.layout.row_chatmessage, parent, false)
            return ChatRoomViewHolder(view)
        }//return 一個 View

        override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
            val message = chatrooms[position]
            holder.userName.setText(message.action)
            holder.tv_message.setText(message.content)
        }//在這裡取得元件的控制（每個item內的控制）

        override fun getItemCount(): Int {
            return chatrooms.size
        }
//        fun submitRooms(rooms: List<ChatData>) {
//            chatrooms.clear()
//            chatrooms.addAll(rooms)
//            notifyDataSetChanged()
//        }
    }

    inner class ChatRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val userName = view.findViewById<TextView>(R.id.user_name)
        val tv_message = view.findViewById<TextView>(R.id.tv_message)
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    fun chatRoomClicked(message: Message) {
//        val action = SecondFragmentDirections.actionRoomListToSingleRoom(lightyear)
        val bundle = Bundle().apply {
            putParcelable("chateoom", message)
        }
    }
}