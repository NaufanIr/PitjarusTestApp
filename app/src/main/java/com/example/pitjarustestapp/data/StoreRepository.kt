package com.example.pitjarustestapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.example.pitjarustestapp.SessionPref
import com.example.pitjarustestapp.data.local.StoreDao
import com.example.pitjarustestapp.data.local.StoreEntity
import com.example.pitjarustestapp.data.remote.ApiService
import com.example.pitjarustestapp.data.remote.Response

class StoreRepository private constructor(
    private val apiService: ApiService,
    private val storeDao: StoreDao,
    private val pref: SessionPref,
) {

    //DATA STORE
    val session = pref.getSession().asLiveData()

    suspend fun setSession(p0: Boolean) {
        pref.setSession(p0)
    }

    //REMOTE DATA API
    fun login(username: String, password: String): LiveData<Response> = liveData {
        val response = apiService.login(username, password)
        emit(response)
    }

    //LOCAL DATA
    fun getDataCount(): LiveData<Int> = storeDao.getDataCount()

    fun getTotalActualStore(): LiveData<Int> = storeDao.getTotalActualStore()

    suspend fun insertStores(stores: List<StoreEntity>) = storeDao.insertStores(stores)

    fun getStores(): LiveData<List<StoreEntity>> = storeDao.getStores()

    fun getStoreById(id: Int): LiveData<StoreEntity> = storeDao.getStoreById(id)

    suspend fun updateStoreVisitStatus(
        id: Int,
        visitDate: String,
        visitPhoto: String,
        isVisited: Boolean
    ) {
        return storeDao.updateStoreVisitStatus(id, visitDate, visitPhoto, isVisited)
    }

    companion object {
        @Volatile
        private var instance: StoreRepository? = null
        fun getInstance(
            apiService: ApiService,
            storeDao: StoreDao,
            pref: SessionPref,
        ): StoreRepository = instance ?: synchronized(this) {
            instance ?: StoreRepository(apiService, storeDao, pref)
        }.also { instance = it }
    }
}