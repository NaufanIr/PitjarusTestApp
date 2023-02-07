package com.example.pitjarustestapp.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pitjarustestapp.data.StoreRepository
import com.example.pitjarustestapp.data.local.StoreEntity
import kotlinx.coroutines.launch

class LoginViewModel(private val storeRepository: StoreRepository) : ViewModel() {

    val loginSession = storeRepository.session

    fun setSession(isLoggedIn: Boolean) {
        viewModelScope.launch {
            storeRepository.setSession(isLoggedIn)
        }
    }

    fun login(username: String, password: String) = storeRepository.login(username, password)

    fun getDataCount() = storeRepository.getDataCount()

    fun insertDataToDatabase(storeData: List<StoreEntity>) {
        viewModelScope.launch {
            storeRepository.insertStores(storeData)
        }
    }
}