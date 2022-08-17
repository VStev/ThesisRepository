package com.aprilla.thesis.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.aprilla.thesis.AppExecutors
import com.aprilla.thesis.BASE_URL
import com.aprilla.thesis.HEROKU_URL
import com.aprilla.thesis.repository.MainRepository
import com.aprilla.thesis.repository.local.LocalRepository
import com.aprilla.thesis.repository.local.SavedRSSDatabase
import com.aprilla.thesis.repository.remote.InterfaceHeroku
import com.aprilla.thesis.repository.remote.InterfaceRSS
import com.aprilla.thesis.repository.remote.RemoteRepository
import com.aprilla.thesis.ui.detect.DetectViewModel
import com.aprilla.thesis.ui.home.HomeViewModel
import com.aprilla.thesis.ui.result.SearchViewModel
import com.aprilla.thesis.ui.saved.SavedNewsViewModel
import com.aprilla.thesis.ui.splash.SplashScreenViewModel
import com.aprilla.thesis.utilities.SettingPreferences
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.logger.Level
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "com.aprilla.thesis.shared.preferences")

@JvmField
val rssModule = module {
    single(named("rss")) {
        val client = OkHttpClient.Builder()
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
        retrofit.create(InterfaceRSS::class.java)
    }
}

val mlModule = module {
    single(named("predict")) {
        val client = OkHttpClient.Builder()
            .readTimeout(120L, TimeUnit.SECONDS)
            .writeTimeout(120L, TimeUnit.SECONDS)
            .build()
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofit = Retrofit.Builder()
            .baseUrl(HEROKU_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        retrofit.create(InterfaceHeroku::class.java)
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

val dataStoreModule = module{
    single{
        SettingPreferences(
            androidContext().dataStore,
            booleanPreferencesKey("isFirstRun")
        )
    }
}

val viewModelModule = module {
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
    viewModel{
        SplashScreenViewModel(get())
    }
}

