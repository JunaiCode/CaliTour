package com.example.calitour.activities.fragments

import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.calitour.R
import com.example.calitour.databinding.EditProfileEntityFragmentBinding
import com.example.calitour.model.entity.EntityFirestore
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EditProfileEntityFragment: Fragment() {

   var profile: EntityFirestore? = null

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = EditProfileEntityFragmentBinding.inflate(inflater, container, false)

        profile = arguments?.getSerializable("profile", EntityFirestore::class.java)

        Log.e("<<<", profile.toString())

        if (profile != null){
            if(profile?.photoID!=""){
                Glide.with(this).load(profile?.photoID).into(binding.entityEditIV)
            }
            binding.entityNameEdit.text = profile?.name
            binding.nameInputEntityEdit.editText?.setText(profile?.name)
            binding.emailInputEntityEdit.editText?.setText(profile?.email)
            binding.descriptionInputEntityEdit.editText?.setText(profile?.description)
            binding.facebookInputEntityEdit.editText?.setText(profile?.facebook)
            binding.twitterInputEntityEdit.editText?.setText(profile?.x)
            binding.instagramInputEntityEdit.editText?.setText(profile?.instagram)
        }

        binding.saveChangesEntityProfile.setOnClickListener {
            val newUser = EntityFirestore(
                binding.descriptionInputEntityEdit.editText?.text.toString(),
                binding.emailInputEntityEdit.editText?.text.toString(),
                Firebase.auth.currentUser?.uid.toString(),
                binding.nameInputEntityEdit.editText?.text.toString(),
                binding.entityEditIV.tag.toString(),
                binding.facebookInputEntityEdit.editText?.text.toString(),
                binding.twitterInputEntityEdit.editText?.text.toString(),
                binding.instagramInputEntityEdit.editText?.text.toString()
            )

            if(profile?.equals(newUser)==false){

            }

        }



        binding.entityEditIV.setColorFilter(
            ContextCompat.getColor(
            requireContext(),
            R.color.black_opacity),
            PorterDuff.Mode.SRC_OVER
        );

        return binding.root
    }

    companion object{
        fun newInstance(): EditProfileEntityFragment{
            return EditProfileEntityFragment()
        }
    }
}