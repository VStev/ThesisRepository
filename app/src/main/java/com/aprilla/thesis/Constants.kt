package com.aprilla.thesis

import java.util.concurrent.Executors

const val API_KEY = "gfboffvvjphlh6u4pwaianzxxcmwdaxxhgrmksck"
const val RSS_GENERAL = "https://www.jawapos.com/feed/"
const val RSS_NEWS = "https://www.jawapos.com/nasional/feed/"
const val RSS_POLITIC = "https://www.jawapos.com/nasional/politik/feed/"
const val RSS_ECONOMY = "https://www.jawapos.com/ekonomi/feed/"
const val RSS_SPORT = "https://www.jawapos.com/sports/feed/"
const val RSS_HEALTH = "https://www.jawapos.com/kesehatan/feed/"
const val RSS_ETC_OTO = "https://www.jawapos.com/oto-dan-tekno/feed/"
const val RSS_ETC_ENT = "https://www.jawapos.com/entertainment/feed/"
const val RSS_ETC_CUL = "https://www.jawapos.com/wisata-dan-kuliner/feed/"
const val BASE_URL = "https://www.jawapos.com/"
const val GCP_URL = "google.com"

private val SINGLE_EXECUTOR = Executors.newSingleThreadExecutor()

fun executeThread(f: () -> Unit) {
    SINGLE_EXECUTOR.execute(f)
}