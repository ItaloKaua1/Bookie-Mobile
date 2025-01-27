package com.example.bookie

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserRepository(private val context: Context) {
    private companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("userRepository")

        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_ID = stringPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val NOTIFICATIONS_ENABLED = stringPreferencesKey("notifications_enabled")
        val CUSTOM_THEME = stringPreferencesKey("custom_theme")
        val ANIMATIONS_ENABLED = stringPreferencesKey("animations_enabled")
    }

    val currentUserEmail: Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[USER_EMAIL] ?: "Unknown"
        }

    suspend fun saveUserEmail(userEmail: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_EMAIL] = userEmail
        }
    }

    val currentUserId: Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[USER_ID] ?: "Unknown"
        }

    suspend fun saveUserId(userId: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_ID] = userId
        }
    }

    val currentUserName: Flow<String> =
        context.dataStore.data.map { preferences ->
            preferences[USER_NAME] ?: "Unknown"
        }

    suspend fun saveUserName(userName: String) {
        context.dataStore.edit { preferences ->
            preferences[USER_NAME] = userName
        }
    }

    // Adicionar métodos para notificações, tema personalizado e animações visuais
    val notificationsEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[NOTIFICATIONS_ENABLED]?.toBoolean() ?: true
    }

    suspend fun updateNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[NOTIFICATIONS_ENABLED] = enabled.toString()
        }
    }

    val customTheme: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[CUSTOM_THEME]
    }

    suspend fun updateCustomTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[CUSTOM_THEME] = theme
        }
    }

    val animationsEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[ANIMATIONS_ENABLED]?.toBoolean() ?: true
    }

    suspend fun updateAnimationsEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[ANIMATIONS_ENABLED] = enabled.toString()
        }
    }
}