package com.example.calitour.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.calitour.databinding.SearchFragmentBinding
import com.example.calitour.viewmodel.EventsViewModel

class FilterEventsFragment: Fragment() {

    val viewModel: EventsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SearchFragmentBinding.inflate(inflater, container, false)
        viewModel.getEvents()
        return binding.root
    }


    companion object{
        fun newInstance(): FilterEventsFragment{
            return FilterEventsFragment()
        }
    }
}