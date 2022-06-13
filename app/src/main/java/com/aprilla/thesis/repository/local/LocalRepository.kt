package com.aprilla.thesis.repository.local

import androidx.lifecycle.LiveData
import com.aprilla.thesis.models.ItemsRSS
import com.aprilla.thesis.utilities.FilterUtility
import com.aprilla.thesis.utilities.GroupingFilter

class LocalRepository(private val database: SavedRSSDAO) {
    fun fetchSavedRss(filter: GroupingFilter): LiveData<List<ItemsRSS>> {
        val query = FilterUtility.getFilteredQuery(filter)
        return database.fetchSavedRss(query)
    }

    fun saveRss(item: ItemsRSS) = database.saveRss(item)

    fun deleteRss(item: ItemsRSS) = database.deleteRss(item)
}