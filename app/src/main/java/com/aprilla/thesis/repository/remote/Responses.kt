package com.aprilla.thesis.repository.remote

import com.aprilla.thesis.repository.Status

//class Responses<T>(val status: RemoteStatus, val body: T, val message: String?) {
//    companion object {
//        fun <T> success(body: T): Responses<T> = Responses(RemoteStatus.SUCCESS, body, null)
//
//        fun <T> empty(msg: String, body: T): Responses<T> = Responses(RemoteStatus.EMPTY, body, msg)
//
//        fun <T> error(msg: String, body: T): Responses<T> = Responses(RemoteStatus.ERROR, body, msg)
//    }
//}

sealed class Responses<out R> {
    data class Success<out T>(val data: T) : Responses<T>()
    data class Error(val errorMessage: String) : Responses<Nothing>()
    object Empty : Responses<Nothing>()
}