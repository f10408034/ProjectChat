package com.vangood.projectchat

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vangood.projectchat.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {
    companion object {
        val TAG = SearchFragment::class.java.simpleName
    }
    lateinit var binding: FragmentSearchBinding
    lateinit var adapter : SearchRoomAdapter
    val ViewModel by viewModels<SearchViewModel>()
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

        ViewModel.searchRooms.observe(viewLifecycleOwner) { rooms ->
            adapter.submitRooms(rooms)
        }

        binding.searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val keyword = binding.searchView.query.toString()
                ViewModel.getSearchRooms(keyword)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val keyword = binding.searchView.query.toString()
                ViewModel.getSearchRooms(keyword)
                return false
            }
        })

    }
    inner class SearchRoomAdapter : RecyclerView.Adapter<SearchViewHolder>() {
        val searchRooms = mutableListOf<Lightyear>()
        override fun getItemCount(): Int {
            return searchRooms.size
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
            val view = layoutInflater.inflate(
                R.layout.row_chatroom, parent, false)
            return SearchViewHolder(view)
        }
        override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
            val lightYearSearch = searchRooms[position]
            holder.host.setText(lightYearSearch.nickname)
            holder.title.setText(lightYearSearch.stream_title)
            Glide.with(this@SearchFragment).load(lightYearSearch.head_photo)
                .into(holder.headPhoto)
        }
        fun submitRooms(rooms: List<Lightyear>) {
            searchRooms.clear()
            searchRooms.addAll(rooms)
            notifyDataSetChanged()
        }

    }
    inner class SearchViewHolder(view: View) :
        RecyclerView.ViewHolder(binding.root) {
        val host = view.findViewById<TextView>(R.id.chatroom_host_name)
        val title = view.findViewById<TextView>(R.id.chatroom_title)
        val headPhoto = view.findViewById<ImageView>(R.id.head_shot)
    }

}