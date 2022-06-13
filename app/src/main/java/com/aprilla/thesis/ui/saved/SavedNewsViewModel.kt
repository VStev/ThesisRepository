package com.aprilla.thesis.ui.saved

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.aprilla.thesis.models.ItemsRSS
import com.aprilla.thesis.repository.MainRepository
import com.aprilla.thesis.repository.Resource
import com.aprilla.thesis.utilities.GroupingFilter

class SavedNewsViewModel(MainRepository: MainRepository) : ViewModel() {
    private val repo = MainRepository
    private val _filter = MutableLiveData<GroupingFilter>()

    fun fetchItems(): LiveData<Resource<List<ItemsRSS>>> = _filter.switchMap {
        repo.getGroupedSavedRss(it)
    }

    init {
        _filter.value = GroupingFilter.CAT_ALL
    }

    fun filter(filter: GroupingFilter){
        _filter.value = filter
    }

    fun deleteRss(item: ItemsRSS) = repo.deleteRss(item)

}