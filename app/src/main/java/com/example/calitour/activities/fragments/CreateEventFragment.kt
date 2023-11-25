package com.example.calitour.activities.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.calitour.R
import com.example.calitour.components.adapter.CategoriesAdapterSpinner
import com.example.calitour.databinding.CreateEventFragmentBinding
import com.example.calitour.databinding.CreateProductFragmentBinding
import com.example.calitour.viewmodel.CreateEventProductViewModel


class CreateEventFragment: Fragment() {
    private lateinit var binding: CreateEventFragmentBinding
    private var badgeUri: Uri = Uri.parse("")
    private var eventUri: Uri = Uri.parse("")
    val viewModel: CreateEventProductViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val appContext = requireContext()
        binding = CreateEventFragmentBinding.inflate(inflater,container,false)
        val categories = resources.getStringArray(R.array.categories)
        val adapter = CategoriesAdapterSpinner(appContext, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categories.adapter = adapter

        val launcherBadge = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ::onGalleryResultBadge
        )

        val launcherImage = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ::onGalleryResultImage
        )

        binding.badgeImg.setOnClickListener(){
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            launcherBadge.launch(intent)

        }

        binding.eventImg.setOnClickListener(){
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            launcherImage.launch(intent)
        }

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

        binding.btnCreateEvent.setOnClickListener(){
            viewModel.createEvent()
        }

        return binding.root
    }

    fun onGalleryResultBadge(result: ActivityResult){
        badgeUri = result.data?.data!!
        Glide.with(this).load(badgeUri).into(binding.badgeImg)
    }

    fun onGalleryResultImage(result: ActivityResult){
        eventUri = result.data?.data!!
        Glide.with(this).load(eventUri).into(binding.eventImg)
    }
    companion object{
        fun newInstance():CreateEventFragment{
            return CreateEventFragment()
        }
    }
}