package com.example.calitour.activities.fragments

import DatePickerFragment
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
import java.util.Date
import java.util.Locale

class ItineraryFragment : Fragment() {
    lateinit var adapter: ItineraryEventAdapter
    private val itineraryFull = ItineraryFullFragment()
    private val itineraryEmpty = ItineraryEmptyFragment()

    private var currentDate: Date = Calendar.getInstance().time
    private lateinit var binding: ItineraryFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItineraryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateDateTextView()

        adapter = ItineraryEventAdapter()
        if (adapter.itemCount == 0) {
            Log.d("ItineraryFragment", "No hay eventos")
            showFragment(itineraryEmpty)
        } else {
            Log.d("ItineraryFragment", "Hay eventos")
            showFragment(itineraryFull)
        }

        binding.calendarPrevBtn.setOnClickListener {
            currentDate = getPreviousDay(currentDate)
            updateDateTextView()
        }

        binding.calendarNextBtn.setOnClickListener {
            currentDate = getNextDay(currentDate)
            updateDateTextView()
        }

        binding.calendarCardView.setOnClickListener {
            openDatePicker()
        }
    }

    private fun updateDateTextView() {
        if (::binding.isInitialized) {
            val dateFormat = SimpleDateFormat("EEE, MMM dd / yyyy", Locale.getDefault())
            val formattedDate = dateFormat.format(currentDate)
            binding.pageDateTV.text = formattedDate
        }
    }

    private fun getPreviousDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        return calendar.time
    }

    private fun getNextDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        return calendar.time
    }

    fun showFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit()
    }

    private fun openDatePicker() {
        val datePickerFragment = DatePickerFragment()
        datePickerFragment.itineraryFragment = this

        val bundle = Bundle()
        bundle.putLong(DatePickerFragment.SELECTED_DATE_KEY, currentDate.time)
        datePickerFragment.arguments = bundle

        datePickerFragment.show(parentFragmentManager, DatePickerFragment.TAG)
    }

    fun updateDate(updatedDateMillis: Long) {
        currentDate = Date(updatedDateMillis)
        updateDateTextView()
        Log.d("currentDate", currentDate.toString())
    }
}
