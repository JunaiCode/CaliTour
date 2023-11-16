package com.example.calitour.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.calitour.R
import com.example.calitour.activities.fragments.SignInFragment
import com.example.calitour.activities.fragments.SignUpFragment
import com.example.calitour.databinding.ActivitySignInsignUpBinding

class SignInSignUpActivity : AppCompatActivity() {

    private val binding:ActivitySignInsignUpBinding by lazy {
        ActivitySignInsignUpBinding.inflate(layoutInflater)
    }

    private val signin = SignInFragment()
    private val signup = SignUpFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showFragment(signin)
        setContentView(binding.root)
    }

    fun showFragment(fragment: Fragment){

        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit()

    }
}