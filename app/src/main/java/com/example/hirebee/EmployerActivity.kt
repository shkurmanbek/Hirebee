package com.example.hirebee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.hirebee.databinding.ActivityEmployerBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EmployerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEmployerBinding
    private lateinit var  employersArrayList: ArrayList<ModelEmployer>
    private lateinit var adapterEmployers: AdapterEmployer
    private var categoryId :String ?= ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmployerBinding.inflate(layoutInflater)

        setContentView(binding.root)
        categoryId = intent.getStringExtra("categoryId")
        Log.d("CATEGORYID", categoryId!!)
        loadEmployersList(categoryId!!)

        binding.logoutBtn.setOnClickListener {
            onBackPressed()
        }

    }
    private fun loadEmployersList(categoryId: String) {
        //init arraylist
        employersArrayList = ArrayList()

        // get all categories from firebase... Firebase DB > Categories
        val ref = FirebaseDatabase.getInstance().getReference("Employers")
        ref.orderByChild("categoryId").equalTo(categoryId).addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear list before starting adding data into it
                employersArrayList.clear()
                for (ds in snapshot.children){
                    var model = ds.getValue(ModelEmployer::class.java)
                    if(model!=null) {
                        employersArrayList.add(model)
                        Log.d("OKAY???", "onDataChange ${model.company} ${model.categoryId}")
                    }
                }
                //setup adapter
                adapterEmployers = AdapterEmployer(this@EmployerActivity, employersArrayList)
                binding.employersRv.adapter = adapterEmployers
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}