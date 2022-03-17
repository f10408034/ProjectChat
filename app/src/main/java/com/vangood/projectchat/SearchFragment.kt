package com.vangood.projectchat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.vangood.projectchat.databinding.FragmentSearchBinding
import java.net.URL
import kotlin.concurrent.thread

class SearchFragment: Fragment() {
    private val TAG = SearchFragment::class.java.simpleName
    lateinit var binding: FragmentSearchBinding
    val rooms = mutableListOf<Lightyear>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.setHasFixedSize(true)
        binding.recycler.layoutManager = GridLayoutManager(requireContext(),2)
        var adapter = ChatRoomAdapter()
        binding.recycler.adapter = adapter
        thread {
            val json = URL("https://api.jsonserve.com/hQAtNk").readText()
            val msg = Gson().fromJson(json, ChatData::class.java)
        }
        thread {
            val json = URL("https://api.jsonserve.com/qHsaqy").readText()
            val chatRooms = Gson().fromJson(json,ChatRoomList::class.java)
            Log.d(TAG, "rooms: ${chatRooms.result.lightyear_list.size}")
            //fill list with new coming data
            rooms.clear()
            rooms.addAll(chatRooms.result.lightyear_list)
            //List<LightYear>
            activity?.runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }

    }

    inner class ChatRoomAdapter : RecyclerView.Adapter<ChatRoomViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
            val view = layoutInflater.inflate(R.layout.row_chatroom, parent, false)
            return ChatRoomViewHolder(view)
        }

        override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
            val lightyear = rooms[position]
            holder.host.setText(lightyear.nickname)
            holder.title.setText(lightyear.stream_title)
            Glide.with(this@SearchFragment).load(lightyear.head_photo)
                .into(holder.headShot)
            holder.itemView.setOnClickListener {
                loadFragment(ChatRoomsFragment())
            }
        }

        override fun getItemCount(): Int {
            return rooms.size
        }

    }

    inner class ChatRoomViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val host =  view.findViewById<TextView>(R.id.chatroom_host_name)
        val title = view.findViewById<TextView>(R.id.chatroom_title)
        val headShot = view.findViewById<ImageView>(R.id.head_shot)
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }
}