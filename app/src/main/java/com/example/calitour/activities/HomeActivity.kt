package com.example.calitour.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.calitour.R
import com.example.calitour.activities.fragments.ItineraryFragment
import com.example.calitour.activities.fragments.UserProfileFragment
import com.example.calitour.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    private val profile = UserProfileFragment()
    private val itinerary = ItineraryFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.navbarUser.setOnItemSelectedListener {
            when(it.itemId){
                R.id.profile_user -> {
                    showFragment(profile)
                }
                R.id.itinerary_user -> {
                showFragment(itinerary)
            }
            }
            true
        }

        setContentView(binding.root)
    }

    fun showFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit()
    }
}