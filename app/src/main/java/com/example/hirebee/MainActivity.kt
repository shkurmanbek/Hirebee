package com.example.hirebee

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.hirebee.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.loginBtn.setOnClickListener {
            validateData()
        }
        binding.registerBtn.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
    private var password = ""
    private var email = ""
    private fun validateData() {
        password = binding.passwordEt.text.toString().trim()
        email = binding.emailEt.text.toString().trim()
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            Toast.makeText(this, "Invalid Email...", Toast.LENGTH_SHORT).show()
        else if (password.isEmpty()){
            Toast.makeText(this, "Enter Password...", Toast.LENGTH_SHORT).show()
        } else{
            loginUser()
        }
    }

    private fun loginUser() {
        progressDialog.setMessage("Logging wait...")
        progressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialog.dismiss()
                intent = Intent(this,DashboardUserActivity::class.java)
                checkUser()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this, "Login Failed to Create Account due to${e.message}...", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser() {
        progressDialog.setMessage("Checking wait...")
        val firebaseUser = firebaseAuth.currentUser!!

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    progressDialog.dismiss()
                    val userType = snapshot.child("userType").value
                    if (userType == "user"){
                        startActivity(Intent(this@MainActivity, DashboardActivity::class.java))
                    }
                    else if (userType == "admin"){
                        startActivity(Intent(this@MainActivity,DashboardUserActivity::class.java))
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}