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

        binding.liveEvents.setOnClickListener{
                binding.liveEvents.setImageResource(R.drawable.live)
                binding.shop.setImageResource(R.drawable.shop_gray)
                binding.endendEvents.setImageResource(R.drawable.clock_gray)
        }

        binding.shop.setOnClickListener{
            binding.shop.setImageResource(R.drawable.shop)
            binding.liveEvents.setImageResource(R.drawable.live_gray)
            binding.endendEvents.setImageResource(R.drawable.clock_gray)
        }

        binding.endendEvents.setOnClickListener{
            binding.endendEvents.setImageResource(R.drawable.clock)
            binding.liveEvents.setImageResource(R.drawable.live_gray)
            binding.shop.setImageResource(R.drawable.shop_gray)
        }

        binding.liveEvents.performClick()


        binding.bottomNavigationView.menu[2].isChecked = true

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