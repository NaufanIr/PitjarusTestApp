package com.example.pitjarustestapp.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.pitjarustestapp.ViewModelFactory
import com.example.pitjarustestapp.data.local.StoreEntity
import com.example.pitjarustestapp.databinding.ActivityLoginBinding
import com.example.pitjarustestapp.home.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        //Disable Night Mode Layout
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@LoginActivity)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            val username = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                viewModel.login(username, password).observe(this) { response ->
                    if (response.stores != null && response.stores.isNotEmpty()) {
                        val storesResponse = response.stores
                        viewModel.getDataCount().observe(this) { dataCount ->
                            if (dataCount == 0) {
                                val storeList = storesResponse.map {
                                    StoreEntity(
                                        id = null,
                                        isVisited = false,
                                        visitPhoto = null,
                                        visitDate = null,
                                        storeId = it.storeId,
                                        storeCode = it.storeCode,
                                        storeName = it.storeName,
                                        address = it.address,
                                        dcId = it.dcId,
                                        dcName = it.dcName,
                                        accountId = it.accountId,
                                        accountName = it.accountName,
                                        subchannelId = it.subchannelId,
                                        subchannelName = it.subchannelName,
                                        channelId = it.channelId,
                                        channelName = it.channelName,
                                        areaId = it.areaId,
                                        areaName = it.areaName,
                                        regionId = it.regionId,
                                        regionName = it.regionName,
                                        longitude = it.longitude,
                                        latitude = it.latitude
                                    )
                                }
                                viewModel.insertDataToDatabase(storeList)
                            }
                        }
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        AlertDialog
                            .Builder(this@LoginActivity)
                            .setTitle("Tidak dapat login, ${response.message}")
                            .setPositiveButton("Kembali") { listener, _ -> listener.dismiss() }
                            .show()
                    }
                }
            } else {
                AlertDialog
                    .Builder(this@LoginActivity)
                    .setTitle("Username/Password tidak boleh kosong!")
                    .setPositiveButton("Kembali") { listener, _ -> listener.dismiss() }
                    .show()
            }
        }
    }


//        val pref = SessionPref.getInstance(dataStore)
//        val viewModel = ViewModelProvider(this, ViewModelFactory(pref))[LoginViewModel::class.java]
//
//        viewModel.getSession().observe(this) {
//            Log.d("Sessions", "==========EXECUTION1==========")
//            if (it) {
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//                //finishAffinity()
//            }
//        }
//
//        binding.btnLogin.setOnClickListener {
//            Log.d("Sessions", "==========EXECUTION2==========")
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//            finish()
////            viewModel.setSession(true)
//        }
//    }
}