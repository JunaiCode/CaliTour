package com.example.calitour.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.example.calitour.databinding.ActivityEventDetailBinding
import com.example.calitour.viewmodel.EntityViewModel
import java.text.SimpleDateFormat

class EventDetailActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityEventDetailBinding.inflate(layoutInflater)
    }

    private val vm: EntityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val eventId = intent?.getStringExtra("event_id")

        vm.singleEvent.observe(this){event ->
            if(event.img != ""){
                Glide.with(this).load(event.img).into(binding.seeEventImage)
            }
            val sdf = SimpleDateFormat("dd/MM - HH:mm a")
            val date = sdf.format(event.date.toDate())

            binding.eventDescriptionTV.text = event.description
            binding.seeEventName.text = event.name
            binding.seeEventEntity.text = event.entityName
            binding.priceEventTV.text = event.price.toString()
            binding.placeEventTV.text = event.place
            binding.dateEventTV.text = date
            binding.eventReactionTV.text = event.reaction.toString()
        }
        if (eventId != null) {
            vm.getFullEventById(eventId)
        }


        binding.goBackBttn.setOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }
}