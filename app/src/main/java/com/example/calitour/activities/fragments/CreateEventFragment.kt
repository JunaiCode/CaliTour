package com.example.calitour.activities.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import com.example.calitour.R
import com.example.calitour.components.adapter.CategoriesAdapterSpinner
import com.example.calitour.databinding.CreateEventFragmentBinding


class CreateEventFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val appContext = requireContext()
        val binding: CreateEventFragmentBinding = CreateEventFragmentBinding.inflate(inflater,container,false)
        val categories = resources.getStringArray(R.array.categories)
        val adapter = CategoriesAdapterSpinner(appContext, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categories.adapter = adapter

        binding.categories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    Log.e("Posicion seleccionada",position.toString())
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        return binding.root
    }

    companion object{
        fun newInstance():CreateEventFragment{
            return CreateEventFragment()
        }
    }
}