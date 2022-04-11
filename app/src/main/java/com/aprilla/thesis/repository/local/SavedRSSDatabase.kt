package com.aprilla.thesis.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aprilla.thesis.models.ItemsRSS

@Database(entities = [ItemsRSS::class], version = 3, exportSchema = false)
abstract class SavedRSSDatabase: RoomDatabase() {
    abstract fun rssDao(): SavedRSSDAO
}