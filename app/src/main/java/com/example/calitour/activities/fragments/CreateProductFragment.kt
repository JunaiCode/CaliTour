package com.example.calitour.activities.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.calitour.databinding.CreateProductFragmentBinding

class CreateProductFragment: Fragment() {
    private lateinit var binding: CreateProductFragmentBinding
    private var uri: Uri = Uri.parse("")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = CreateProductFragmentBinding.inflate(inflater,container,false)

        val launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ::onGalleryResult
        )

        binding.productImage.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            launcher.launch(intent)

        }

        binding.createProductBttn.setOnClickListener {

        }
        return binding.root
    }

    fun onGalleryResult(result: ActivityResult){
        uri = result.data?.data!!
        Glide.with(this).load(uri).into(binding.productImage)
    }

    companion object{
        fun newInstance():CreateProductFragment{
            return CreateProductFragment()
        }
    }
}