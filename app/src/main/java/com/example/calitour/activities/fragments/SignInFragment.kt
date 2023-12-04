package com.example.calitour.activities.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.calitour.activities.HomeActivity
import com.example.calitour.activities.ProfileEntityActivity
import com.example.calitour.activities.SignInSignUpActivity
import com.example.calitour.databinding.SignInFragmentBinding
import com.example.calitour.viewmodel.AuthViewModel

class SignInFragment: Fragment() {

    private val vm: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding:SignInFragmentBinding = SignInFragmentBinding.inflate(inflater, container, false)
        binding.noAccTV.setOnClickListener{
            (activity as SignInSignUpActivity).showFragmentReplace(WhoAreYouFragment.newInstance())
        }
        binding.loginBtn.setOnClickListener {
            vm.signin(
                binding.emailInput.editText?.text.toString(),
                binding.passwordInput.editText?.text.toString()
            )
        }

        vm.authStateLV.observe(viewLifecycleOwner){ state ->
            if(state.isAuth){
                if(state.userType=="user"){
                    startActivity(Intent(requireContext(), HomeActivity::class.java))

                }else {
                    startActivity(Intent(requireContext(), ProfileEntityActivity::class.java))
                }
            }
        }

        vm.errorLV.observe(viewLifecycleOwner){error->
            Toast.makeText(requireContext(), error.message, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    companion object{
        fun newInstance(): SignInFragment{
            return SignInFragment()
        }
    }
}