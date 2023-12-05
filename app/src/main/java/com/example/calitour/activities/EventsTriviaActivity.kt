package com.example.calitour.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calitour.R
import com.example.calitour.components.adapter.TriviasEventAdapter
import com.example.calitour.databinding.ActivityEventsTriviaBinding
import com.example.calitour.viewmodel.EntityViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EventsTriviaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventsTriviaBinding;
    private lateinit var  adapter: TriviasEventAdapter

    private val vm = EntityViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventsTriviaBinding.inflate(layoutInflater)
        setContentView(binding.root)



        adapter = TriviasEventAdapter()
        vm.getEventsAvailablesTriviaByEntityId(Firebase.auth.currentUser?.uid.toString())
        vm.getImagesEntityAvailableEvents(Firebase.auth.currentUser?.uid.toString())
        binding.EventsTriviasRecyclerView.adapter=adapter
        binding.EventsTriviasRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.EventsTriviasRecyclerView.setHasFixedSize(true)

        vm.eventsTriviaQuery.observe(this) { list ->
            adapter.setList(list)
        }

        // Cambia viewLifecycleOwner por this
        vm.uriEventsEntity.observe(this) { uris ->
            adapter.setUris(uris)
        }

        binding.bottomNavigationView.menu[0].isChecked = true
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                binding.bottomNavigationView.menu[2].itemId ->{
                    startActivity(Intent(this, ProfileEntityActivity::class.java))
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
            binding.bottomNavigationView.menu[0].isChecked = false
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
}