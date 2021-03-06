package com.aprilla.thesis.repository.remote

import com.aprilla.thesis.models.BatchedResponse
import com.aprilla.thesis.models.HerokuBatchRequest
import com.aprilla.thesis.models.HerokuRequest
import com.aprilla.thesis.models.ResponseHeroku
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface InterfaceHeroku {
    @POST("search")
    fun predict(@Body request: HerokuRequest): Call<ResponseHeroku>

    @POST("batch")
    fun batchPredict(@Body request: HerokuBatchRequest): Call<BatchedResponse>
}