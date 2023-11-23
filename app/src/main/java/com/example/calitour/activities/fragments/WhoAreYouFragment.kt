package com.example.calitour.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calitour.activities.SignInSignUpActivity
import com.example.calitour.databinding.WhoAreYouFragmentBinding

class WhoAreYouFragment: Fragment() {

    private val signup: SignUpFragment = SignUpFragment.newInstance()
    private val signupEntity: SignUpEntityFragment = SignUpEntityFragment.newInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: WhoAreYouFragmentBinding = WhoAreYouFragmentBinding.inflate(inflater, container, false)

        binding.imEntity.setOnClickListener {
            (activity as SignInSignUpActivity).showFragmentAdd(signupEntity)

        }

        binding.imUser.setOnClickListener {
            (activity as SignInSignUpActivity).showFragmentAdd(signup)

        }

        return binding.root
    }



    companion object{
        fun newInstance():Fragment {
            return WhoAreYouFragment()
        }
    }
}