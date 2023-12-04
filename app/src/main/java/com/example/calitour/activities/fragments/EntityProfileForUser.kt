package com.example.calitour.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.calitour.databinding.EntityProfileFragmentBinding
import com.example.calitour.viewmodel.EntityViewModel

class EntityProfileForUser: Fragment() {

    private lateinit var binding: EntityProfileFragmentBinding
    private val vm: EntityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EntityProfileFragmentBinding.inflate(inflater, container, false)

        val extras = activity?.intent?.extras?.getString("entity_id")
        val extras2 = arguments?.getString("entity_id")
        vm.profile.observe(viewLifecycleOwner){profile ->

        }

        if (extras != null) {
            vm.getEntityProfile(extras)
        }

        return binding.root
    }
}