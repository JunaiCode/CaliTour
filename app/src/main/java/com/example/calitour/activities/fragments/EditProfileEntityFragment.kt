package com.example.calitour.activities.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.calitour.R
import com.example.calitour.databinding.EditProfileEntityFragmentBinding
import com.example.calitour.model.entity.EntityFirestore
import com.example.calitour.viewmodel.EntityViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EditProfileEntityFragment: Fragment() {

    var profile: EntityFirestore? = null
    val vm:EntityViewModel by activityViewModels()
    var binding: EditProfileEntityFragmentBinding? = null
    private var uri: Uri = Uri.parse("")


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = EditProfileEntityFragmentBinding.inflate(inflater, container, false)

        val launcher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ::onGalleryResult
        )

        profile = arguments?.getSerializable("profile", EntityFirestore::class.java)

        Log.e("<<<", profile.toString())

        if (profile != null){
            if(profile?.photoID!=""){
                Glide.with(this).load(profile?.photoID).into(binding!!.entityEditIV)
            }
            binding!!.entityNameEdit.text = profile?.name
            binding!!.nameInputEntityEdit.editText?.setText(profile?.name)
            binding!!.descriptionInputEntityEdit.editText?.setText(profile?.description)
            binding!!.facebookInputEntityEdit.editText?.setText(profile?.facebook)
            binding!!.twitterInputEntityEdit.editText?.setText(profile?.x)
            binding!!.instagramInputEntityEdit.editText?.setText(profile?.instagram)
        }
        vm.profile.observe(viewLifecycleOwner){
            activity?.finish()
        }
        binding!!.saveChangesEntityProfile.setOnClickListener {
            val newUser = EntityFirestore(
                binding!!.descriptionInputEntityEdit.editText?.text.toString(),
                Firebase.auth.currentUser?.email.toString(),
                Firebase.auth.currentUser?.uid.toString(),
                binding!!.nameInputEntityEdit.editText?.text.toString(),
                uri.toString(),
                binding!!.facebookInputEntityEdit.editText?.text.toString(),
                binding!!.twitterInputEntityEdit.editText?.text.toString(),
                binding!!.instagramInputEntityEdit.editText?.text.toString()
            )

            if(!compareEntities(profile!! , newUser)){
                vm.updateEntity(newUser)
                Log.e("<<<", "hay cambios")
            }else {
                Toast.makeText(requireContext(), "No hay cambios", Toast.LENGTH_SHORT).show()
                activity?.finish()
            }

        }

        binding!!.entityEditIV.setOnClickListener{
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            launcher.launch(intent)
        }

        binding!!.entityEditIV.setColorFilter(
            ContextCompat.getColor(
            requireContext(),
            R.color.black_opacity),
            PorterDuff.Mode.SRC_OVER
        );


        return binding!!.root
    }


    fun compareEntities(entity1: EntityFirestore, entity2: EntityFirestore): Boolean {
        return entity1.description == entity2.description &&
                entity1.email == entity2.email &&
                entity1.name == entity2.name &&
                entity1.facebook == entity2.facebook &&
                entity1.x == entity2.x &&
                entity1.instagram == entity2.instagram &&
                uri==Uri.parse("")

    }

    fun onGalleryResult(result: ActivityResult){

        if(result.resultCode != Activity.RESULT_CANCELED){
            uri = result.data?.data!!
            Glide.with(this).load(uri).into(binding!!.entityEditIV)
        }
    }

    companion object{
        fun newInstance(): EditProfileEntityFragment{
            return EditProfileEntityFragment()
        }
    }
}