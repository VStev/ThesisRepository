package com.aprilla.thesis.utilities

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingPreferences constructor(private val dataStore: DataStore<Preferences>, private val prefKey: Preferences.Key<Boolean>) {

    fun getRunStatus(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[prefKey] ?: true
        }
    }

    suspend fun saveRunStatus(isFirstRun: Boolean) {
        dataStore.edit { preferences ->
            preferences[prefKey] = isFirstRun
        }
    }
}