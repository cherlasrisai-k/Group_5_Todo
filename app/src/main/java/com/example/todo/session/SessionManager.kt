package com.example.todo.session


import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore("session")

object SessionManager {

    private val USER_MOBILE = stringPreferencesKey("user_mobile")

    suspend fun saveUser(context: Context, mobile: String) {
        context.dataStore.edit { prefs ->
            prefs[USER_MOBILE] = mobile
        }
    }

    suspend fun getUser(context: Context): String? {
        return context.dataStore.data.first()[USER_MOBILE]
    }

    suspend fun clear(context: Context) {
        context.dataStore.edit { it.clear() }
    }
}
