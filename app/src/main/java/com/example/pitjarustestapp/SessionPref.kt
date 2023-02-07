package com.example.pitjarustestapp

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SessionPref private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun setSession(isLoggedIn: Boolean) {
        dataStore.edit {
            it[SESSION_KEY] = isLoggedIn
        }
    }

    fun getSession(): Flow<Boolean> {
        return dataStore.data.map {
            it[SESSION_KEY] ?: false
        }
    }

    companion object {
        private val SESSION_KEY = booleanPreferencesKey("session")

        @Volatile
        private var INSTANCE: SessionPref? = null
        fun getInstance(dataStore: DataStore<Preferences>): SessionPref {
            return INSTANCE ?: synchronized(this) {
                val instance = SessionPref(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}