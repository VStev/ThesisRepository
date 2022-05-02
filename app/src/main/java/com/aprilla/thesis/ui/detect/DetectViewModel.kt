package com.aprilla.thesis.ui.detect

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aprilla.thesis.repository.MainRepository

class DetectViewModel(MainRepository: MainRepository) : ViewModel() {
    private val repo = MainRepository

    fun predictCategory(title: String) = repo.predictCategory(title)

    fun getFeedFromCategory(category: String) = repo.fetchItemsByCategory(category)
}