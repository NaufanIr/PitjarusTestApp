package com.example.pitjarustestapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pitjarustestapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Home"

        binding.btnVisit.setOnClickListener {
            val intent = Intent(this, ListStoreActivity::class.java)
            startActivity(intent)
        }
        binding.btnDashboard.setOnClickListener {
            val intent = Intent(this, StoreDetailActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}