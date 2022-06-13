package com.aprilla.thesis.repository.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aprilla.thesis.models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteRepository(private val retrofit: InterfaceRSS, private val pRetrofit: InterfaceHeroku) {

    fun mainFeed(): LiveData<Responses<List<ItemsRSS>>>{
        val rValue = MutableLiveData<Responses<List<ItemsRSS>>>()
        val fetch = retrofit.mainFeed()
        fetch.enqueue(object: Callback<ResponseRSS>{
            override fun onResponse(call: Call<ResponseRSS>, response: Response<ResponseRSS>) {
                if (response.code() == 200){
                    rValue.postValue(Responses.Success(response.body()?.item as List<ItemsRSS>))
                }else{
                    rValue.postValue(Responses.Empty)
                }
            }
            override fun onFailure(call: Call<ResponseRSS>, t: Throwable) {
                rValue.postValue(t.message?.let { Responses.Error(it) })
            }
        })
        return rValue
    }

    fun politicFeed(): LiveData<Responses<List<ItemsRSS>>>{
        val rValue = MutableLiveData<Responses<List<ItemsRSS>>>()
        val fetch = retrofit.politicFeed()
        fetch.enqueue(object: Callback<ResponseRSS>{
            override fun onResponse(call: Call<ResponseRSS>, response: Response<ResponseRSS>) {
                if (response.code() == 200){
                    rValue.postValue(Responses.Success(response.body()?.item as List<ItemsRSS>))
                }else{
                    rValue.postValue(Responses.Empty)
                }
            }
            override fun onFailure(call: Call<ResponseRSS>, t: Throwable) {
                rValue.postValue(t.message?.let { Responses.Error(it) })
            }
        })
        return rValue
    }

    fun newsFeed(): LiveData<Responses<List<ItemsRSS>>>{
        val rValue = MutableLiveData<Responses<List<ItemsRSS>>>()
        val fetch = retrofit.newsFeed()
        fetch.enqueue(object: Callback<ResponseRSS>{
            override fun onResponse(call: Call<ResponseRSS>, response: Response<ResponseRSS>) {
                if (response.code() == 200){
                    rValue.postValue(Responses.Success(response.body()?.item as List<ItemsRSS>))
                }else{
                    rValue.postValue(Responses.Empty)
                }
            }
            override fun onFailure(call: Call<ResponseRSS>, t: Throwable) {
                rValue.postValue(t.message?.let { Responses.Error(it) })
            }
        })
        return rValue
    }

    fun sportFeed(): LiveData<Responses<List<ItemsRSS>>>{
        val rValue = MutableLiveData<Responses<List<ItemsRSS>>>()
        val fetch = retrofit.sportFeed()
        fetch.enqueue(object: Callback<ResponseRSS>{
            override fun onResponse(call: Call<ResponseRSS>, response: Response<ResponseRSS>) {
                if (response.code() == 200){
                    rValue.postValue(Responses.Success(response.body()?.item as List<ItemsRSS>))
                }else{
                    rValue.postValue(Responses.Empty)
                }
            }
            override fun onFailure(call: Call<ResponseRSS>, t: Throwable) {
                rValue.postValue(t.message?.let { Responses.Error(it) })
            }
        })
        return rValue
    }

    fun economyFeed(): LiveData<Responses<List<ItemsRSS>>>{
        val rValue = MutableLiveData<Responses<List<ItemsRSS>>>()
        val fetch = retrofit.economyFeed()
        fetch.enqueue(object: Callback<ResponseRSS>{
            override fun onResponse(call: Call<ResponseRSS>, response: Response<ResponseRSS>) {
                if (response.code() == 200){
                    rValue.postValue(Responses.Success(response.body()?.item as List<ItemsRSS>))
                }else{
                    rValue.postValue(Responses.Empty)
                }
            }
            override fun onFailure(call: Call<ResponseRSS>, t: Throwable) {
                rValue.postValue(t.message?.let { Responses.Error(it) })
            }
        })
        return rValue
    }

    fun healthFeed(): LiveData<Responses<List<ItemsRSS>>>{
        val rValue = MutableLiveData<Responses<List<ItemsRSS>>>()
        val fetch = retrofit.healthFeed()
        fetch.enqueue(object: Callback<ResponseRSS>{
            override fun onResponse(call: Call<ResponseRSS>, response: Response<ResponseRSS>) {
                if (response.code() == 200){
                    rValue.postValue(Responses.Success(response.body()?.item as List<ItemsRSS>))
                }else{
                    rValue.postValue(Responses.Empty)
                }
            }
            override fun onFailure(call: Call<ResponseRSS>, t: Throwable) {
                rValue.postValue(t.message?.let { Responses.Error(it) })
            }
        })
        return rValue
    }

    fun entFeed(): LiveData<Responses<List<ItemsRSS>>>{
        val rValue = MutableLiveData<Responses<List<ItemsRSS>>>()
        val fetch = retrofit.entFeed()
        fetch.enqueue(object: Callback<ResponseRSS>{
            override fun onResponse(call: Call<ResponseRSS>, response: Response<ResponseRSS>) {
                if (response.code() == 200){
                    rValue.postValue(Responses.Success(response.body()?.item as List<ItemsRSS>))
                }else{
                    rValue.postValue(Responses.Empty)
                }
            }
            override fun onFailure(call: Call<ResponseRSS>, t: Throwable) {
                rValue.postValue(t.message?.let { Responses.Error(it) })
            }
        })
        return rValue
    }

    fun searchFeed(keyword: String): LiveData<Responses<List<ItemsRSS>>>{
        val rValue = MutableLiveData<Responses<List<ItemsRSS>>>()
        val fetch = retrofit.search(keyword)
        fetch.enqueue(object: Callback<ResponseRSS>{
            override fun onResponse(call: Call<ResponseRSS>, response: Response<ResponseRSS>) {
                if (response.code() == 200){
                    if (response.body()?.item != null) {
                        rValue.postValue(Responses.Success(response.body()?.item as List<ItemsRSS>))
                    }else{
                        rValue.postValue(Responses.Empty)
                    }
                }else{
                    rValue.postValue(Responses.Empty)
                }
            }
            override fun onFailure(call: Call<ResponseRSS>, t: Throwable) {
                rValue.postValue(t.message?.let { Responses.Error(it) })
            }
        })
        return rValue
    }

    fun predictCategory(title: String): LiveData<Responses<String>>{
        val request = HerokuRequest(title)
        val rValue = MutableLiveData<Responses<String>>()
        val fetch = pRetrofit.predict(request)
        fetch.enqueue(object: Callback<ResponseHeroku>{
            override fun onResponse(call: Call<ResponseHeroku>, response: Response<ResponseHeroku>) {
                if (response.code() == 200){
                    if (response.body()?.category != null) {
                        rValue.postValue(Responses.Success(response.body()?.category as String))
                    }else{
                        rValue.postValue(Responses.Empty)
                    }
                }else{
                    rValue.postValue(Responses.Empty)
                }
            }
            override fun onFailure(call: Call<ResponseHeroku>, t: Throwable) {
                rValue.postValue(t.message?.let { Responses.Error(it) })
            }
        })
        return rValue
    }

    fun batchPredictCategory(title: List<String>): LiveData<Responses<List<ResponseHeroku>>>{
        val tempRequest = ArrayList<HerokuRequest>()
        title.forEach {
            tempRequest.add(HerokuRequest(it))
        }
        val request = HerokuBatchRequest(tempRequest)
        val rValue = MutableLiveData<Responses<List<ResponseHeroku>>>()
        val fetch = pRetrofit.batchPredict(request)
        fetch.enqueue(object: Callback<BatchedResponse>{
            override fun onResponse(call: Call<BatchedResponse>, response: Response<BatchedResponse>) {
                if (response.code() == 200){
                    if (response.body()?.batch_category != null) {
                        rValue.postValue(Responses.Success(response.body()?.batch_category as List<ResponseHeroku>))
                    }else{
                        rValue.postValue(Responses.Empty)
                    }
                }else{
                    rValue.postValue(Responses.Empty)
                }
            }
            override fun onFailure(call: Call<BatchedResponse>, t: Throwable) {
                rValue.postValue(t.message?.let { Responses.Error(it) })
            }
        })
        return rValue
    }
}