package com.aprilla.thesis.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.aprilla.thesis.AppExecutors
import com.aprilla.thesis.models.ItemsRSS
import com.aprilla.thesis.repository.local.LocalRepository
import com.aprilla.thesis.repository.remote.RemoteRepository
import com.aprilla.thesis.repository.remote.Responses

class MainRepository(
    RemoteRepository: RemoteRepository,
    LocalRepository: LocalRepository,
    appExecutors: AppExecutors
) {
    private val repo = RemoteRepository
    private val db = LocalRepository
    private val exec = appExecutors

    //    fun fetchItems(): LiveData<Resource<List<ItemsRSS>>> {
//        return object: NetworkBoundResource<List<ItemsRSS>, List<ItemsRSS>>(exec){
//            override fun loadFromDB(): LiveData<List<ItemsRSS>> {
//                return db.fetchRss()
//            }
//
//            override fun shouldFetch(data: List<ItemsRSS>?): Boolean {
//                return true
//            }
//
//            override fun createCall(): LiveData<Responses<List<ItemsRSS>>> = repo.mainFeed()
//
//            override fun saveCallResult(data: List<ItemsRSS>) {
//                db.clearCache()
//                data.forEach {
//                    db.saveRss(it)
//                }
//            }
//        }.asLiveData()
//    }

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


//    fun fetchCategorySpecific(category: String): LiveData<Resource<List<ItemsRSS>>> {
//        return object : NetworkBoundResource<List<ItemsRSS>, List<ItemsRSS>>(exec) {
//            override fun loadFromDB(): LiveData<List<ItemsRSS>> {
//                return db.fetchRss()
//            }
//
//            override fun shouldFetch(data: List<ItemsRSS>?): Boolean {
//                return true
//            }
//
//            override fun createCall(): LiveData<Responses<List<ItemsRSS>>> =
//                when {
//                    (category == "politik") -> repo.politicFeed()
//                    (category == "news") -> repo.newsFeed()
//                    (category == "ekomoni") -> repo.economyFeed()
//                    (category == "kesehatan") -> repo.healthFeed()
//                    (category == "olahraga") -> repo.sportFeed()
//                    else -> repo.entFeed() //combine 3 as 1
//                }
//
//            override fun saveCallResult(data: List<ItemsRSS>) {
//                db.clearCache()
//                data.forEach {
//                    db.saveRss(it)
//                }
//            }
//        }.asLiveData()
//    }

    fun getSavedRss(): LiveData<Resource<List<ItemsRSS>>> {
        val result = MediatorLiveData<Resource<List<ItemsRSS>>>()
        result.value = Resource.loading(null)
        result.addSource(db.fetchSavedRss()) { data ->
            result.removeSource(db.fetchSavedRss())
            when {
                (data.isNotEmpty()) -> {
                    result.value = Resource.success(data)
                }
                else -> result.value = Resource.error("No saved data", null)
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

    fun setFavorite(link: String, state: Boolean) =
        exec.diskIO().execute { db.setFavorite(link, state) }

    fun saveRss(item: ItemsRSS) = exec.diskIO().execute { db.saveRss(item) }

    fun deleteRss(item: ItemsRSS) = exec.diskIO().execute { db.deleteRss(item) }
}