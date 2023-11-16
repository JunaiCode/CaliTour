package com.example.calitour.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.example.calitour.databinding.ActivitySingupUserBinding
import com.example.calitour.model.entity.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
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
        if (binding.passwordTIR.editText!!.text.toString()
                .equals(binding.confirmPassTIR.editText!!.text.toString())){

            registerNewUser(binding.emailTIR.editText!!.text.toString()
                ,binding.passwordTIR.editText!!.text.toString()
                ,binding.nameTIR.editText!!.text.toString()
                ,binding.dateTIR.editText!!.text.toString()
                ,binding.phoneTIR.editText!!.text.toString())
        }else{
            showPasswordMismatchAlert()
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

    private fun registerNewUser( email:String, password:String, name: String, birthday : String, phoneNumber:String) {

        Firebase.auth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {

                val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser;
                val userId : String = firebaseUser!!.uid

                val user = User(userId,birthday,email,name,phoneNumber,0)



                Firebase.firestore.collection("users").document(userId).set(user)
                Log.e("user", it.user.toString())
                startActivity(Intent(this, LoginActivity::class.java))
                finish()

            }
            .addOnFailureListener{
                Log.e("Error", it.message.toString())
                showEmailAlreadyExists()
            }


    }
}