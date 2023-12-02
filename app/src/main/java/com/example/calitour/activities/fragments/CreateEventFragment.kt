package com.example.calitour.activities.fragments

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.calitour.R
import com.example.calitour.activities.ProfileEntityActivity
import com.example.calitour.components.adapter.CategoriesAdapterSpinner
import com.example.calitour.databinding.CreateEventFragmentBinding
import com.example.calitour.model.DTO.EventDocumentDTO
import com.example.calitour.model.entity.Badge
import com.example.calitour.model.entity.Event
import com.example.calitour.model.entity.Price
import com.example.calitour.model.entity.Trivia
import com.example.calitour.viewmodel.CreateEventProductViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.UUID


class CreateEventFragment: Fragment() {
    private lateinit var binding: CreateEventFragmentBinding
    private var badgeUri: Uri = Uri.parse("")
    private var eventUri: Uri = Uri.parse("")
    private val vm: CreateEventProductViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val appContext = requireContext()
        binding = CreateEventFragmentBinding.inflate(inflater,container,false)
        val categories = resources.getStringArray(R.array.categories)
        val adapter = CategoriesAdapterSpinner(appContext, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categories.adapter = adapter
        vm.editEvent.observe(viewLifecycleOwner){event ->
           setValues(event)
        }
        if(arguments?.getString("eventId") != null){
            binding.btnCreateEvent.visibility = View.GONE
            Log.e("arguments",arguments.toString())
            val eventId = arguments?.getString("eventId").toString()
            vm.getEventById(eventId)
        }else{
            vm.clearEditEvent()
            binding.btnEditEvent.visibility = View.GONE
        }

        val launcherBadge = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ::onGalleryResultBadge
        )

        val launcherImage = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ::onGalleryResultImage
        )

        binding.badgeImg.setOnClickListener(){
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            launcherBadge.launch(intent)
        }

        binding.eventImg.setOnClickListener(){
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            launcherImage.launch(intent)
        }

        binding.categories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    Log.e("Posicion seleccionada",position.toString())
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.btnCreateEvent.setOnClickListener(){
            val newEvent = Event(
                UUID.randomUUID(),
                Firebase.auth.currentUser?.uid.toString(),
                binding.categories.selectedItem.toString(),
                vm.dateToMilliseconds(binding.dateEvent.text.toString(),vm.dateFormat),
                binding.descriptionEvent.text.toString(),
                binding.nameEvent.text.toString(),
                binding.locationEvent.text.toString(),
                0,
                0.0,
                "available",
                eventUri,
                ArrayList<Price>(),
                ArrayList<Badge>(),
                ArrayList<Trivia>()
            )
            newEvent.prices.add(Price("Entrada General",binding.priceEvent.text.toString().toDouble(),UUID.randomUUID(),"General"))
            newEvent.badges.add(Badge(UUID.randomUUID(),badgeUri,"Badge"))
            vm.createEvent(newEvent)
            vm.uploadImages(newEvent)
            startActivity(Intent(requireContext(), ProfileEntityActivity::class.java))
        }

        binding.btnEditEvent.setOnClickListener() {

        }

        return binding.root
    }

    fun onGalleryResultBadge(result: ActivityResult){
        badgeUri = result.data?.data!!
        Glide.with(this).load(badgeUri).into(binding.badgeImg)
    }

    fun onGalleryResultImage(result: ActivityResult){
        eventUri = result.data?.data!!
        Glide.with(this).load(eventUri).into(binding.eventImg)
    }

    private fun setValues(event:EventDocumentDTO?){
        if(event !=null){
            binding.nameEvent.text = Editable.Factory.getInstance().newEditable(event?.name)
            binding.categories.setSelection(getIndexCategory(event?.category.toString()))
            binding.dateEvent.text = Editable.Factory.getInstance().newEditable(vm.millisecondsToDate(event?.date?.toDate()?.time.toString(),vm.dateFormat))
            binding.descriptionEvent.text = Editable.Factory.getInstance().newEditable(event?.description)
            binding.locationEvent.text = Editable.Factory.getInstance().newEditable(event?.place)
        }else{
            binding.nameEvent.text.clear()
            binding.categories.setSelection(0)
            binding.dateEvent.text.clear()
            binding.descriptionEvent.text.clear()
            binding.locationEvent.text.clear()
        }
    }

    private fun getIndexCategory(category:String):Int{
        val array = resources.getStringArray(R.array.categories)
        var position = 0
        for(i in array){
            if(i != category){
                position++
            }else{
                return position
            }
        }
        return 0
    }

    companion object{
        fun newInstance():CreateEventFragment{
            return CreateEventFragment()
        }
    }
}