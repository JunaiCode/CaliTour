package com.example.calitour.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calitour.activities.SignInSignUpActivity
import com.example.calitour.databinding.WelcomeFragmentBinding

class WelcomeFragment: Fragment() {

    private val signin: SignInFragment = SignInFragment.newInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: WelcomeFragmentBinding = WelcomeFragmentBinding.inflate(inflater, container, false)

        binding.startWelcome.setOnClickListener {
            (activity as SignInSignUpActivity).showFragmentReplace(signin)
        }

        return binding.root
    }

    companion object{
        fun newInstance(): WelcomeFragment{
            return WelcomeFragment()
        }
    }
}