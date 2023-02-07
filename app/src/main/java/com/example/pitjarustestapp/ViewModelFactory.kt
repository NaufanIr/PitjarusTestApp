package com.example.pitjarustestapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pitjarustestapp.data.Injection
import com.example.pitjarustestapp.data.StoreRepository
import com.example.pitjarustestapp.home.MainViewModel
import com.example.pitjarustestapp.liststore.ListStoreViewModel
import com.example.pitjarustestapp.login.LoginViewModel
import com.example.pitjarustestapp.storedetail.StoreDetailViewModel

class ViewModelFactory private constructor(private val storeRepository: StoreRepository)
    : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(storeRepository) as T
        } else if (modelClass.isAssignableFrom(ListStoreViewModel::class.java)) {
            return ListStoreViewModel(storeRepository) as T
        } else if (modelClass.isAssignableFrom(StoreDetailViewModel::class.java)) {
            return StoreDetailViewModel(storeRepository) as T
        } else if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(storeRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}