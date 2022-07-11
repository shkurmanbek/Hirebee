package com.example.hirebee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.example.hirebee.databinding.ActivityDashboardBinding
import com.example.hirebee.databinding.ActivityDashboardUserBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDashboardBinding

    private lateinit var firebaseAuth: FirebaseAuth

    //array list to hold categories

    private lateinit var  categoryArrayList: ArrayList<ModelCategory>

    private lateinit var adapterCategory: AdapterCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        loadCategories()

        //search
        binding.searchEt.addTextChangedListener(object: TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //called as and when user type anything
                try {
                    adapterCategory.filter.filter(s)
                } catch(e:Exception){

                }
            }
            override fun afterTextChanged(s: Editable?) {

            }
        })
        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }
        binding.accBtn.setOnClickListener {
            startActivity(Intent(this@DashboardActivity, MyRequestActivity::class.java))
        }
        var bottomNavigationView: BottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener() {
            when(it.itemId){
                R.id.nav_vacancy -> {
                    val intent = Intent(this@DashboardActivity, EmployerActivity::class.java)
                    intent.putExtra("categoryId", "1639122746648")
                    startActivity(intent)
                }
                R.id.nav_search -> {
                    Toast.makeText(this, "You are in this page!", Toast.LENGTH_SHORT).show()
                }
                R.id.nav_profile -> {
                    val intent = Intent(this@DashboardActivity, ProfileActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

    }
    private fun loadCategories() {
        //init arraylist

        categoryArrayList = ArrayList()

        // get all categories from firebase... Firebase DB > Categories
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear list before starting adding data into it
                categoryArrayList.clear()
                for (ds in snapshot.children){
                    var model = ds.getValue(ModelCategory::class.java)
                    categoryArrayList.add(model!!)
                }
                //setup adapter
                adapterCategory = AdapterCategory(this@DashboardActivity, categoryArrayList)
                binding.categoriesRv.adapter = adapterCategory
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun checkUser() {
        val firebaseUser =firebaseAuth.currentUser
        if(firebaseUser==null){
            startActivity(Intent(this@DashboardActivity, MainActivity::class.java))
        } else {
            val email =firebaseUser.email
        }
    }
}