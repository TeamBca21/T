package com.example.chatgtp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatgtp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class LoginActivity : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding
    lateinit var database : FirebaseDatabase
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var storage = FirebaseStorage.getInstance().reference.child("Users/profileImages")
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        var progress = ProgressDialog(this@LoginActivity)
        progress.setTitle("Login")
        progress.setMessage("Pleas Wait.\nWe are creating your account...")
        progress.setIcon(R.drawable.img)


        binding.txtReg.setOnClickListener {
            var intent = Intent(this@LoginActivity,RegistrationActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            if(!binding.etEmailLogin.text.toString().equals("") && !binding.etPassLogin.text.toString().equals("")){
                progress.show()
                var emailLogin = binding.etEmailLogin.text.toString()
                var passLogin = binding.etPassLogin.text.toString()
                auth.signInWithEmailAndPassword(emailLogin,passLogin).
                        addOnCompleteListener {task->
                            progress.dismiss()
                            if(task.isSuccessful) {
                                Toast.makeText(this@LoginActivity, "Login Successfully...", Toast.LENGTH_SHORT).show()
                                var intent = Intent(this@LoginActivity, MainActivity::class.java)
                                startActivity(intent)
                            }
                        }
                    .addOnFailureListener {
                        progress.dismiss()
                        Toast.makeText(this@LoginActivity, "You don't have account .\n" + "SignUp First ! ",Toast.LENGTH_SHORT).show()
                    }
            }else{
                Toast.makeText(this@LoginActivity, "Invalid Email or Password!",Toast.LENGTH_SHORT).show()
            }
        }
    }
}