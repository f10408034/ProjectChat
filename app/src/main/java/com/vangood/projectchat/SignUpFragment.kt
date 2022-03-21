package com.vangood.projectchat

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vangood.projectchat.databinding.FragmentSignupBinding

class SignUpFragment : Fragment() {
    lateinit var binding: FragmentSignupBinding
    val viewModel by viewModels<SignUpViewModel>()

    val selectPictureFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                binding.imageView2.setImageURI(it)
                uri.toString()//content://ccc.ddd/sss/aaa

            }
        }
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
            if (viewModel.accountverify(account)){
                if (viewModel.passwordverify(password)){
                    val pref = requireContext().getSharedPreferences("check", Context.MODE_PRIVATE)
                    pref.edit().putString("nickname", nickname)
                        .putString("account", account)
                        .putString("password", password)
                        .apply()
                    loadFragment(LoginFragment())
                }else {
                    AlertDialog.Builder(requireContext())
                        .setTitle("password error")
                        .setMessage("Please enter 8-12 letters or numbers")
                        .setPositiveButton("ok",null)
                        .show()
                }
            }else {
                AlertDialog.Builder(requireContext())
                    .setTitle("account error")
                    .setMessage("Please enter 4-20 letters or numbers")
                    .setPositiveButton("ok",null)
                    .show()
            }
        }

        binding.back.setOnClickListener {
            loadFragment(LoginFragment())
        }

        binding.setheadphoto.setOnClickListener {
            pickFromGallery()
        }

    }

    private fun loadFragment(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    private fun pickFromGallery() {
        selectPictureFromGallery.launch("image/*")
    }
}