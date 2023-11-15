package com.example.calitour

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.example.calitour.databinding.ActivityProfileEntityBinding

class ProfileEntityActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityProfileEntityBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.bottomNavigationView.menu[2].setCheckable(true)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                binding.bottomNavigationView.menu[0].itemId ->{
                    startActivity(Intent(this, MainActivity::class.java))
                }

                binding.bottomNavigationView.menu[1].itemId ->{
                    startActivity(Intent(this, MainActivity::class.java))
                }
            }
            false
        }

    }
}