package com.example.calitour.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.calitour.databinding.ActivityEventDetailBinding
import com.example.calitour.viewmodel.EntityViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class EventDetailActivity : AppCompatActivity() {


    private var dateItinerary:String=""
    private var itineraryId:String=""

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

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            Log.i("FECHAAA DE LOS DETALLES  ",event.date.toDate().toString())
            dateItinerary =dateFormat.format(event.date.toDate())
            Log.i("FECHAAA DE LOS DETALLES 22 ",dateItinerary)


            binding.eventDescriptionTV.text = event.description
            binding.seeEventName.text = event.name
            binding.seeEventEntity.text = event.entityName
            binding.priceEventTV.text = event.price.toString()
            binding.placeEventTV.text = event.place
            binding.dateEventTV.text = date
            binding.eventReactionTV.text = event.reaction.toString()

            lifecycleScope.launch {
                itineraryId = vm.findItineraryIdByEventId(event.id)
                if (itineraryId.isNotEmpty()) {
                    Log.i("Itinerary ID", itineraryId)
                    binding.addToItinerary.text = "Agregado"
                } else {
                    Log.i("Itinerary ID", "No se encontró el itinerario para el evento")
                    binding.addToItinerary.text = "Añadir"
                }


                binding.addEventItinerary.setOnClickListener{
                    var texto = binding.addToItinerary.text
                    if(texto=="Agregado"){
                        vm.removeEventFromItinerary(dateItinerary,eventId,itineraryId)
                        binding.addToItinerary.text = "Añadir"
                    }else{
                        var newItineraryId=vm.addEventToItinerary(dateItinerary,eventId,itineraryId)
                        if(newItineraryId.toString()!=""){
                            itineraryId= newItineraryId.toString()
                        }
                        binding.addToItinerary.text = "Agregado"
                    }
                }
            }

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