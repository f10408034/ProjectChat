package com.vangood.projectchat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vangood.projectchat.databinding.FragmentSignupBinding

class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bSignupSignup.setOnClickListener {
            val nickname = binding.edSignupNickname.text.toString()
            val account = binding.edSignupAccount.text.toString()
            val password = binding.edSignupPassword.text.toString()

            val pref = requireContext().getSharedPreferences("check", Context.MODE_PRIVATE)
            pref.edit().putString("nickname", nickname)
                .putString("account", account)
                .putString("password", password)
                .apply()

            loadFragment(LoginFragment())
        }

        binding.back.setOnClickListener {
            loadFragment(LoginFragment())
        }

        binding.setheadphoto.setOnClickListener {
            loadFragment(HeadPhotoFrafment())
        }
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }
}