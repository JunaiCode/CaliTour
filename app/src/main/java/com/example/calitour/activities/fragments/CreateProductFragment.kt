package com.example.calitour.activities.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.calitour.databinding.CreateProductFragmentBinding
import com.example.calitour.model.entity.EntityProduct
import com.example.calitour.viewmodel.EntityViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CreateProductFragment: Fragment() {
    private lateinit var binding: CreateProductFragmentBinding
    private val vm: EntityViewModel by activityViewModels()
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

            if(!validateInputs()){
                val newProduct = EntityProduct(
                    binding.productNameTI.editText?.text.toString(),
                    "",
                    uri,
                    binding.productDescripTI.editText?.text.toString(),
                    Integer.parseInt(binding.productPriceTI.editText?.text.toString())
                )
                val entityID = Firebase.auth.currentUser?.uid.toString()

                vm.createProduct(newProduct, entityID)
            }
        }
        return binding.root
    }

    fun onGalleryResult(result: ActivityResult){

        if(result.resultCode != Activity.RESULT_CANCELED){
            uri = result.data?.data!!
            Glide.with(this).load(uri).into(binding.productImage)
        }
    }

    fun validateInputs(): Boolean {
        var hasEmptyField = false

        val textInputLayouts = listOf(
            binding.productNameTI,
            binding.productDescripTI,
            binding.productPriceTI
        )
        for (textInputLayout in textInputLayouts) {
            val editText = textInputLayout.editText
            if (editText != null && editText.text.isEmpty()) {
                textInputLayout.error = "This field cannot be empty"
                hasEmptyField = true
            }
        }

        return hasEmptyField
    }

    companion object{
        fun newInstance():CreateProductFragment{
            return CreateProductFragment()
        }
    }
}