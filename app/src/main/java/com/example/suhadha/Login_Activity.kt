package com.example.suhadha

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.suhadha.databinding.ActivityLoginBinding
import com.example.suhadha.databinding.ActivitySignupBinding
import com.google.firebase.database.*

class Login_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        binding.LoginButton.setOnClickListener {
            val LoginUsername = binding.LoginUsername.text.toString()
            val LoginPassword = binding.LoginPassword.text.toString()

            if (LoginUsername.isNotEmpty() && LoginPassword.isNotEmpty()) {
                loginUser(LoginUsername, LoginPassword)
            } else {
                Toast.makeText(this@Login_Activity, "All fields are mandatory", Toast.LENGTH_SHORT).show()
            }
        }

        binding.SignupRedirect.setOnClickListener {
            startActivity(Intent(this@Login_Activity, Signup_Activity::class.java))
            finish()
        }


    }

    private fun loginUser(username:String, password:String){
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        val userData = userSnapshot.getValue(UserData::class.java)

                        if (userData != null && userData.password == password) {
                            Toast.makeText(this@Login_Activity, "Login Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@Login_Activity, MainActivity::class.java))
                            finish()
                            return
                        }
                    }
                }
                Toast.makeText(this@Login_Activity, "Login Failed", Toast.LENGTH_SHORT).show()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@Login_Activity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
            }
            })
    }
}