package com.example.calitour.activities.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calitour.R
import com.example.calitour.databinding.SearchFragmentBinding

class EventsFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = SearchFragmentBinding.inflate(inflater, container, false)

        binding.musicaFilterButton.setOnClickListener{
            Log.d("","OKK")
            //changeFragment()
        }

        binding.baileFilterButton.setOnClickListener{
            //changeFragment()
        }

        binding.arteFilterButton.setOnClickListener{
            //changeFragment()
        }

//        binding.literaturaFilterButton.setOnClickListener(
//            changeFragment()
//        )
//        binding.teatroFilterButton.setOnClickListener(
//            changeFragment()
//        )
//        binding.cineFilterButton.setOnClickListener(
//            changeFragment()
//        )
//        binding.deporteFilterButton.setOnClickListener(
//            changeFragment()
//        )
//        binding.comidaFilterButton.setOnClickListener(
//            changeFragment()
//        )



        return binding.root
    }

    fun changeFragment(){
        Log.e(">>>", "OK")
        val activity = requireActivity()
        val fragmentManager = activity.supportFragmentManager
        val newFragment = FilterEventsFragment()
        newFragment.let {
             fragmentManager.beginTransaction()
                 .replace(R.id.fragmentContainerHome, it)
                 .addToBackStack(null)
                 .commit()
        }
    }


    companion object{
        fun newInstance(): EventsFragment{
            return EventsFragment()
        }
    }


}




