package com.example.calitour.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.calitour.R
import com.example.calitour.activities.fragments.ProductListFragment
import com.example.calitour.databinding.ActivityProfileEntityBinding
import com.example.calitour.viewmodel.EntityViewModel


class ProfileEntityActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityProfileEntityBinding.inflate(layoutInflater)
    }
    private val vm: EntityViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        vm.loadProfile()

        vm.profile.observe(this){profile ->
            binding.entityName.text = profile.name
            binding.descriptionEntityTV.text = profile.description
            if(profile.photoID!=""){
                Glide.with(this).load(profile.photoID).into(binding.entityIV)
            }
        }

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
        binding.editEntityProfileBtn.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("fragment", "entity_fragment")
            bundle.putSerializable("profile", vm.profile.value)

            Log.e("<<<", vm.profile.value.toString())

            intent = Intent(this, EditProfileActivity::class.java).putExtras(bundle)
            startActivity(intent)

        }

    }

    fun showFragmentAdd(fragment: Fragment){
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainerEntity, fragment).addToBackStack(null).commit()

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
        }

        binding.shop.setOnClickListener{
            binding.shop.setImageResource(R.drawable.shop)
            binding.liveEvents.setImageResource(R.drawable.live_gray)
            binding.endendEvents.setImageResource(R.drawable.clock_gray)
            showFragmentAdd(ProductListFragment())

        }

        binding.endendEvents.setOnClickListener{
            binding.endendEvents.setImageResource(R.drawable.clock)
            binding.liveEvents.setImageResource(R.drawable.live_gray)
            binding.shop.setImageResource(R.drawable.shop_gray)
        }

        binding.liveEvents.performClick()
    }
}