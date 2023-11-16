package com.example.calitour.activities.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.calitour.R
import com.example.calitour.databinding.SignInFragmentBinding

class SignInFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding:SignInFragmentBinding = SignInFragmentBinding.inflate(inflater, container, false)
        binding.noAccTV.setOnClickListener{
            activity?.
                supportFragmentManager?.
                beginTransaction()?.
                add(R.id.fragmentContainer, SignUpFragment())?.
                addToBackStack(null)?.
                commit()

        }
        return binding.root
    }

    companion object{
        fun newInstance(): SignInFragment{
            return SignInFragment()
        }
    }
}