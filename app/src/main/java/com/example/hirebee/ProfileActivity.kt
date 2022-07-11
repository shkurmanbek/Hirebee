package com.example.hirebee

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hirebee.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        firebaseAuth = FirebaseAuth.getInstance()

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        initUser()
        initRequest()
    }

    private fun initRequest() {

    }


    private fun initUser() {
        val userId = firebaseAuth.currentUser!!.uid

        val ref = FirebaseDatabase.getInstance().getReference("Users")

        ref.child(userId).addValueEventListener( object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.titleTv2.text = "Name: " + snapshot.child("name").getValue(String::class.java).toString()
                binding.titleTv3.text = "Surname: " + snapshot.child("surname").getValue(String::class.java).toString()
                binding.titleTv4.text = "Date of Birth: " + snapshot.child("dob").getValue(String::class.java).toString()
                binding.titleTv5.text = "Mobile Number: " + snapshot.child("mobile").getValue(String::class.java).toString()
                binding.titleTv6.text = "Email: "+snapshot.child("email").getValue(String::class.java).toString()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

}