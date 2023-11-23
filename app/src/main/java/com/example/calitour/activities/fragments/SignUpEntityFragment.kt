package com.example.calitour.activities.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.calitour.activities.MainActivity
import com.example.calitour.databinding.SignUpEntityFragmentBinding
import com.example.calitour.model.entity.Entity
import com.example.calitour.viewmodel.AuthViewModel

class SignUpEntityFragment: Fragment() {

    private var uri: Uri = Uri.parse("")
    private lateinit var binding: SignUpEntityFragmentBinding
    private val vm: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= SignUpEntityFragmentBinding.inflate(inflater, container, false)

        val launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ::onGalleryResult
        )

        binding.profileEntityImg.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            launcher.launch(intent)

        }

        binding.registerEntityBttn.setOnClickListener {
            val newEntity = Entity (
                binding.descriptionInputEntity.editText?.text.toString(),
                binding.emailInputEntity.editText?.text.toString(),
                "",
                binding.nameInputEntity.editText?.text.toString(),
                binding.passwordInputEntity.editText?.text.toString(),
                uri
            )

            vm.signUpEntity(newEntity, binding.passwordInputEntity.editText?.text.toString())
        }

        vm.authStateLV.observe(viewLifecycleOwner){ state ->
            if(state.isAuth){
                startActivity(Intent(requireContext(), MainActivity::class.java))
            }
        }
        vm.errorLV.observe(viewLifecycleOwner){error->
            Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    fun onGalleryResult(result: ActivityResult){

        if(result.resultCode != Activity.RESULT_CANCELED){
            uri = result.data?.data!!
            Glide.with(this).load(uri).into(binding.profileEntityImg)
        }
    }

    companion object{
        fun newInstance(): SignUpEntityFragment {
            return SignUpEntityFragment()
        }
    }

}