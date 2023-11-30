package com.example.calitour.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calitour.components.adapter.InactiveEventAdapter
import com.example.calitour.databinding.InactiveEventFragmentBinding
import com.example.calitour.viewmodel.EntityViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class InactiveEventFragment: Fragment() {
    private val vm = EntityViewModel()
    private lateinit var  binding: InactiveEventFragmentBinding
    private lateinit var  adapter: InactiveEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = InactiveEventFragmentBinding.inflate(inflater, container, false)
        adapter = InactiveEventAdapter()
        vm.getEventsUnavailablesByEntityId(Firebase.auth.currentUser?.uid.toString())
        binding.inactiveEventList.adapter = adapter
        binding.inactiveEventList.layoutManager = LinearLayoutManager(context)
        binding.inactiveEventList.setHasFixedSize(true)
        vm.eventsQuery.observe(viewLifecycleOwner){ list->
            adapter.setList(list)
        }
        return binding.root
    }
    companion object{
        fun newInstance(): InactiveEventFragment{
            return InactiveEventFragment()
        }
    }
}