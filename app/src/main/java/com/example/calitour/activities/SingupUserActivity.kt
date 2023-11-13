package com.example.calitour.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.calitour.R
import com.example.calitour.databinding.ActivityLoginBinding
import com.example.calitour.databinding.ActivitySingupUserBinding

class SingupUserActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySingupUserBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingupUserBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.registerBtn.setOnClickListener {
            var name = binding.nameTIR.text.toString();
            var password = binding.passwordTIR.text.toString();
            var confPass = binding.confirmPassTIR.text.toString()
            var email = binding.emailTIR.text.toString();
            var date = binding.dateTIR.text.toString();
            var phoneNumber = binding.phoneTIR.text.toString()
        }

        binding.haveAccTV.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}