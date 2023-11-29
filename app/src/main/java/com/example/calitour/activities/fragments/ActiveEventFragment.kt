package com.example.calitour.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calitour.activities.SignInSignUpActivity
import com.example.calitour.components.adapter.ActiveEventAdapter
import com.example.calitour.databinding.ActiveEventFragmentBinding
import com.example.calitour.databinding.ActivityCreateEventBinding
import com.example.calitour.databinding.WelcomeFragmentBinding

class ActiveEventFragment: Fragment() {

    private val activeEventFragment: ActiveEventFragment = ActiveEventFragment.newInstance()
    private lateinit var  adapter: ActiveEventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: ActiveEventFragmentBinding = ActiveEventFragmentBinding.inflate(inflater, container, false)
        adapter = ActiveEventAdapter()
        binding.activeEventList.adapter = adapter
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