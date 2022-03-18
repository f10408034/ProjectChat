package com.vangood.projectchat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.vangood.projectchat.databinding.FragmentLoginBinding
import com.vangood.projectchat.databinding.FragmentSignupBinding

class LoginFragment: Fragment() {
    private val TAG = FragmentLoginBinding::class.java.simpleName
    lateinit var binding: FragmentLoginBinding
    val viewModel by viewModels<LoginViewModel>()
    var remember = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = requireContext().getSharedPreferences("check", Context.MODE_PRIVATE)
        val checked = pref.getBoolean("rem_account", false)
        binding.cbLoginRemember.isChecked = checked
        cbFuntion(pref)

        val prefAccount = pref.getString("account","")
        if (checked) binding.edLoginAccount.setText(prefAccount)
        bLoginFunction(pref)
        binding.bLoginSignup.setOnClickListener {
            loadFragment(SignUpFragment())
        }
    }

    private fun bLoginFunction(pref: SharedPreferences) {
        binding.bLoginLogin.setOnClickListener {
            val account = pref.getString("account","")
            val password = pref.getString("password","")

            if (viewModel.loginFunc(binding.edLoginAccount.text.toString()
                                ,binding.edLoginPassword.text.toString()
                                ,account.toString()
                                ,password.toString())) {
                val nickname = pref.getString("nickname", "")
                AlertDialog.Builder(requireContext())
                    .setTitle("Login")
                    .setMessage("Login success")
                    .setPositiveButton("ok", null)
                    .show()
                if (remember)
                    pref.edit()
                        .putString("account", binding.edLoginAccount.text.toString())
                        .apply()
                loadFragment(HomeFragment())
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("Login")
                    .setMessage("Login Failed")
                    .setPositiveButton("ok", null)
                    .show()
            }
        }
    }
    private fun cbFuntion(pref: SharedPreferences) {
        binding.cbLoginRemember.setOnCheckedChangeListener { compoundButton, checked ->
            remember = checked
            pref.edit().putBoolean("rem_account", remember).apply()
            if (!checked) {
                binding.edLoginAccount.setText("")
            }
        }
    }
    private fun loadFragment(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

}