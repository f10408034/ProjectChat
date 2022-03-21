package com.vangood.projectchat

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vangood.projectchat.databinding.FragmentPersonBinding

class PersonFragment: Fragment() {
    lateinit var binding: FragmentPersonBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonBinding.inflate(layoutInflater)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var pref = requireContext().getSharedPreferences("check",Context.MODE_PRIVATE)

        binding.tvNickname.text = "nick name : ${pref.getString("nickname", "")}"
        binding.tvAccount.text = "account : ${pref.getString("account", "")}"
        binding.bSignOut.setOnClickListener {
            pref.edit().putBoolean("loginstate", false)
                .apply()
            loadFragment(LoginFragment())
        }
    }


    private fun loadFragment(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }
}