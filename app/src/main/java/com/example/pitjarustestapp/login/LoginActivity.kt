package com.example.pitjarustestapp.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
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

        val factory: ViewModelFactory = ViewModelFactory.getInstance(this@LoginActivity)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        setupView()

        actionViewPerform()
    }

    private fun setupView() {
        //FORCE DISABLE NIGHT MODE
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()

        //LOADING VIEW
        loadingViewState(true)

        //SESSION CHECKER
        viewModel.loginSession.observeOnce(this) {
            if (it) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                loadingViewState(false)
            }
        }
    }

    private fun actionViewPerform() {
        binding.btnLogin.setOnClickListener {
            val username = binding.edtEmail.text.toString()
            val password = binding.edtPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                loadingViewState(true)
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
                        viewModel.setSession(true)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        loadingViewState(false)
                        AlertDialog
                            .Builder(this@LoginActivity)
                            .setTitle("Tidak dapat login, ${response.message}")
                            .setPositiveButton("Kembali") { listener, _ -> listener.dismiss() }
                            .show()
                    }
                }
            } else {
                loadingViewState(false)
                AlertDialog
                    .Builder(this@LoginActivity)
                    .setTitle("Username/Password tidak boleh kosong!")
                    .setPositiveButton("Kembali") { listener, _ -> listener.dismiss() }
                    .show()
            }
        }
    }

    private fun loadingViewState(isLoading: Boolean) {
        if (isLoading) {
            binding.loading.visibility = View.VISIBLE
            disableEnableControls(false, binding.loginLayout)
        } else {
            binding.loading.visibility = View.INVISIBLE
            disableEnableControls(true, binding.loginLayout)
        }
    }

    private fun disableEnableControls(enable: Boolean, vg: ViewGroup) {
        for (i in 0 until vg.childCount) {
            val child = vg.getChildAt(i)
            child.isEnabled = enable
            if (child is ViewGroup) {
                disableEnableControls(enable, child)
            }
        }
    }

    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }
}