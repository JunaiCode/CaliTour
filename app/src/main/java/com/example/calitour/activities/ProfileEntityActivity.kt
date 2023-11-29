package com.example.calitour.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.example.calitour.R
import com.example.calitour.databinding.ActivityProfileEntityBinding
import com.example.calitour.viewmodel.EntityViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ProfileEntityActivity : AppCompatActivity() {

    private val vm = EntityViewModel()
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
        val event = dialog.findViewById<Button>(R.id.eventPopUpBttn)
        val product = dialog.findViewById<Button>(R.id.productPopUpBttn)

        event.setOnClickListener {
            val intent = Intent(this, CreateEventProductActivity::class.java).putExtra("fragment", "EVENT")
            dialog.dismiss()
            startActivity(intent)
        }

        product.setOnClickListener {
            val intent = Intent(this, CreateEventProductActivity::class.java).putExtra("fragment", "PRODUCT")
            dialog.dismiss()
            startActivity(intent)
        }


        dialog.show()
        binding.bottomNavigationView.menu[1].isChecked = true


    }
    private fun setProfileNavigation(){
        binding.liveEvents.setOnClickListener{
            binding.liveEvents.setImageResource(R.drawable.live)
            binding.shop.setImageResource(R.drawable.shop_gray)
            binding.endendEvents.setImageResource(R.drawable.clock_gray)
            vm.getEventsAvailablesByEntityId(Firebase.auth.currentUser?.uid.toString())
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
            vm.getEventsUnavailablesByEntityId(Firebase.auth.currentUser?.uid.toString())
        }

        binding.liveEvents.performClick()
    }
}