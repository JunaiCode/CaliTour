package com.example.calitour.activities.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calitour.activities.SignInSignUpActivity
import com.example.calitour.components.adapter.ActiveEventAdapter
import com.example.calitour.databinding.ActiveEventFragmentBinding
import com.example.calitour.databinding.ActivityCreateEventBinding
import com.example.calitour.databinding.CreateEventFragmentBinding
import com.example.calitour.databinding.WelcomeFragmentBinding
import com.example.calitour.model.DTO.EventDocumentDTO

class ActiveEventFragment: Fragment() {

    private lateinit var  binding: ActiveEventFragmentBinding
    private lateinit var  adapter: ActiveEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ActiveEventFragmentBinding.inflate(inflater, container, false)
        adapter = ActiveEventAdapter()
        binding.activeEventList.adapter = adapter
        Log.e("Si",arguments.toString())
        binding.activeEventList.layoutManager = LinearLayoutManager(context)
        binding.activeEventList.setHasFixedSize(true)

        return binding.root
    }
    companion object{
        fun newInstance(): ActiveEventFragment{
            return ActiveEventFragment()
        }
    }
}