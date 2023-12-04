package com.example.calitour.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.calitour.databinding.EventsFragmentBinding
import com.example.calitour.viewmodel.EventsViewModel

class EventsFragment: Fragment() {

    val viewModel: EventsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = EventsFragmentBinding.inflate(inflater, container, false)
        viewModel.getEvents()

        binding.musicaFilterButton.setOnClickListener(

        )
        binding.baileFilterButton.setOnClickListener(

        )
        binding.arteFilterButton.setOnClickListener(

        )
        binding.literaturaFilterButton.setOnClickListener(

        )
        binding.teatroFilterButton.setOnClickListener(

        )
        binding.cineFilterButton.setOnClickListener(

        )
        binding.deporteFilterButton.setOnClickListener(

        )
        binding.comidaFilterButton.setOnClickListener(
            
        )



        return binding.root
    }






    companion object{
        fun newInstance(): EventsFragment{
            return EventsFragment()
        }
    }
}