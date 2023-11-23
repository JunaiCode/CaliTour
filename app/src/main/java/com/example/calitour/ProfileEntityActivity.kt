package com.example.calitour

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.example.calitour.activities.MainActivity
import com.example.calitour.databinding.ActivityProfileEntityBinding


class ProfileEntityActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityProfileEntityBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

       setProfileNavigation()

        binding.bottomNavigationView.menu[2].isChecked = true

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                binding.bottomNavigationView.menu[0].itemId ->{
                    startActivity(Intent(this, MainActivity::class.java))
                }

                binding.bottomNavigationView.menu[1].itemId ->{
                    showPopUp()
                }
            }
            false
        }

    }

    fun showPopUp() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.entity_popup_create)
        dialog.setOnDismissListener{
            binding.bottomNavigationView.menu[2].isChecked = false
        }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
        binding.bottomNavigationView.menu[1].isChecked = true


    }

    private fun setProfileNavigation(){
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
    }
}