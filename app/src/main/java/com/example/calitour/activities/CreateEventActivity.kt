package com.example.calitour.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.calitour.R
import com.example.calitour.activities.fragments.CreateEventFragment
import com.example.calitour.activities.fragments.CreateProductFragment
import com.example.calitour.databinding.ActivityCreateEventBinding

class CreateEventActivity : AppCompatActivity() {

    private val createEventFragment = CreateEventFragment.newInstance()
    private val createProductFragment = CreateProductFragment.newInstance()

    private val binding: ActivityCreateEventBinding by lazy {
        ActivityCreateEventBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    fun showFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer,fragment).commit()
    }
}