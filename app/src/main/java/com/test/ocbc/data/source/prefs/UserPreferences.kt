package com.test.ocbc.data.source.prefs

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore by preferencesDataStore("OCBCUserPref")
class UserPreferences @Inject constructor(
    val context: Context
) {
    val authToken: Flow<String?>
        get() = context.dataStore.data.map {
            it[TOKEN]
        }

    suspend fun saveToken(token: String) {
        context.dataStore.edit {
            it[TOKEN] = token
        }
    }

    companion object {
        var TOKEN = stringPreferencesKey("token")
    }
}