package com.vangood.projectchat

import android.content.Context
import android.content.SharedPreferences
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
import com.vangood.projectchat.databinding.FragmentHomeBinding
import java.net.URL
import kotlin.concurrent.thread

class HomeFragment: Fragment() {
    private val TAG = FragmentHomeBinding::class.java.simpleName
    lateinit var binding: FragmentHomeBinding
    val rooms = mutableListOf<Lightyear>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = requireContext().getSharedPreferences("check",Context.MODE_PRIVATE)

        if (pref.getBoolean("loginstate", false)) {
            binding.tvName.visibility = View.VISIBLE
            binding.tvName.setText(pref.getString("nickname",""))
        } else binding.tvName.visibility = View.GONE

        //RecyclerView's Adapter
        binding.recycler.setHasFixedSize(true)
        binding.recycler.layoutManager = GridLayoutManager(requireContext(),2)
        val adapter = ChatRoomAdapter()
        binding.recycler.adapter = adapter

        // chatroomlist
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
            val view = layoutInflater.inflate(
                R.layout.row_chatroom, parent, false)
            return ChatRoomViewHolder(view)
        }//return ?????? View

        override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
            val lightyear = rooms[position]
            holder.streamid.setText(lightyear.stream_id.toString())
            holder.tags.setText(lightyear.tags)
            holder.nickname.setText(lightyear.nickname)
            holder.title.setText(lightyear.stream_title)
            Glide.with(this@HomeFragment).load(lightyear.head_photo)
                .into(holder.headShot)
            holder.itemView.setOnClickListener {
                loadFragment(ChatRoomsFragment())
            }
        }//???????????????????????????????????????item???????????????

        override fun getItemCount(): Int {
             return rooms.size
        }//return ?????? int???????????????return????????????(array list lenth)

    }

    inner class ChatRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val streamid = view.findViewById<TextView>(R.id.stream_id)
        val tags = view.findViewById<TextView>(R.id.stream_tags)
        val nickname = view.findViewById<TextView>(R.id.nickname)
        val title = view.findViewById<TextView>(R.id.stream_title)
        val headShot = view.findViewById<ImageView>(R.id.head_shot)
    }
    private fun loadFragment(fragment: Fragment){
        val parentActivity =  requireActivity() as MainActivity
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
        parentActivity.binding.bottomNavBar.visibility = View.GONE

    }


}