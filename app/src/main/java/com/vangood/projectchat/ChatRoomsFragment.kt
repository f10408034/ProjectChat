package com.vangood.projectchat

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import androidx.fragment.app.Fragment
import com.vangood.projectchat.databinding.FragmentChatroomsBinding

class ChatRoomsFragment : Fragment() {
    lateinit var binding: FragmentChatroomsBinding
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

        var mediaController = MediaController(requireContext())
        mediaController.setAnchorView(binding.videoView)

        val offlineUri = Uri.parse("android.resource://${requireActivity().packageName}/${R.raw.hime3}")

        binding.videoView.setMediaController(mediaController)
        binding.videoView.setVideoURI(offlineUri)
        binding.videoView.requestFocus()
        binding.videoView.start()
    }
}