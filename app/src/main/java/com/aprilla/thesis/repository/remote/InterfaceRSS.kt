package com.aprilla.thesis.repository.remote

import com.aprilla.thesis.models.ResponseRSS
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface InterfaceRSS {
    @GET("feed/")
    fun mainFeed(): Call<ResponseRSS>

    @GET("nasional/politik/feed/")
    fun politicFeed(): Call<ResponseRSS>

    @GET("nasional/feed/")
    fun newsFeed(): Call<ResponseRSS>

    @GET("sports/feed/")
    fun sportFeed(): Call<ResponseRSS>

    @GET("ekonomi/feed/")
    fun economyFeed(): Call<ResponseRSS>

    @GET("kesehatan/feed/")
    fun healthFeed(): Call<ResponseRSS>

    @GET("oto-dan-tekno/feed/")
    fun otoFeed(): Call<ResponseRSS>

    @GET("wisata-dan-kuliner/feed/")
    fun culinaryFeed(): Call<ResponseRSS>

    @GET("entertainment/feed/")
    fun entFeed(): Call<ResponseRSS>

    @GET("feed/")
    fun search(@Query("s") title: String): Call<ResponseRSS>
}