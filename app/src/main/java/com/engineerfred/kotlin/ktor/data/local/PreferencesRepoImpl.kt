package com.engineerfred.kotlin.ktor.data.local

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.engineerfred.kotlin.ktor.domain.repository.PreferencesRepository
import com.engineerfred.kotlin.ktor.util.Constants.USER_KEY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferencesRepoImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferencesRepository {

    companion object {
        private val TAG = PreferencesRepoImpl::class.java.simpleName
    }

    override suspend fun storeUser(user: String) {
        dataStore.edit {
            it[USER_KEY] = user
        }
    }

    override fun getUser(): Flow<String?> {
        return dataStore.data.map {
            it[USER_KEY] ?: null
        }.distinctUntilChanged()
            .flowOn(Dispatchers.IO)
            .catch {
                Log.i(TAG, "Failed to get app user! $it")
        }
    }
}