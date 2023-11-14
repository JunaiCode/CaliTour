package com.example.calitour.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.calitour.R
import com.example.calitour.databinding.ActivityLoginBinding
import com.example.calitour.databinding.ActivitySingupUserBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener {
            var email = binding.emailTI.text.toString();
            var password = binding.passwordTI.text.toString();

        }

        binding.noAccTV.setOnClickListener {
            startActivity(Intent(this, SingupUserActivity::class.java))
        }

    }
}