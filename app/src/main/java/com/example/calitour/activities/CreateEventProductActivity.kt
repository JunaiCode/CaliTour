package com.example.calitour.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.calitour.R
import com.example.calitour.activities.fragments.CreateEventFragment
import com.example.calitour.activities.fragments.CreateProductFragment
import com.example.calitour.databinding.ActivityCreateEventBinding

class CreateEventProductActivity : AppCompatActivity() {

    private val createEventFragment = CreateEventFragment()
    private val createProductFragment = CreateProductFragment()

    private val binding: ActivityCreateEventBinding by lazy {
        ActivityCreateEventBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        when(intent?.extras?.getString("fragment")){
            "EVENT" ->{
                showFragment(createEventFragment)
            }
            "PRODUCT" -> {
                showFragment(createProductFragment)
            }
        }
        setContentView(binding.root)
    }

    fun showFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerCreate,fragment).commit()
    }
}