package com.vangood.projectchat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.vangood.projectchat.databinding.FragmentSearchBinding
import com.vangood.projectchat.databinding.RowSearchroomBinding
import java.net.URL
import kotlin.concurrent.thread

class SearchFragment : Fragment() {
    companion object{
        val TAG = SearchFragment::class.java.simpleName
        val instance : SearchFragment by lazy {
            SearchFragment()
        }
    }
    lateinit var binding : FragmentSearchBinding
    lateinit var adapter : SearchRoomAdapter
    lateinit var popularadapter : ChatRoomAdapter
    val viewModel by viewModels<SearchViewModel>()
    val rooms = mutableListOf<Lightyear>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchRecycler.setHasFixedSize(true)
        binding.searchRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        adapter = SearchRoomAdapter()
        binding.searchRecycler.adapter = adapter

        viewModel.searchRooms.observe(viewLifecycleOwner) { rooms ->
            adapter.submitRooms(rooms)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val key = binding.searchView.query.toString()
                viewModel.getSearchRooms(key)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val key = binding.searchView.query.toString()
                viewModel.getSearchRooms(key)
                return false
            }

        })
        binding.popularRecycler.setHasFixedSize(true)
        binding.popularRecycler.layoutManager = GridLayoutManager(requireContext(), 2)
        popularadapter = ChatRoomAdapter()
        binding.popularRecycler.adapter = popularadapter

        //test chatroomlist
        thread {
            val json = URL("https://api.jsonserve.com/qHsaqy").readText()
            val chatRooms = Gson().fromJson(json,ChatRoomList::class.java)
            Log.d(TAG, "rooms: ${chatRooms.result.lightyear_list.size}")
            //fill list with new coming data
            rooms.clear()
            rooms.addAll(chatRooms.result.lightyear_list)
            //List<LightYear>
            activity?.runOnUiThread {
                popularadapter.notifyDataSetChanged()
            }
        }
    }

    inner class SearchRoomAdapter : RecyclerView.Adapter<SearchViewHolder>() {
        val searchRooms = mutableListOf<Lightyear>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
            val binding = RowSearchroomBinding.inflate(layoutInflater, parent, false)
            return SearchViewHolder(binding)
        }

        override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
            val lightYearSearch = searchRooms[position]
            holder.streamid.setText(lightYearSearch.stream_id.toString())
            holder.streamtags.setText(lightYearSearch.tags)
            holder.nickname.setText(lightYearSearch.nickname)
            holder.title.setText(lightYearSearch.stream_title)
            Glide.with(this@SearchFragment).load(lightYearSearch.head_photo)
                .into(holder.headPhoto)
            holder.itemView.setOnClickListener {
                loadFragment(ChatRoomsFragment())
            }
        }
        override fun getItemCount(): Int {
            return searchRooms.size
        }
        fun submitRooms(rooms: List<Lightyear>) {
            searchRooms.clear()
            Log.d(TAG, "submitRooms: ${searchRooms.size}")
            searchRooms.addAll(rooms)
            Log.d(TAG, "submitRooms: ${searchRooms.size}")
            notifyDataSetChanged()
        }
    }

    inner class SearchViewHolder(val binding: RowSearchroomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val streamid = binding.streamId
        val streamtags = binding.streamTags
        val nickname = binding.nickname
        val title = binding.streamTitle
        val headPhoto = binding.headShot
    }

    inner class ChatRoomAdapter : RecyclerView.Adapter<ChatRoomViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatRoomViewHolder {
            val view = layoutInflater.inflate(
                R.layout.row_chatroom, parent, false)
            return ChatRoomViewHolder(view)
        }//return 一個 View

        override fun onBindViewHolder(holder: ChatRoomViewHolder, position: Int) {
            val lightyear = rooms[position]
            holder.streamid.setText(lightyear.stream_id.toString())
            holder.tags.setText(lightyear.tags)
            holder.nickname.setText(lightyear.nickname)
            holder.title.setText(lightyear.stream_title)
            Glide.with(this@SearchFragment).load(lightyear.head_photo)
                .into(holder.headShot)
            holder.itemView.setOnClickListener {
                loadFragment(ChatRoomsFragment())
            }
        }//在這裡取得元件的控制（每個item內的控制）

        override fun getItemCount(): Int {
            return rooms.size
        }//return 一個 int，通常都會return陣列長度(array list lenth)
    }

    inner class ChatRoomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val streamid = view.findViewById<TextView>(R.id.stream_id)
        val tags = view.findViewById<TextView>(R.id.stream_tags)
        val nickname = view.findViewById<TextView>(R.id.nickname)
        val title = view.findViewById<TextView>(R.id.stream_title)
        val headShot = view.findViewById<ImageView>(R.id.head_shot)
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }
}