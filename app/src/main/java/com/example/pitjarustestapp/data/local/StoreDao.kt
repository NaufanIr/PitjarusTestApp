package com.example.pitjarustestapp.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StoreDao {

    @Query("SELECT COUNT(*) FROM store")
    fun getDataCount(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM store WHERE visited = 1")
    fun getTotalActualStore(): LiveData<Int>

    @Query("SELECT * FROM store")
    fun getStores(): LiveData<List<StoreEntity>>

    @Query("SELECT * FROM store WHERE id = :id")
    fun getStoreById(id: Int): LiveData<StoreEntity>

    @Query("SELECT * FROM store WHERE visited = 1")
    fun getVisitedStores(): LiveData<List<StoreEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStores(stores: List<StoreEntity>)

    @Query("UPDATE store SET visited = :isVisited, visit_date = :visitDate, visit_photo = :visitPhoto WHERE id = :id")
    suspend fun updateStoreVisitStatus(
        id: Int,
        visitDate: String,
        visitPhoto: String,
        isVisited: Boolean
    )
}