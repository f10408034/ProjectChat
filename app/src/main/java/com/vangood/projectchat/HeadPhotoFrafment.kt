package com.vangood.projectchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vangood.projectchat.databinding.FragmentHeadphotoBinding

class HeadPhotoFrafment : Fragment() {
    lateinit var binding: FragmentHeadphotoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHeadphotoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            loadFragment(SignUpFragment())
        }

    }

    private fun loadFragment(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }
}