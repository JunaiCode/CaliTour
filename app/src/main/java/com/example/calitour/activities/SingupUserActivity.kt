package com.example.calitour.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.calitour.R
import com.example.calitour.databinding.ActivityLoginBinding
import com.example.calitour.databinding.ActivitySingupUserBinding
import com.example.calitour.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

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
        ).addOnSuccessListener{
            val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser;
            if(binding.passwordTIR.editText?.text==binding.confirmPassTIR.editText?.text){
                val user = User(
                    firebaseUser!!.uid,
                    binding.nameTIR.editText?.text.toString(),
                    firebaseUser!!.email,
                    binding.dateTIR.editText?.text.toString(),
                    binding.phoneTIR.editText?.text.toString(),
                    "0"
                );
                FirebaseFirestore.getInstance().collection("users").document(user.id).set(user);
                startActivity(Intent(this, LoginActivity::class.java))
            }else{
                showPasswordMismatchAlert()
            }
        }
            .addOnFailureListener{
                showEmailAlreadyExists();
            }
    }

    private fun showPasswordMismatchAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Las contraseñas no coinciden. Por favor verifica la información ingresada.")
        builder.setPositiveButton("OK", null)
        val dialog = builder.create()
        dialog.show()
    }

    private fun showEmailAlreadyExists() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Este email ya se encuentra registrado.")
        builder.setPositiveButton("OK", null)
        val dialog = builder.create()
        dialog.show()
    }
}