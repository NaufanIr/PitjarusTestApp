package com.example.pitjarustestapp.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pitjarustestapp.SessionPref
import com.example.pitjarustestapp.data.StoreRepository
import kotlinx.coroutines.launch

class MainViewModel(private val storeRepository: StoreRepository) : ViewModel() {
    fun getTotalStore() = storeRepository.getDataCount()
    fun getActualStore() = storeRepository.getTotalActualStore()

    fun setSession(isLoggedIn: Boolean) {
        viewModelScope.launch {
            storeRepository.setSession(isLoggedIn)
        }
    }
}