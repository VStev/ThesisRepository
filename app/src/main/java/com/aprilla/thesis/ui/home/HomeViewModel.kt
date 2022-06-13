package com.aprilla.thesis.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aprilla.thesis.models.ItemsRSS
import com.aprilla.thesis.repository.MainRepository
import com.aprilla.thesis.repository.Resource

class HomeViewModel(MainRepository: MainRepository) : ViewModel() {
    private val repo = MainRepository

    fun fetchSaved(): LiveData<Resource<List<ItemsRSS>>> = repo.getSavedRss()

    fun fetchItems(): LiveData<Resource<List<ItemsRSS>>> = repo.fetchItems()

    fun batchPredict(title: List<String>): LiveData<Resource<List<String>>> = repo.batchPredictCategory(title)

    fun saveItem(item: ItemsRSS) = repo.saveRss(item)

    fun deleteItem(item: ItemsRSS) = repo.deleteRss(item)
}