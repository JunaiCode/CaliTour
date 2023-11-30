package com.example.calitour.activities.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.calitour.databinding.UserProfileFragmentBinding
import com.example.calitour.viewmodel.UserViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File

class UserProfileFragment: Fragment() {

    val viewModel : UserViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = UserProfileFragmentBinding.inflate(inflater, container, false)
        viewModel.getUser()
        viewModel._user.observe(viewLifecycleOwner){
            binding.userName.text = it.name
        }

        /*viewModel._user.observe(viewLifecycleOwner){
            Glide.with(this@UserProfileFragment)
                .load(createPath(it.photoUri))
                .into(binding.userIV)
        }*/


        return binding.root
    }

    fun createPath(id : String) : String {
        return "https://firebasestorage.googleapis.com/v0/b/calitour.appspot.com/o/profileImages%2F${id}?alt=media&token=7207e3a5-8514-4831-87a7-58e100b28efb"
    }

    companion object{
        fun newInstance(): UserProfileFragment {
            return UserProfileFragment()
        }
    }
}