package com.rksrtx76.truebalance.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ThemePreference {
    private val Context.dataStore by preferencesDataStore("settings")

    private val THEME_KEY = booleanPreferencesKey("dark_theme")

    suspend fun saveThemeToDataStore(context: Context, isDark: Boolean) {
        context.dataStore.edit { settings ->
            settings[THEME_KEY] = isDark
        }
    }

    fun getThemeFromDataStore(context: Context): Flow<Boolean> {
        return context.dataStore.data
            .map { prefs -> prefs[THEME_KEY] ?: false }
    }
}