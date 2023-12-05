package com.example.calitour.activities.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calitour.R
import com.example.calitour.databinding.EventsFragmentBinding

class EventsFragment: Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragment = FilterEventsFragment()
        val binding = EventsFragmentBinding.inflate(inflater, container, false)
        val bundle = Bundle()
        binding.musicaFilterButton.setOnClickListener{
            changeFragment("Música", R.drawable.musica_filter_img)
        }

        binding.baileFilterButton.setOnClickListener{
            changeFragment("Baile", R.drawable.baile_filter_img)
        }

        binding.arteFilterButton.setOnClickListener{
            changeFragment("Arte", R.drawable.arte_filter_img)
        }

        binding.literaturaFilterButton.setOnClickListener{
            changeFragment("Literatura", R.drawable.literatura_filter_img)
        }

        binding.teatroFilterButton.setOnClickListener{
            changeFragment("Teatro", R.drawable.teatro_filter_img)
        }

        binding.cineFilterButton.setOnClickListener{
            changeFragment("Cine", R.drawable.cine_filter_img)
        }

        binding.deporteFilterButton.setOnClickListener{
            changeFragment("Deporte", R.drawable.deporte_filter_img)
        }

        binding.comidaFilterButton.setOnClickListener{
            changeFragment("Comida", R.drawable.comida_filter_img)
        }


        return binding.root
    }

    fun changeFragment(categoryName: String, imageResource: Int){
        Log.e(">>>", "OK")
        val activity = requireActivity()
        val fragmentManager = activity.supportFragmentManager
        val newFragment = FilterEventsFragment.newInstance(categoryName, imageResource)
        fragmentManager.beginTransaction()
            .replace(R.id.fragmentContainerHome, newFragment)
            .addToBackStack(null)
            .commit()
    }


    companion object{
        fun newInstance(): EventsFragment{
            return EventsFragment()
        }
    }


}




