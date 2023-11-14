package com.example.calitour.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.calitour.R
import com.example.calitour.databinding.ActivityLoginBinding
import com.example.calitour.databinding.ActivitySingupUserBinding
import com.google.firebase.auth.FirebaseAuth

class SingupUserActivity : AppCompatActivity() {

    private lateinit var binding:ActivitySingupUserBinding;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingupUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.haveAccTV.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        binding.registerBtn.setOnClickListener(this::register);

    }

    private fun register(view: View) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            binding.emailTIR.editText?.text.toString(),
            binding.passwordTIR.editText?.text.toString()
        )


    }
}