package com.example.pitjarustestapp.home

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.pitjarustestapp.SessionPref
import com.example.pitjarustestapp.ViewModelFactory
import com.example.pitjarustestapp.liststore.ListStoreActivity
import com.example.pitjarustestapp.storedetail.StoreDetailActivity
import com.example.pitjarustestapp.databinding.ActivityMainBinding
import com.example.pitjarustestapp.login.LoginActivity
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]

        setupView()
        viewActionPerform()
    }

    private fun setupView() {
        viewModel.getTotalStore().observe(this) { totalStore ->
            binding.tvCardTotal.text = totalStore.toString()
            viewModel.getActualStore().observe(this) { actualStore ->
                binding.tvCardActual.text = actualStore.toString()
                val percentage = "${actualStore.times(100).toFloat().div(totalStore).toInt()}%"
                binding.tvCardPercentage.text = percentage
            }
        }
    }

    private fun viewActionPerform() {

        //GO TO STORE LIST PAGE
        binding.btnVisit.setOnClickListener {
            val intent = Intent(this, ListStoreActivity::class.java)
            startActivity(intent)
        }

        //GO TO LOGIN PAGE
        binding.btnLogout.setOnClickListener {
            viewModel.setSession(false)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}