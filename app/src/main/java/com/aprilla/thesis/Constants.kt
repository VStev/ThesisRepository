package com.aprilla.thesis

import java.util.concurrent.Executors

const val BASE_URL = "https://www.jawapos.com/"
const val HEROKU_URL = "https://thesis-news-category.herokuapp.com/"

private val SINGLE_EXECUTOR = Executors.newSingleThreadExecutor()

fun executeThread(f: () -> Unit) {
    SINGLE_EXECUTOR.execute(f)
}