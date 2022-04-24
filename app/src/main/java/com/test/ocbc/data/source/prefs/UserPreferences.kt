package com.test.ocbc.data.source.prefs

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
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

    val userData: Flow<UserData?>
        get() = context.dataStore.data.map {
            UserData(
                it[USER_USERNAME].toString(),
                it[USER_ACCOUNT_NO].toString()
            )
        }

    val isUserLogin: Flow<Boolean?>
        get() = context.dataStore.data.map {
            it[LOGIN_VALIDATION]
        }

    suspend fun saveToken(token: String) {
        context.dataStore.edit {
            it[TOKEN] = token
        }
    }

    suspend fun saveUserData(userData: UserData) {
        context.dataStore.edit { mapData ->
            userData.username?.let { mapData[USER_USERNAME] = it }
            userData.accountNo?.let { mapData[USER_ACCOUNT_NO] = it }
        }
    }

    suspend fun isUserLogin(isLogin: Boolean) {
        context.dataStore.edit {
            it[LOGIN_VALIDATION] = isLogin
        }
    }

    companion object {
        private val TOKEN = stringPreferencesKey("token")
        private val LOGIN_VALIDATION = booleanPreferencesKey("is_user_login")
        private val USER_USERNAME = stringPreferencesKey("username")
        private val USER_ACCOUNT_NO = stringPreferencesKey("accountNo")
    }
}

data class UserData(
    val username: String? = null,
    val accountNo: String? = null
)