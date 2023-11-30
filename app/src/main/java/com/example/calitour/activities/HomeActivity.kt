package com.example.calitour.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.calitour.R
import com.example.calitour.activities.fragments.EventsFragment
import com.example.calitour.activities.fragments.UserProfileFragment
import com.example.calitour.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    private val profile = UserProfileFragment()
    private val events = EventsFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.navbarUser.setOnItemSelectedListener {
            when(it.itemId){
                R.id.profile_user -> {
                    showFragment(profile)
                }
                R.id.search_user -> {
                    showFragment(events)
                }
            }
            true
        }

        setContentView(binding.root)
    }

    fun showFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragmentContainerHome, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}