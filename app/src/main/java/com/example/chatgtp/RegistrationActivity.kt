package com.example.chatgtp

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatgtp.Classes.Users
import com.example.chatgtp.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class RegistrationActivity : AppCompatActivity() {
    lateinit var binding : ActivityRegistrationBinding
    lateinit var database : FirebaseDatabase
    lateinit var auth : FirebaseAuth
    lateinit var imgUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var storage = FirebaseStorage.getInstance().reference.child("Users/profileImages")
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        var progress = ProgressDialog(this@RegistrationActivity)
        progress.setTitle("Registration")
        progress.setMessage("Pleas Wait.\nWe are creating your account...")
        progress.setIcon(R.drawable.img)



        binding.txtLogin.setOnClickListener {
            var intent = Intent(this@RegistrationActivity,LoginActivity::class.java)
            startActivity(intent)
        }
        binding.userImg.setOnClickListener {
            var intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,1)
        }

        binding.btnREg.setOnClickListener {
            var name = binding.etName.text.toString()
            var email = binding.etEmail.text.toString()
            var pass = binding.etPass.text.toString()
            if(imgUri != null && !binding.etName.text.toString().equals("") && !binding.etEmail.text.toString().equals("") && !binding.etPass.text.toString().equals("")){
                progress.show()
                auth.createUserWithEmailAndPassword(email,pass)
                    .addOnCompleteListener {
                        storage.putFile(imgUri)
                            .addOnCompleteListener {
                                if(it.isSuccessful){
                                    storage.downloadUrl.addOnSuccessListener {task->
                                        var users = Users(name,email,pass,task.toString())
                                            database.getReference("Users").child(auth.uid.toString()).setValue(users)
                                            progress.dismiss()
                                    }
                                }
                            }
                            .addOnFailureListener {
                                progress.dismiss()
                                Toast.makeText(this@RegistrationActivity, "Image Uplading Faild...",Toast.LENGTH_SHORT).show()
                            }
                        var intent = Intent(this@RegistrationActivity, MainActivity::class.java)
                     startActivity(intent)
                        Toast.makeText(this@RegistrationActivity, "Regisration Successfully...",Toast.LENGTH_SHORT).show()
                    }
                .addOnFailureListener {
                    progress.dismiss()
                    Toast.makeText(this@RegistrationActivity, "Registration Faild\nTry again later...",Toast.LENGTH_SHORT).show()
               }
            }else{
                progress.dismiss()
                Toast.makeText(this@RegistrationActivity, "Pleas enter cratiarea !",Toast.LENGTH_SHORT).show()
            }


        }
        if(auth.currentUser != null){
            var intent = Intent(this@RegistrationActivity, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data != null){
            if(data.data != null){
                imgUri = data.data!!
                binding.userImg.setImageURI(imgUri)
            }
        }

    }


}