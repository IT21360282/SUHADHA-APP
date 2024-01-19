package com.example.suhadha

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.suhadha.databinding.ActivitySignupBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.io.path.Path

class Signup_Activity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        binding.SignupButton.setOnClickListener {
            val SignupUsername = binding.SignupUsername.text.toString()
            val SignupPassword = binding.SignupPassword.text.toString()

            if (SignupUsername.isNotEmpty() && SignupPassword.isNotEmpty()){
                signupUser(SignupUsername, SignupPassword)
            } else {
                Toast.makeText(this@Signup_Activity, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            }
        }

        binding.LoginRedirect.setOnClickListener {
            startActivity(Intent(this@Signup_Activity, Login_Activity::class.java))
            finish()
        }

    }

    private fun signupUser(username:String, password:String){
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (!dataSnapshot.exists()) {
                    val id = databaseReference.push().key
                    val userData = UserData(id, username, password)
                    databaseReference.child(id!!).setValue(userData)
                    Toast.makeText(this@Signup_Activity, "Registration Successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@Signup_Activity, Login_Activity::class.java))
                    finish()
                } else{
                    Toast.makeText(this@Signup_Activity, "User already exists", Toast.LENGTH_SHORT)
                }
            }
            override fun onCancelled(databaseError: DatabaseError){
                Toast.makeText(this@Signup_Activity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}