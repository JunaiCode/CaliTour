package com.example.calitour.activities.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calitour.R
import com.example.calitour.components.adapter.ItineraryEventAdapter
import com.example.calitour.databinding.ItineraryFragmentBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ItineraryFragment: Fragment() {
    lateinit var adapter: ItineraryEventAdapter
    private val itineraryFull = ItineraryFullFragment()
    private val itineraryEmpty = ItineraryEmptyFragment()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.itinerary_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtener la fecha actual y actualizar el TextView
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("EEEE, MMM dd / yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        val binding = ItineraryFragmentBinding.bind(view)
        binding.pageDateTV.text = formattedDate

        adapter = ItineraryEventAdapter()
        if (adapter.itemCount == 0) {
            Log.d("ItineraryFragment", "No hay eventos")
            showFragment(itineraryEmpty)
        } else {
            Log.d("ItineraryFragment", "Hay eventos")
            showFragment(itineraryFull)
        }
    }
    fun showFragment(fragment: Fragment){
        childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }

}