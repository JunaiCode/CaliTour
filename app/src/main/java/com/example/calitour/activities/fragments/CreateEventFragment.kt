package com.example.calitour.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.calitour.R
import com.example.calitour.databinding.CreateEventFragmentBinding


class CreateEventFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val appContext = requireContext()
        val binding: CreateEventFragmentBinding = CreateEventFragmentBinding.inflate(inflater,container,false)
        /*val adapter = ArrayAdapter.createFromResource(
            appContext,
            R.array.categories,
            R.layout.create_event_fragment
        )
        adapter.setDropDownViewResource(R.layout.create_event_fragment);
        binding.categories.adapter = adapter;*/
        return binding.root
    }

    companion object{
        fun newInstance():CreateEventFragment{
            return CreateEventFragment()
        }
    }
}