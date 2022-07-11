package com.example.hirebee

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import com.example.hirebee.databinding.ActivityMyRequestBinding
import com.example.hirebee.databinding.ActivitySendRequestBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class MyRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyRequestBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private lateinit var imageView: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyRequestBinding.inflate(layoutInflater)

        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }




        initRequest()
    }

    private fun initRequest() {
        val userId = firebaseAuth.currentUser!!.uid

        val ref = FirebaseDatabase.getInstance().getReference("Requests")
        ref.child(userId).addValueEventListener( object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.nameTv.text = snapshot.child("name").getValue(String::class.java).toString()
                binding.surnameTv.text = snapshot.child("surname").getValue(String::class.java).toString()
                binding.dobTv.text = snapshot.child("dob").getValue(String::class.java).toString()
                binding.mobileTv.text = snapshot.child("mobile").getValue(String::class.java).toString()
                binding.companyTv.text = "Компания: " + snapshot.child("company").getValue(String::class.java).toString()
                binding.timeTv.text = "Загруженность: " +snapshot.child("time").getValue(String::class.java).toString()
                binding.priceTv.text = "Ставка за час: " +snapshot.child("price").getValue(Int::class.java).toString()
                binding.categoryTv.text = "Специальность: " + snapshot.child("category").getValue(String::class.java).toString()
                imageView = snapshot.child("image").getValue(String::class.java).toString()
                val ref = FirebaseStorage.getInstance().reference.child("Images/$imageView")
                val localfile = File.createTempFile("tempImage", "jpeg")

                ref.getFile(localfile).addOnSuccessListener {
                    val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
                    binding.companyIv.setImageBitmap(bitmap)
                }
                    .addOnFailureListener{
                        Log.d("TAG", "All is not okay")
                    }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}