package com.example.calitour.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.calitour.R
import com.example.calitour.activities.fragments.EditProfileEntityFragment
import com.example.calitour.databinding.ActivityEditProfileBinding
import com.example.calitour.model.entity.EntityFirestore

class EditProfileActivity : AppCompatActivity() {


    private val binding by lazy{
        ActivityEditProfileBinding.inflate(layoutInflater)
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        val fragment = bundle?.getString("fragment")
        val profile = bundle?.getSerializable("profile", EntityFirestore::class.java)
        val profileBundle = Bundle()
        profileBundle.putSerializable("profile", profile)

        Log.e("<<<", profileBundle.toString())

        when(fragment){
            "entity_fragment" -> {
                val fragment = EditProfileEntityFragment.newInstance()
                fragment.arguments = profileBundle
                showFragmentAdd(fragment)
            }
        }

    }

    fun showFragmentAdd(fragment: Fragment){
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainerEditProfile, fragment).addToBackStack(null).commit()

    }
}