package com.aprilla.thesis.ui.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aprilla.thesis.models.ItemsRSS
import com.aprilla.thesis.repository.MainRepository
import com.aprilla.thesis.repository.Resource

class SavedNewsViewModel(MainRepository: MainRepository) : ViewModel() {
    private val repo = MainRepository

    fun fetchItems(): LiveData<Resource<List<ItemsRSS>>> = repo.getSavedRss()

    fun deleteRss(item: ItemsRSS) = repo.deleteRss(item)

}