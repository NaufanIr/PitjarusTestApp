package com.example.pitjarustestapp.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.pitjarustestapp.data.local.StoreDao
import com.example.pitjarustestapp.data.local.StoreEntity
import com.example.pitjarustestapp.data.remote.ApiService
import com.example.pitjarustestapp.data.remote.Response

class StoreRepository private constructor(
    private val apiService: ApiService,
    private val storeDao: StoreDao
) {
    fun login(username: String, password: String): LiveData<Response> = liveData {
        val response = apiService.login(username, password)
        emit(response)
    }

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
            storeDao: StoreDao
        ): StoreRepository = instance ?: synchronized(this) {
            instance ?: StoreRepository(apiService, storeDao)
        }.also { instance = it }
    }
}