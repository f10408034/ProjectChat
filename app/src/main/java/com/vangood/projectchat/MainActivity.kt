package com.vangood.projectchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.vangood.projectchat.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val fragments = mutableListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFragment()

        binding.bottomNavBar.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.action_home -> {
                    supportFragmentManager.beginTransaction().run{
                        replace(R.id.container, fragments[0])
                        commit()
                    }
                    true
                }
                R.id.action_person ->{
                    supportFragmentManager.beginTransaction().run{
                        replace(R.id.container, fragments[1])
                        commit()
                    }
                    true
                }
                else -> true
            }
        }
    }

    private fun initFragment(){
        fragments.add(0, HomeFragment())
        fragments.add(1, LoginFragment())
        fragments.add(2,SignUpFragment())

        supportFragmentManager.beginTransaction().run {
            add(R.id.container, fragments[0])
            commit()
        }
    }
}