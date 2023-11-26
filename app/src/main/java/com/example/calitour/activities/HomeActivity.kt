package com.example.calitour.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.calitour.R
import com.example.calitour.activities.fragments.EmptyFragment
import com.example.calitour.activities.fragments.ItineraryFragment
import com.example.calitour.activities.fragments.UserProfileFragment
import com.example.calitour.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    private val home = EmptyFragment()
    private val search = EmptyFragment()
    private val itinerary = ItineraryFragment()
    private val profile = UserProfileFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.navbarUser.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home_user -> {
                    Log.d("HomeActivity", "Selected Home")
                    showFragment(home)
                }
                R.id.search_user -> {
                    Log.d("HomeActivity", "Selected Search")
                    showFragment(search)
                }
                R.id.itinerary_user -> {
                    Log.d("HomeActivity", "Selected Itinerary")
                    showFragment(itinerary)
                    itinerary.updateDate(System.currentTimeMillis())
                }
                R.id.profile_user -> {
                    Log.d("HomeActivity", "Selected Profile")
                    showFragment(profile)
                }
            }
            true
        }

        setContentView(binding.root)
    }

    fun showFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }


}