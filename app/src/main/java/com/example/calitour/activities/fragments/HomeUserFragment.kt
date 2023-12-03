package com.example.calitour.activities.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calitour.components.adapter.UserEventAdapter
import com.example.calitour.databinding.FragmentHomeUserBinding
import com.example.calitour.viewmodel.UserViewModel

class HomeUserFragment : Fragment(), UserEventAdapter.ItemClickListener {

    private lateinit var binding: FragmentHomeUserBinding
    private lateinit var adapter: UserEventAdapter
    val vm: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeUserBinding.inflate(inflater, container, false)
        adapter = UserEventAdapter()
        adapter.setClickListener(this)
        vm.getAllActiveEvents()
        binding.userHomeEventsRV.adapter = adapter
        binding.userHomeEventsRV.layoutManager = LinearLayoutManager(context)
        binding.userHomeEventsRV.setHasFixedSize(true)
        vm.eventsQuery.observe(viewLifecycleOwner){ events ->
            adapter.setEvents(events)
        }
        return binding.root
    }

    override fun interestClickListener(eventId: String) {
        vm.reactToEvent(eventId)
    }


    companion object {
        fun newInstance(): HomeUserFragment {
            return HomeUserFragment()
        }
    }


}