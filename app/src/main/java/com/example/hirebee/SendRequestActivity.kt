package com.example.hirebee

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.hirebee.databinding.ActivitySendRequestBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class SendRequestActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySendRequestBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: ProgressDialog
    private var category = ""
    private var categoryId = ""
    private var company = ""
    private var price = 0
    private var time = ""
    private var image = ""

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendRequestBinding.inflate(layoutInflater)

        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        categoryId = intent.getStringExtra("categoryId").toString()
        category = intent.getStringExtra("category").toString()
        company = intent.getStringExtra("company").toString()
        price = intent.getIntExtra("price",0)
        time = intent.getStringExtra("time").toString()
        image = intent.getStringExtra("imageUrl").toString()

        initCompany()
        binding.registerBtn.setOnClickListener {
            validateData()
        }
        binding.categoryTv.text = "Специальность: " +category
        binding.companyTv.text = "Компания: " +company
        binding.priceTv.text = "Оплата за час: " +price.toString()
        binding.timeTv.text = "Загруженность: " +time
        val ref = FirebaseStorage.getInstance().reference.child("Images/$image")
        val localfile = File.createTempFile("tempImage", "jpeg")

        ref.getFile(localfile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localfile.absolutePath)
            binding.companyIv.setImageBitmap(bitmap)
        }
            .addOnFailureListener{
                Log.d("TAG", "All is not okay")
            }
        Log.d("TAG", "All is okay")

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initCompany() {
        val userId = firebaseAuth.currentUser!!.uid

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(userId).addValueEventListener( object : ValueEventListener {
            @SuppressLint("SetTextI18n")
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.nameTv.text = "Имя: " +snapshot.child("name").getValue(String::class.java).toString()
                binding.surnameTv.text = "Фамилия: " +snapshot.child("surname").getValue(String::class.java).toString()
                binding.dobTv.text = "Дата рождения: " + snapshot.child("dob").getValue(String::class.java).toString()
                binding.mobileTv.text = "Мобильный номер: " + snapshot.child("mobile").getValue(String::class.java).toString()
                binding.emailTv.text = "Эл. почта: " + snapshot.child("email").getValue(String::class.java).toString()
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    private var name = ""
    private var surname = ""
    private var dob = ""
    private var mobile = ""
    private var email = ""
    private fun validateData() {
        name = binding.nameTv.text.toString().trim()
        surname = binding.surnameTv.text.toString().trim()
        dob = binding.dobTv.text.toString().trim()
        mobile = binding.mobileTv.text.toString().trim()
        email = binding.emailTv.text.toString().trim()
        updateUserInfo()
        
    }

    private fun updateUserInfo() {
        progressDialog.setMessage("Saving user info...")

        val timestamp = System.currentTimeMillis()

        val uid = firebaseAuth.uid

        val hashMap: HashMap<String, Any?> = HashMap()

        hashMap["uid"] = uid
        hashMap["name"] = name
        hashMap["surname"] = surname
        hashMap["dob"] = dob
        hashMap["mobile"] = mobile
        hashMap["email"] = email
        hashMap["categoryId"] = categoryId
        hashMap["category"] = category
        hashMap["company"] = company
        hashMap["price"] = price
        hashMap["time"] = time
        hashMap["image"] = image

        val ref = FirebaseDatabase.getInstance().getReference("Requests")
        ref.child("$uid")
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Created Successfully...", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,DashboardUserActivity::class.java))
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to Create Account due to${e.message}...", Toast.LENGTH_SHORT).show()
            }
    }
}