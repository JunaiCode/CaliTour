package com.example.calitour.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.calitour.databinding.FilterEventsFragmentBinding
import com.example.calitour.viewmodel.EventsViewModel

class FilterEventsFragment: Fragment() {

    val viewModel: EventsViewModel by viewModels()
    private lateinit var binding: FilterEventsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FilterEventsFragmentBinding.inflate(inflater, container, false)
        viewModel.getEvents()
        val categoryName = arguments?.getString(ARG_CATEGORY_NAME)
        val imageResourceId = arguments?.getInt(ARG_IMAGE_RESOURCE_ID)
        binding.categoryTitle.text = categoryName
        if (imageResourceId != null) {
            binding.categoryImage.setImageResource(imageResourceId)
        }
        return binding.root
    }



    companion object{
        private const val ARG_CATEGORY_NAME = "categoryName"
        private const val ARG_IMAGE_RESOURCE_ID = "imageResourceId"

        fun newInstance(categoryName: String, imageResourceId: Int): FilterEventsFragment {
            val fragment = FilterEventsFragment()
            val args = Bundle()
            args.putString(ARG_CATEGORY_NAME, categoryName)
            args.putInt(ARG_IMAGE_RESOURCE_ID, imageResourceId)
            fragment.arguments = args
            return fragment
        }

    }
}