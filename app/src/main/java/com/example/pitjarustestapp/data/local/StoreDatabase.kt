package com.example.pitjarustestapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StoreEntity::class], version = 2, exportSchema = false)
abstract class StoreDatabase : RoomDatabase() {
    abstract fun storeDao(): StoreDao

    companion object {
        @Volatile
        private var instance: StoreDatabase? = null
        fun getInstance(context: Context): StoreDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoreDatabase::class.java,
                    "store.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
    }
}