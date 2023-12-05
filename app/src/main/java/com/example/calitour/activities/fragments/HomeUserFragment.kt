package com.example.calitour.activities.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calitour.activities.EventDetailActivity
import com.example.calitour.activities.HomeActivity
import com.example.calitour.components.adapter.UserEventAdapter
import com.example.calitour.databinding.FragmentHomeUserBinding
import com.example.calitour.viewmodel.UserViewModel

class HomeUserFragment : Fragment(), UserEventAdapter.ItemClickListener {

    private lateinit var binding: FragmentHomeUserBinding
    private lateinit var adapter: UserEventAdapter
    private val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ::handleActivityResult
    )
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
        binding.userHomeEventsRV.isNestedScrollingEnabled = false
        vm.eventsQuery.observe(viewLifecycleOwner){ events ->
            adapter.setEvents(events)
        }
        return binding.root
    }

    override fun interestClickListener(eventId: String, operation: String) {
        vm.reactToEvent(eventId, operation)
    }

    override fun showEventDetail(eventId: String) {
        val intent = Intent(context, EventDetailActivity::class.java).putExtra("event_id",eventId)
        launcher.launch(intent)
    }

    fun handleActivityResult(result: ActivityResult){
        val data = result.data?.extras?.getString("entity_id")

        val activity = activity as HomeActivity
        activity.showEntity(data.toString())
    }


    companion object {
        fun newInstance(): HomeUserFragment {
            return HomeUserFragment()
        }
    }


}