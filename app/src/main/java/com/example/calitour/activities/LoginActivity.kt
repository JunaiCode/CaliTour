package com.example.calitour.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.calitour.R
import com.example.calitour.auth.AuthManager
import com.example.calitour.databinding.ActivityLoginBinding
import com.example.calitour.databinding.ActivityProfileBinding
import com.example.calitour.databinding.ActivitySingupUserBinding
import com.example.calitour.model.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding;
    private val manager : AuthManager = AuthManager()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginBtn.setOnClickListener(this::login)

        binding.noAccTV.setOnClickListener {
            startActivity(Intent(this, SingupUserActivity::class.java))
        }

    }

    private fun login(view: View?) {

        var user = manager.loginManager(binding.emailTI.text.toString(),binding.passwordTI.text.toString())

        if (user != null){
            saveUser(user)
            startActivity(Intent(this, ProfileActivity::class.java))
            finish()
        }else{
            showLoginFailure()
        }
    }



    private fun showLoginFailure() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Usuario o contraseña incorrecto, verifica la información ingresada")
        builder.setPositiveButton("OK", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun saveUser(user: User?){
        val json: String = Gson().toJson(user);
        getSharedPreferences("CaliTour", MODE_PRIVATE).edit().putString("user", json).apply()
    }
}