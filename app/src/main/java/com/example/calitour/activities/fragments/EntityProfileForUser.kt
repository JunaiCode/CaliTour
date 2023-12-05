package com.example.calitour.activities.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.calitour.R
import com.example.calitour.components.adapter.ProductItemAdapter
import com.example.calitour.components.adapter.UserEventAdapter
import com.example.calitour.databinding.EntityProfileFragmentBinding
import com.example.calitour.viewmodel.EntityViewModel

class EntityProfileForUser: Fragment() {

    private lateinit var binding: EntityProfileFragmentBinding
    private val vm: EntityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EntityProfileFragmentBinding.inflate(inflater, container, false)

        val extras = arguments?.getString("entity_id")
        if (extras != null) {
            vm.getEntityProfile(extras)
        }

        binding.userShop.setOnClickListener {
            binding.userShop.setImageResource(R.drawable.shop)
            binding.userLiveEvents.setImageResource(R.drawable.live_gray)
            binding.userEndedEvents.setImageResource(R.drawable.clock_gray)

            binding.userEntityProfileList.layoutManager = GridLayoutManager(context, 3)
            val adapter = ProductItemAdapter()
            binding.userEntityProfileList.adapter = adapter
            vm.getProductsByEntityId(extras.toString())

            vm.products.observe(viewLifecycleOwner) {
                adapter.addAll(it)
            }
        }

        binding.userLiveEvents.setOnClickListener {
            binding.userLiveEvents.setImageResource(R.drawable.live)
            binding.userShop.setImageResource(R.drawable.shop_gray)
            binding.userEndedEvents.setImageResource(R.drawable.clock_gray)

            binding.userEntityProfileList.layoutManager = LinearLayoutManager(context)
            val adapter = UserEventAdapter()
            vm.getAvailableEventsByEntityIdFull(extras.toString())

            vm.eventsFull.observe(viewLifecycleOwner){
                adapter.setEvents(it)
            }
            binding.userEntityProfileList.adapter = adapter

        }

        binding.userEndedEvents.setOnClickListener {
            binding.userEndedEvents.setImageResource(R.drawable.clock)
            binding.userLiveEvents.setImageResource(R.drawable.live_gray)
            binding.userShop.setImageResource(R.drawable.shop_gray)

            binding.userEntityProfileList.layoutManager = LinearLayoutManager(context)
            val adapter = UserEventAdapter()
            vm.getUnavailableEventsByEntityIdFull(extras.toString())

            vm.eventsFull.observe(viewLifecycleOwner) {
                adapter.setEvents(it)
            }

            binding.userEntityProfileList.adapter = adapter
        }

        binding.userLiveEvents.performClick()
        vm.profile.observe(viewLifecycleOwner){profile ->
            binding.userEntityName.text = profile.name
            binding.userDescriptionEntityTV.text = profile.description
            setSocialMedia(binding.userFacebook, profile.facebook)
            setSocialMedia(binding.userTwitter, profile.x)
            setSocialMedia(binding.userInstagram, profile.instagram)

            if(profile.photoID!=""){
                Glide.with(this).load(profile.photoID).into(binding.userEntityIV)
            }
        }


        return binding.root
    }

    fun setSocialMedia(button: ImageView, url:String){
        if(url!=""){
            button.setOnClickListener{
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        }

    }

    companion object {
        fun newInstance(): EntityProfileForUser {
            return EntityProfileForUser()
        }
    }
}