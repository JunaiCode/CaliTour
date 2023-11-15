package com.example.calitour.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.calitour.R
import com.example.calitour.databinding.ActivityLoginBinding
import com.example.calitour.databinding.ActivityProfileBinding
import com.example.calitour.databinding.ActivitySingupUserBinding
import com.example.calitour.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding;
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
        var email = binding.emailTI.text.toString();
        var password = binding.passwordTI.text.toString();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser;
                FirebaseFirestore.getInstance().collection("users").document(firebaseUser!!.uid).get()
                    .addOnSuccessListener {
                        document->
                        var user = document.toObject(User::class.java);
                        saveUser(user);
                        startActivity(Intent(this, ProfileActivity::class.java))
                    }
            }.addOnFailureListener{
                showLoginFailure()
            };


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