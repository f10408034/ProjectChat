package com.vangood.projectchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vangood.projectchat.databinding.FragmentSearchBinding
import com.vangood.projectchat.databinding.RowChatroomBinding

class SearchFragment : Fragment() {
    val TAG = SearchFragment::class.java.simpleName
    lateinit var binding: FragmentSearchBinding
    lateinit var adapter : SearchRoomAdapter
    val roomViewModel by viewModels<SearchViewModel>()
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

        roomViewModel.searchRooms.observe(viewLifecycleOwner) { rooms ->
            adapter.submitRooms(rooms)
        }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                val keywords = binding.searchView.query.toString()
                roomViewModel.getSearchRooms(keywords)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val keywords = binding.searchView.query.toString()
                roomViewModel.getSearchRooms(keywords)
                return false
            }
        })
    }

    inner class SearchRoomAdapter : RecyclerView.Adapter<SearchViewHolder>() {
        val searchRooms = mutableListOf<Lightyear>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
            val binding = RowChatroomBinding.inflate(layoutInflater, parent, false)
            return SearchViewHolder(binding)
        }

        override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
            val lightYearSearch = searchRooms[position]
            holder.HostName.setText(lightYearSearch.nickname)
            holder.title.setText(lightYearSearch.stream_title)
            Glide.with(this@SearchFragment).load(lightYearSearch.head_photo)
                .into(holder.headPhoto)
            holder.itemView.setOnClickListener {
            }
        }

        override fun getItemCount(): Int {
            return searchRooms.size
        }

        fun submitRooms(rooms: List<Lightyear>) {
            searchRooms.clear()
            searchRooms.addAll(rooms)
            notifyDataSetChanged()
        }
    }

    inner class SearchViewHolder(val binding: RowChatroomBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val HostName = binding.chatroomHostName
        val title = binding.chatroomTitle
        val headPhoto = binding.headShot
    }


}