package com.example.pitjarustestapp.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pitjarustestapp.SessionPref
import com.example.pitjarustestapp.data.StoreRepository
import com.example.pitjarustestapp.data.local.StoreEntity
import kotlinx.coroutines.launch
import retrofit2.Response

class LoginViewModel(private val storeRepository: StoreRepository) : ViewModel() {

    //FIRST LOGIN METHOD
    //fun login2(username: String, password: String) = storeRepository.login(username, password)

    fun login(username: String, password: String) = storeRepository.login(username, password)

    fun getDataCount() = storeRepository.getDataCount()

    fun insertDataToDatabase(storeData: List<StoreEntity>) {
        viewModelScope.launch {
            storeRepository.insertStores(storeData)
        }
    }
}

//class LoginViewModel(private val pref: SessionPref): ViewModel() {
//
//    fun setSession(isLoggedIn: Boolean) {
//        Log.d("Sessions", "==========SET SESSION==========")
//        viewModelScope.launch {
//            pref.setSession(isLoggedIn)
//        }
//    }
//
//    fun getSession(): LiveData<Boolean> {
//        Log.d("Sessions", "==========GET SESSION==========")
//        return pref.getSession().asLiveData()
//    }
//}