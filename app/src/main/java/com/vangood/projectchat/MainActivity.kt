package com.vangood.projectchat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.vangood.projectchat.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    val fragments = mutableListOf<Fragment>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var pref = getSharedPreferences("check", MODE_PRIVATE)
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
                R.id.action_search ->{
                    supportFragmentManager.beginTransaction().run {
                        replace(R.id.container, fragments[1])
                        commit()
                    }
                    true
                }
                R.id.action_person ->{

                    if (pref.getBoolean("loginstate", false)){
                    supportFragmentManager.beginTransaction().run{
                        replace(R.id.container, fragments[5])
                        commit()
                        }
                    } else {
                        supportFragmentManager.beginTransaction().run{
                            replace(R.id.container, fragments[2])
                            commit()
                        }
                    }
                    true
                }
                else -> true
            }
        }
    }

    private fun initFragment(){
        fragments.add(0, HomeFragment())
        fragments.add(1, SearchFragment())
        fragments.add(2, LoginFragment())
        fragments.add(3, SignUpFragment())
        fragments.add(4, ChatRoomsFragment())
        fragments.add(5, PersonFragment())

        supportFragmentManager.beginTransaction().run {
            add(R.id.container, fragments[0])
            commit()
        }
    }
}