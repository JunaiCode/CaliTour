package com.example.calitour.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calitour.databinding.CreateEventFragmentBinding
import com.example.calitour.databinding.CreateProductFragmentBinding

class CreateProductFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: CreateProductFragmentBinding = CreateProductFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    companion object{
        fun newInstance():CreateProductFragment{
            return CreateProductFragment()
        }
    }
}