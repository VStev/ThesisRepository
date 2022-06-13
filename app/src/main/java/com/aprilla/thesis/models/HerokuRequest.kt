package com.aprilla.thesis.models

import com.google.gson.annotations.SerializedName

data class HerokuRequest(
    @SerializedName("message")
    val message: String = ""
)

data class HerokuBatchRequest(
    @SerializedName("_message")
    val message: List<HerokuRequest>
)
