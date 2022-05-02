package com.aprilla.thesis.di

import androidx.room.Room
import com.aprilla.thesis.AppExecutors
import com.aprilla.thesis.BASE_URL
import com.aprilla.thesis.HEROKU_URL
import com.aprilla.thesis.repository.MainRepository
import com.aprilla.thesis.repository.remote.InterfaceRSS
import com.aprilla.thesis.repository.remote.RemoteRepository
import com.aprilla.thesis.repository.local.LocalRepository
import com.aprilla.thesis.repository.local.SavedRSSDatabase
import com.aprilla.thesis.ui.detect.DetectViewModel
import com.aprilla.thesis.ui.home.HomeViewModel
import com.aprilla.thesis.ui.result.SearchViewModel
import com.aprilla.thesis.ui.saved.SavedNewsViewModel
import com.google.gson.GsonBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory

@JvmField
val rssModule = module {
    single(named("rss")) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
        retrofit.create(InterfaceRSS::class.java)
    }
}

val mlModule = module {
    single(named("predict")) {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(HEROKU_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}

val databaseModule = module {
    factory { get<SavedRSSDatabase>().rssDao() }
    single {
        Room.databaseBuilder(
            androidContext(),
            SavedRSSDatabase::class.java,
            "saved.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}

val repositoryModule = module {
    single(named("remoteRepository")) { RemoteRepository(get(named("rss")), get(named("predict"))) }
    single(named("databaseRepo")) { LocalRepository(get()) }
    single(named("mainRepository")) {
        MainRepository(
            get(named("remoteRepository")),
            get(named("databaseRepo")),
            AppExecutors()
        )
    }
}


val viewModelModule = module {
//    viewModel{
//        HomeViewModel(get(named("rssRepo")), get(named("databaseRepo")))
//    }
    viewModel {
        HomeViewModel(get(named("mainRepository")))
    }
    viewModel{
        SavedNewsViewModel(get(named("mainRepository")))
    }
    viewModel{
        SearchViewModel(get(named("mainRepository")))
    }
    viewModel{
        DetectViewModel(get(named("mainRepository")))
    }
}

