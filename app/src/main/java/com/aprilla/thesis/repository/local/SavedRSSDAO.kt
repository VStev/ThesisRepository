package com.aprilla.thesis.repository.local

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.aprilla.thesis.models.ItemsRSS

@Dao
interface SavedRSSDAO {
    @RawQuery(observedEntities = [ItemsRSS::class]) //make this raw query
    fun fetchSavedRss(query: SupportSQLiteQuery): LiveData<List<ItemsRSS>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveRss(item: ItemsRSS)

    @Delete
    fun deleteRss(item: ItemsRSS)
}