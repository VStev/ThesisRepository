package com.aprilla.thesis.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.aprilla.thesis.models.ItemsRSS
import com.aprilla.thesis.repository.MainRepository
import com.aprilla.thesis.repository.Resource

class SearchViewModel(MainRepository: MainRepository): ViewModel() {
    private val repo = MainRepository

    fun fetchSearched(keyword:String): LiveData<Resource<List<ItemsRSS>>> = repo.searchRss(keyword)

    fun fetchItems(): LiveData<Resource<List<ItemsRSS>>> = repo.getSavedRss()

    fun setFavourite(link: String, state: Boolean) = repo.setFavorite(link, state)
}