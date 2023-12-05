package com.example.calitour.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.calitour.R
import com.example.calitour.activities.fragments.EventsFragment
import com.example.calitour.activities.fragments.EmptyFragment
import com.example.calitour.activities.fragments.HomeUserFragment
import com.example.calitour.activities.fragments.ItineraryFragment
import com.example.calitour.activities.fragments.UserProfileFragment
import com.example.calitour.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityHomeBinding.inflate(layoutInflater)
    }

    private val home = HomeUserFragment.newInstance()
    private val search = EventsFragment()
    private val itinerary = ItineraryFragment()
    private val profile = UserProfileFragment.newInstance()
    private val entity = EntityProfileForUser.newInstance()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.navbarUser.setOnItemSelectedListener {
            when (it.itemId) {
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
        binding.navbarUser.selectedItemId = R.id.home_user
        setContentView(binding.root)
    }

    /*fun showFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }*/
    fun showFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerHome, fragment)
            .addToBackStack(null)  // Agrega el fragmento al back stack
            .commit()
    }

    fun showEntity(entityId: String) {
        val extras = Bundle()
        extras.putString("entity_id", entityId)
        entity.arguments = extras
        binding.navbarUser.menu[1].isChecked = true
        showFragment(entity)

    }
}

