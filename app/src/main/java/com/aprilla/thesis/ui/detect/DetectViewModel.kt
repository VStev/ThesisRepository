package com.aprilla.thesis.ui.detect

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aprilla.thesis.models.ItemsRSS
import com.aprilla.thesis.repository.MainRepository
import com.aprilla.thesis.repository.Resource

class DetectViewModel(MainRepository: MainRepository) : ViewModel() {
    private val repo = MainRepository

    fun predictCategory(title: String) = repo.predictCategory(title)

    fun getFeedFromCategory(category: String) = repo.fetchItemsByCategory(category)

    fun fetchItems(): LiveData<Resource<List<ItemsRSS>>> = repo.getSavedRss()

    fun saveItem(item: ItemsRSS) = repo.saveRss(item)

    fun deleteItem(item: ItemsRSS) = repo.deleteRss(item)
}