package com.example.calitour.activities.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calitour.R
import com.example.calitour.activities.ProfileEntityActivity
import com.example.calitour.components.adapter.ActiveEventAdapter
import com.example.calitour.databinding.ActiveEventFragmentBinding
import com.example.calitour.viewmodel.CreateEventProductViewModel
import com.example.calitour.viewmodel.EntityViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ActiveEventFragment: Fragment() {
    private val vm = EntityViewModel()
    private lateinit var  binding: ActiveEventFragmentBinding
    private lateinit var  adapter: ActiveEventAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActiveEventFragmentBinding.inflate(inflater, container, false)
        adapter = ActiveEventAdapter()
        vm.getEventsAvailablesByEntityId(Firebase.auth.currentUser?.uid.toString())
        vm.getImagesEntityAvailableEvents(Firebase.auth.currentUser?.uid.toString())
        binding.activeEventList.adapter = adapter
        binding.activeEventList.layoutManager = LinearLayoutManager(context)
        binding.activeEventList.setHasFixedSize(true)
        vm.eventsQuery.observe(viewLifecycleOwner){ list->
            adapter.setList(list)
        }
        vm.uriEventsEntity.observe(viewLifecycleOwner){uris->
            adapter.setUris(uris)
        }
        return binding.root
    }
    companion object{
        fun newInstance(): ActiveEventFragment{
            return ActiveEventFragment()
        }
    }
}