package com.example.calitour.activities.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.calitour.activities.HomeActivity
import com.example.calitour.databinding.SignUpFragmentBinding
import com.example.calitour.model.entity.User
import com.example.calitour.viewmodel.AuthViewmodel

class SignUpFragment: Fragment() {

    private var _binding: SignUpFragmentBinding? = null
    private val vm: AuthViewmodel by activityViewModels()
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

        _binding!!.registerBttn.setOnClickListener {
            var newUser = User(
                "",
                _binding!!.birthdayIT.editText?.text.toString(),
                _binding!!.emailIT.editText?.text.toString(),
                _binding!!.nameInput.editText?.text.toString(),
                _binding!!.phoneIT.editText?.text.toString(),
                0
            )
            vm.signup(newUser, _binding!!.passwordInput.editText?.text.toString())
        }

        vm.authStateLV.observe(viewLifecycleOwner){ state ->
            if(state.isAuth){
                startActivity(Intent(requireContext(), HomeActivity::class.java))
            }
        }
        vm.errorLV.observe(viewLifecycleOwner){error->
            Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    fun onGalleryResult(result: ActivityResult){
        val uri = result.data?.data
        Glide.with(this).load(uri).into(binding.profileImg)
        uri?.let{
        }
    }



    //private fun createAlert()
    private fun showEmailAlreadyExists() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage("Este email ya se encuentra registrado.")
        builder.setPositiveButton("OK", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun showPasswordMismatchAlert() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage("Las contraseñas no coinciden. Por favor verifica la información ingresada.")
        builder.setPositiveButton("OK", null)
        val dialog = builder.create()
        dialog.show()
    }

    companion object{
        fun newInstance(): SignInFragment {
            return SignInFragment()
        }
    }
}