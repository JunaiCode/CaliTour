package com.example.calitour.activities.fragments

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.calitour.databinding.UserProfileFragmentBinding
import com.example.calitour.viewmodel.UserViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.IOException

class UserProfileFragment: Fragment() {

    val viewModel : UserViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = UserProfileFragmentBinding.inflate(inflater, container, false)

        viewModel._user.observe(viewLifecycleOwner){

            val storageReference = Firebase.storage.getReference("/profileImages/${it.photoUri}.jpeg")

            try {
                val localFile = File.createTempFile("user_picture", ".jpeg")
                storageReference.getFile(localFile).addOnSuccessListener {
                    val bitmap : Bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                    binding.userIV.setImageBitmap(bitmap)
                }.addOnFailureListener{
                    Toast.makeText(requireContext(), "No fué posible recuperar la imagen", Toast.LENGTH_LONG)
                }
            }catch (e: IOException){
                println("Haha caiste!!")
            }


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