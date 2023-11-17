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

        viewModel._user.observe(viewLifecycleOwner){
            Glide.with(this@UserProfileFragment)
                .load("https://firebasestorage.googleapis.com/v0/b/calitour.appspot.com/o/profileImages%2Fbde9a612-0bc6-4cb2-8396-fd37d899050d?alt=media&token=7207e3a5-8514-4831-87a7-58e100b28efb")
                .into(binding.userIV)



        }

        viewModel.getUser()
        return binding.root
    }

    companion object{
        fun newInstance(): UserProfileFragment {
            return UserProfileFragment()
        }
    }
}