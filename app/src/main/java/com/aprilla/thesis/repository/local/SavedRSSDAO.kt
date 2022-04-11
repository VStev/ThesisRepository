package com.aprilla.thesis.repository.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aprilla.thesis.models.ItemsRSS

@Dao
interface SavedRSSDAO {
    @Query("SELECT * FROM saved ORDER BY pubDate DESC")
    fun fetchRss(): LiveData<List<ItemsRSS>>

    @Query("SELECT * FROM saved ORDER BY pubDate DESC")
    fun fetchSavedRss(): LiveData<List<ItemsRSS>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveRss(item: ItemsRSS)

    @Query("UPDATE saved SET isFav = :status WHERE link = :link")
    fun setFavorite(link: String, status: Boolean)

    @Query("DELETE FROM saved WHERE isFav = 0")
    fun clearCache()

    @Delete
    fun deleteRss(item: ItemsRSS)
}