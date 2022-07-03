package com.aprilla.thesis.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aprilla.thesis.utilities.SettingPreferences
import kotlinx.coroutines.launch

class SplashScreenViewModel(private val pref: SettingPreferences): ViewModel() {
    fun getRunStatus(): LiveData<Boolean> {
        return pref.getRunStatus().asLiveData()
    }

    fun setRunStatus(run: Boolean){
        viewModelScope.launch{
            pref.saveRunStatus(run)
        }
    }
}