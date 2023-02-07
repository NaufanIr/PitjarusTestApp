package com.example.pitjarustestapp.storedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pitjarustestapp.data.StoreRepository
import kotlinx.coroutines.launch

class StoreDetailViewModel(private val storeRepository: StoreRepository): ViewModel() {
    fun getData(id: Int) = storeRepository.getStoreById(id)

    fun visitStore(id: Int, visitDate: String, visitPhoto: String, isVisited: Boolean = true) {
        viewModelScope.launch {
            storeRepository.updateStoreVisitStatus(id, visitDate, visitPhoto, isVisited)
        }
    }
}