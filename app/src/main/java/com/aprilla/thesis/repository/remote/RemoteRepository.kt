package com.aprilla.thesis.repository.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aprilla.thesis.models.ItemsRSS
import com.aprilla.thesis.models.ResponseRSS
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteRepository(private val retrofit: InterfaceRSS) {

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
            override fun onFailure(call: Call<ResponseRSS>?, t: Throwable?) {
                if (t != null) {
                    rValue.postValue(t.message?.let { Responses.Error(it) })
                }
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
            override fun onFailure(call: Call<ResponseRSS>?, t: Throwable?) {
                if (t != null) {
                    rValue.postValue(t.message?.let { Responses.Error(it) })
                }
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
            override fun onFailure(call: Call<ResponseRSS>?, t: Throwable?) {
                if (t != null) {
                    rValue.postValue(t.message?.let { Responses.Error(it) })
                }
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
            override fun onFailure(call: Call<ResponseRSS>?, t: Throwable?) {
                if (t != null) {
                    rValue.postValue(t.message?.let { Responses.Error(it) })
                }
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
            override fun onFailure(call: Call<ResponseRSS>?, t: Throwable?) {
                if (t != null) {
                    rValue.postValue(t.message?.let { Responses.Error(it) })
                }
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
            override fun onFailure(call: Call<ResponseRSS>?, t: Throwable?) {
                if (t != null) {
                    rValue.postValue(t.message?.let { Responses.Error(it) })
                }
            }
        })
        return rValue
    }

    fun otoFeed(): LiveData<Responses<List<ItemsRSS>>>{
        val rValue = MutableLiveData<Responses<List<ItemsRSS>>>()
        val fetch = retrofit.otoFeed()
        fetch.enqueue(object: Callback<ResponseRSS>{
            override fun onResponse(call: Call<ResponseRSS>, response: Response<ResponseRSS>) {
                if (response.code() == 200){
                    rValue.postValue(Responses.Success(response.body()?.item as List<ItemsRSS>))
                }else{
                    rValue.postValue(Responses.Empty)
                }
            }
            override fun onFailure(call: Call<ResponseRSS>?, t: Throwable?) {
                if (t != null) {
                    rValue.postValue(t.message?.let { Responses.Error(it) })
                }
            }
        })
        return rValue
    }

    fun culinaryFeed(): LiveData<Responses<List<ItemsRSS>>>{
        val rValue = MutableLiveData<Responses<List<ItemsRSS>>>()
        val fetch = retrofit.culinaryFeed()
        fetch.enqueue(object: Callback<ResponseRSS>{
            override fun onResponse(call: Call<ResponseRSS>, response: Response<ResponseRSS>) {
                if (response.code() == 200){
                    rValue.postValue(Responses.Success(response.body()?.item as List<ItemsRSS>))
                }else{
                    rValue.postValue(Responses.Empty)
                }
            }
            override fun onFailure(call: Call<ResponseRSS>?, t: Throwable?) {
                if (t != null) {
                    rValue.postValue(t.message?.let { Responses.Error(it) })
                }
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
            override fun onFailure(call: Call<ResponseRSS>?, t: Throwable?) {
                if (t != null) {
                    rValue.postValue(t.message?.let { Responses.Error(it) })
                }
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
            override fun onFailure(call: Call<ResponseRSS>?, t: Throwable?) {
                if (t != null) {
                    rValue.postValue(t.message?.let { Responses.Error(it) })
                }
            }
        })
        return rValue
    }
}