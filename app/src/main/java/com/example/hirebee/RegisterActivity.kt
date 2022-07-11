package com.example.hirebee

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.hirebee.databinding.ActivityMainBinding
import com.example.hirebee.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.security.auth.login.LoginException

class RegisterActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        binding.registerBtn.setOnClickListener {
            validateData()
        }
        binding.signinBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private var name = ""
    private var surname = ""
    private var password = ""
    private var dob = ""
    private var email = ""
    private var mobile = ""

    private fun validateData() {
        name = binding.nameEt.text.toString().trim()
        surname = binding.surnameEt.text.toString().trim()
        dob = binding.dobEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()
        email = binding.emailEt.text.toString().trim()
        mobile = binding.mobileEt.text.toString().trim()
        val cPassword = binding.cpasswordEt.text.toString().trim()
        if(name.isEmpty()){
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show()
        }else if(surname.isEmpty()){
            Toast.makeText(this, "Enter your name", Toast.LENGTH_SHORT).show()
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
            Toast.makeText(this, "Invalid Email...", Toast.LENGTH_SHORT).show()
        else if (password.isEmpty()){
            Toast.makeText(this, "Enter Password...", Toast.LENGTH_SHORT).show()
        }
        else if (dob.isEmpty()){
            Toast.makeText(this, "Enter Date of Birth...", Toast.LENGTH_SHORT).show()
        }
        else if (mobile.isEmpty()){
            Toast.makeText(this, "Enter Date of Birth...", Toast.LENGTH_SHORT).show()
        }
        else if (cPassword.isEmpty()){
            Toast.makeText(this, "Confirm Password...", Toast.LENGTH_SHORT).show()
        }
        else if (password != cPassword){
            Toast.makeText(this, "Passwords Not matching...", Toast.LENGTH_SHORT).show()
        } else {
            createUserAccount()
        }
    }

    private fun createUserAccount() {
        progressDialog.setTitle("Please wait...")
        progressDialog.show()
        firebaseAuth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                updateUserInfo()
                Toast.makeText(this, "Created Successfully...", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to Create Account...", Toast.LENGTH_SHORT).show()

            }
    }

    private fun updateUserInfo() {
        progressDialog.setMessage("Saving user info...")

        val timestamp = System.currentTimeMillis()

        val uid = firebaseAuth.uid

        val hashMap: HashMap<String, Any?> = HashMap()

        hashMap["uid"] = uid
        hashMap["email"] = email
        hashMap["name"] = name
        hashMap["surname"] = surname
        hashMap["dob"] = dob
        hashMap["password"] = password
        hashMap["mobile"] = mobile
        hashMap["userType"] = "user"
        hashMap["timestamp"] = timestamp

        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(uid!!)
            .setValue(hashMap)
            .addOnSuccessListener {
                progressDialog.dismiss()
                Toast.makeText(this, "Created Successfully...", Toast.LENGTH_SHORT).show()
                checkUser()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to Create Account due to${e.message}...", Toast.LENGTH_SHORT).show()
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
                        startActivity(Intent(this@RegisterActivity, MyActivity::class.java))
                    }
                    else if (userType == "admin"){
                        startActivity(Intent(this@RegisterActivity,DashboardUserActivity::class.java))
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}