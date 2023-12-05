package com.example.calitour.activities.fragments

import android.app.Activity
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
import com.example.calitour.model.entity.Question
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
    ): View {
        val appContext = requireContext()
        binding = CreateEventFragmentBinding.inflate(inflater,container,false)
        val categories = resources.getStringArray(R.array.categories)
        val adapter = CategoriesAdapterSpinner(appContext, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categories.adapter = adapter
        vm.editEvent.observe(viewLifecycleOwner){event ->
           setValues(event)
        }
        vm.uploadedEventImgComplete.observe(viewLifecycleOwner){img->
            if(img == true && vm.uploadedBadgeImgComplete.value == true){
                    startActivity(Intent(requireContext(), ProfileEntityActivity::class.java))
            }
        }
        vm.uploadedBadgeImgComplete.observe(viewLifecycleOwner){img->
            if(img == true && vm.uploadedEventImgComplete.value == true){
                startActivity(Intent(requireContext(), ProfileEntityActivity::class.java))
            }
        }
        vm.eventBadges.observe(viewLifecycleOwner){}
        vm.eventImgUri.observe(viewLifecycleOwner){img ->
            eventUri = img
            Glide.with(this).load(eventUri).into(binding.eventImg)
        }
        vm.eventBadgesUri.observe(viewLifecycleOwner){badge ->
            badgeUri = badge[0]
            Glide.with(this).load(badgeUri).into(binding.badgeImg)
        }
        vm.eventPrices.observe(viewLifecycleOwner){prices->
            binding.priceEvent.text = Editable.Factory.getInstance().newEditable(prices[0].fee.toString())
        }
        if(arguments?.getString("eventId") != null){
            binding.btnCreateEvent.visibility = View.GONE
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
                ArrayList<Question>()
            )
            newEvent.prices.add(Price("Entrada General",binding.priceEvent.text.toString().toDouble(),UUID.randomUUID(),"General"))
            newEvent.badges.add(Badge(UUID.randomUUID(),badgeUri,"Badge"))
            vm.createEvent(newEvent)
        }

        binding.btnEditEvent.setOnClickListener() {
            val newEvent = Event(
                UUID.fromString(vm.editEvent.value?.id),
                Firebase.auth.currentUser?.uid.toString(),
                binding.categories.selectedItem.toString(),
                vm.dateToMilliseconds(binding.dateEvent.text.toString(),vm.dateFormat),
                binding.descriptionEvent.text.toString(),
                binding.nameEvent.text.toString(),
                binding.locationEvent.text.toString(),
                vm.editEvent.value?.reaction.toString().toInt(),
                vm.editEvent.value?.score.toString().toDouble(),
                vm.editEvent.value?.state.toString(),
                eventUri,
                ArrayList<Price>(),
                ArrayList<Badge>(),
                ArrayList<Question>()
            )
            newEvent.prices.add(Price("Entrada General",binding.priceEvent.text.toString().toDouble(),UUID.fromString(
                vm.eventPrices.value?.get(0)?.id),"General"))
            newEvent.badges.add(Badge(UUID.fromString(vm.eventBadges.value?.get(0)?.id),badgeUri,"Badge"))
            vm.editEvent(newEvent)
            startActivity(Intent(requireContext(), ProfileEntityActivity::class.java))
        }
        return binding.root
    }

    fun onGalleryResultBadge(result: ActivityResult){
        if(result.resultCode != Activity.RESULT_CANCELED) {
            badgeUri = result.data?.data!!
            Glide.with(this).load(badgeUri).into(binding.badgeImg)
        }
    }
    fun onGalleryResultImage(result: ActivityResult){
        if(result.resultCode != Activity.RESULT_CANCELED) {
            eventUri = result.data?.data!!
            Glide.with(this).load(eventUri).into(binding.eventImg)
        }
    }

    private fun setValues(event:EventDocumentDTO?){
        if(event !=null){
            vm.getImgEvent(event.id)
            vm.getImgBadge(event.id)
            vm.getPricesEvent(event.id)
            vm.getEventBadges(event.id)
            binding.nameEvent.text = Editable.Factory.getInstance().newEditable(event.name)
            binding.categories.setSelection(getIndexCategory(event.category))
            binding.dateEvent.text = Editable.Factory.getInstance().newEditable(vm.millisecondsToDate(event.date.toDate().time.toString(),vm.dateFormat))
            binding.descriptionEvent.text = Editable.Factory.getInstance().newEditable(event.description)
            binding.locationEvent.text = Editable.Factory.getInstance().newEditable(event.place)
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