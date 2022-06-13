package com.aprilla.thesis.models

import com.google.gson.annotations.SerializedName

data class ResponseHeroku(
    @SerializedName("kategori")
    val category: String = ""
)

data class BatchedResponse(
    @SerializedName("batch_kategori")
    val batch_category: List<ResponseHeroku>
)
