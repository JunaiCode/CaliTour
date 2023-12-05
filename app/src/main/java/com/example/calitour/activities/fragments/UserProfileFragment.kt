package com.example.calitour.activities.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.calitour.components.adapter.HistoricEventAdapter
import com.example.calitour.databinding.UserProfileFragmentBinding
import com.example.calitour.viewmodel.UserViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class UserProfileFragment: Fragment() {

    lateinit var historialAdapter : HistoricEventAdapter
    val viewModel : UserViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = UserProfileFragmentBinding.inflate(inflater, container, false)
        historialAdapter = HistoricEventAdapter()

        binding.historialRV.layoutManager = LinearLayoutManager(requireContext())
        binding.historialRV.setHasFixedSize(false)
        binding.historialRV.adapter = historialAdapter


        Log.e(">>>>", "${historialAdapter.events.size}")

        viewModel.getUser()
        viewModel._user.observe(viewLifecycleOwner){
            binding.userName.text = it.name
            binding.points.text = it.points.toString()
        }

        viewModel._user.observe(viewLifecycleOwner){
            Log.e(">>>>", it.toString())


            Glide.with(this@UserProfileFragment)
                .load(createPath(it.photoID))
                .into(binding.userIV)
        }


        return binding.root
    }

    fun createPath(id : String) : String {
        return "https://firebasestorage.googleapis.com/v0/b/calitour.appspot.com/o/profileImages%2F${id}?alt=media&token=84dab1d6-2144-4a84-b5db-a8525e3b3252"
    }

    companion object{
        fun newInstance(): UserProfileFragment {
            return UserProfileFragment()
        }
    }
}