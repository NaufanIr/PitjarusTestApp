package com.example.pitjarustestapp.liststore

import androidx.lifecycle.ViewModel
import com.example.pitjarustestapp.data.StoreRepository

class ListStoreViewModel(private val storeRepository: StoreRepository): ViewModel() {
    fun getStores() = storeRepository.getStores()


}