package com.example.hirebee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hirebee.databinding.ActivityMyBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
        var bottomNavigationView: BottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnNavigationItemSelectedListener() {
            when(it.itemId){
                R.id.nav_vacancy -> {
                    startActivity(Intent(this@MyActivity, DashboardActivity::class.java))
                }
//                R.id.nav_search -> {
//                    startActivity(Intent(this@MyActivity, SearchActivity::class.java))
//                }
//                R.id.nav_profile -> {
//                    startActivity(Intent(this@MyActivity, ProfileActivity::class.java))
//                }

            }
            true
        }

    }
}