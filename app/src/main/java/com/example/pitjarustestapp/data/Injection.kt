package com.example.pitjarustestapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.pitjarustestapp.data.local.StoreDatabase
import com.example.pitjarustestapp.data.remote.ApiConfig

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

object Injection {
    fun provideRepository(context: Context): StoreRepository {
        val apiService = ApiConfig.getApiService()
        val database = StoreDatabase.getInstance(context)
        val storeDao = database.storeDao()
        return StoreRepository.getInstance(apiService, storeDao)
    }
}