package com.aprilla.thesis.repository.local

import androidx.lifecycle.LiveData
import com.aprilla.thesis.models.ItemsRSS

class LocalRepository(private val database: SavedRSSDAO) {
    fun fetchRss(): LiveData<List<ItemsRSS>> = database.fetchRss()

    fun fetchSavedRss(): LiveData<List<ItemsRSS>> = database.fetchSavedRss()

    fun saveRss(item: ItemsRSS) = database.saveRss(item)

    fun setFavorite(link: String, state: Boolean) = database.setFavorite(link, state)

    fun clearCache() = database.clearCache()

    fun deleteRss(item: ItemsRSS) = database.deleteRss(item)
}