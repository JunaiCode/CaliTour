package com.example.calitour.activities.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.calitour.databinding.SignUpFragmentBinding

class SignUpFragment: Fragment() {

    private var _binding: SignUpFragmentBinding? = null
    private val binding get() = _binding!!

    val launcher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
        ::onGalleryResult
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SignUpFragmentBinding.inflate(inflater, container, false)
        _binding!!.profileImg.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            launcher.launch(intent)

        }
        return binding.root
    }

    fun onGalleryResult(result: ActivityResult){
        val uri = result.data?.data
        Glide.with(this).load(uri).into(binding.profileImg)
        uri?.let{
        }
    }


    companion object{
        fun newInstance(): SignInFragment {
            return SignInFragment()
        }
    }
}