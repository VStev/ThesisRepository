package com.aprilla.thesis.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.aprilla.thesis.AppExecutors
import com.aprilla.thesis.models.ItemsRSS
import com.aprilla.thesis.repository.local.LocalRepository
import com.aprilla.thesis.repository.remote.RemoteRepository
import com.aprilla.thesis.repository.remote.Responses
import com.aprilla.thesis.utilities.EspressoIdlingResource
import com.aprilla.thesis.utilities.GroupingFilter

class MainRepository(
    RemoteRepository: RemoteRepository,
    LocalRepository: LocalRepository,
    appExecutors: AppExecutors
) {
    private val repo = RemoteRepository
    private val db = LocalRepository
    private val exec = appExecutors

    fun fetchItems(): LiveData<Resource<List<ItemsRSS>>> {
        val result = MediatorLiveData<Resource<List<ItemsRSS>>>()
        result.value = Resource.loading(null)
        result.addSource(repo.mainFeed()) { data ->
            result.removeSource(repo.mainFeed())
            when (data) {
                is Responses.Success -> {
                    result.value = Resource.success(data.data)
                }
                is Responses.Error -> {
                    result.value = Resource.error(data.errorMessage, null)
                }
                is Responses.Empty -> {
                    result.value = Resource.error("Tidak ada data", null)
                }
            }
        }
        return result
    }

    fun fetchItemsByCategory(category: String): LiveData<Resource<List<ItemsRSS>>> {
        val result = MediatorLiveData<Resource<List<ItemsRSS>>>()
        result.value = Resource.loading(null)
        val fetch = when {
            (category == "News") -> repo.newsFeed()
            (category == "Politik") -> repo.politicFeed()
            (category == "Ekonomi") -> repo.economyFeed()
            (category == "Olahraga") -> repo.sportFeed()
            (category == "Kesehatan") -> repo.healthFeed()
            else -> repo.entFeed()
        }
        result.addSource(fetch) { data ->
            result.removeSource(fetch)
            when (data) {
                is Responses.Success -> {
                    result.value = Resource.success(data.data)
                }
                is Responses.Error -> {
                    result.value = Resource.error(data.errorMessage, null)
                }
                is Responses.Empty -> {
                    result.value = Resource.error("Tidak ada data", null)
                }
            }
        }
        return result
    }

    fun getSavedRss(): LiveData<Resource<List<ItemsRSS>>> {
        val result = MediatorLiveData<Resource<List<ItemsRSS>>>()
        result.value = Resource.loading(null)
        result.addSource(db.fetchSavedRss(GroupingFilter.CAT_ALL)) { data ->
            result.removeSource(db.fetchSavedRss(GroupingFilter.CAT_ALL))
            when {
                (data.isNotEmpty()) -> {
                    result.value = Resource.success(data)
                }
                else -> {
                    result.value = Resource.error("No saved data", null)
                }
            }
        }
        return result
    }

    fun getGroupedSavedRss(filter: GroupingFilter): LiveData<Resource<List<ItemsRSS>>> {
        val result = MediatorLiveData<Resource<List<ItemsRSS>>>()
        result.value = Resource.loading(null)
        result.addSource(db.fetchSavedRss(filter)) { data ->
            result.removeSource(db.fetchSavedRss(filter))
            when {
                (data.isNotEmpty()) -> {
                    result.value = Resource.success(data)
                }
                else -> {
                    result.value = Resource.error("No saved data", null)
                }
            }
        }
        return result
    }

    fun searchRss(keyword: String): LiveData<Resource<List<ItemsRSS>>> {
        val result = MediatorLiveData<Resource<List<ItemsRSS>>>()
        result.value = Resource.loading(null)
        result.addSource(repo.searchFeed(keyword)) { data ->
            result.removeSource(repo.searchFeed(keyword))
            when (data) {
                is Responses.Success -> {
                    result.value = Resource.success(data.data)
                }
                is Responses.Error -> {
                    result.value = Resource.error(data.errorMessage, null)
                }
                is Responses.Empty -> {
                    result.value = Resource.error("Tidak ada data", null)
                }
            }
        }
        return result
    }

    fun predictCategory(title: String): LiveData<Resource<String>>{
        val result = MediatorLiveData<Resource<String>>()
        result.value = Resource.loading(null)
        result.addSource(repo.predictCategory(title)) { data ->
            result.removeSource(repo.predictCategory(title))
            when (data) {
                is Responses.Success -> {
                    result.value = Resource.success(data.data)
                }
                is Responses.Error -> {
                    result.value = Resource.error(data.errorMessage, null)
                }
                is Responses.Empty -> {
                    result.value = Resource.error("Tidak ada data", null)
                }
            }
        }
        return result
    }

    fun batchPredictCategory(title: List<String>): LiveData<Resource<List<String>>>{
        val result = MediatorLiveData<Resource<List<String>>>()
        result.value = Resource.loading(null)
        result.addSource(repo.batchPredictCategory(title)) { data ->
            result.removeSource(repo.batchPredictCategory(title))
            when (data) {
                is Responses.Success -> {
                    val returnedList = ArrayList<String>()
                    data.data.forEach {
                        returnedList.add(it.category)
                    }
                    result.value = Resource.success(returnedList)
                }
                is Responses.Error -> {
                    result.value = Resource.error(data.errorMessage, null)
                }
                is Responses.Empty -> {
                    result.value = Resource.error("Tidak ada data", null)
                }
            }
        }
        return result
    }

    fun saveRss(item: ItemsRSS) = exec.diskIO().execute {
        db.saveRss(item)
    }

    fun deleteRss(item: ItemsRSS) = exec.diskIO().execute {
        db.deleteRss(item)
    }
}