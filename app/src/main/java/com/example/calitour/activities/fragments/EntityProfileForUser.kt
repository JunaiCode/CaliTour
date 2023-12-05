package com.example.calitour.activities.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.calitour.databinding.EntityProfileFragmentBinding
import com.example.calitour.viewmodel.EntityViewModel

class EntityProfileForUser: Fragment() {

    private lateinit var binding: EntityProfileFragmentBinding
    private val vm: EntityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EntityProfileFragmentBinding.inflate(inflater, container, false)

        val extras = arguments?.getString("entity_id")
        Log.e("<<<", extras.toString())
        if (extras != null) {
            vm.getEntityProfile(extras)
        }

        vm.profile.observe(viewLifecycleOwner){profile ->
            binding.userEntityName.text = profile.name
            binding.userDescriptionEntityTV.text = profile.description
            setSocialMedia(binding.userFacebook, profile.facebook)
            setSocialMedia(binding.userTwitter, profile.x)
            setSocialMedia(binding.userInstagram, profile.instagram)

            if(profile.photoID!=""){
                Glide.with(this).load(profile.photoID).into(binding.userEntityIV)
            }
        }


        return binding.root
    }

    fun setSocialMedia(button: ImageView, url:String){
        if(url!=""){
            button.setOnClickListener{
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
        }

    }

    companion object {
        fun newInstance(): EntityProfileForUser {
            return EntityProfileForUser()
        }
    }
}