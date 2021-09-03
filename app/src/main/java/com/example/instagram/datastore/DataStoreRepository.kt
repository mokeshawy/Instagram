package com.example.instagram.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.instagram.utils.Const
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DataStoreRepository
@Inject
constructor(@ApplicationContext private val context: Context) {

    object PreferenceKey{
        val userName    =  stringPreferencesKey(Const.USER_NAME_KEY)
        val fullName    = stringPreferencesKey(Const.FULL_NAME_KEY)
        val bio         = stringPreferencesKey(Const.BIO_KEY)
        val image       = stringPreferencesKey(Const.IMAGE_KEY)
    }
    private val Context.dataStore by preferencesDataStore(Const.DATA_STORE_NAME)

    suspend fun insertUserInfoInLocale( userName : String ,
                                      fullName : String ,
                                      bio : String ,
                                      image : String ){
        context.dataStore.edit {
            it[PreferenceKey.userName] = userName
            it[PreferenceKey.fullName] = fullName
            it[PreferenceKey.bio]      = bio
            it[PreferenceKey.image]    = image
        }
    }

    val readUserName : Flow<String> = context.dataStore.data
        .catch {
            if(this is Exception){
                emit(emptyPreferences())
            }
        }.map {
            val userName = it[PreferenceKey.userName] ?:""
            userName
        }

    val readFullName : Flow<String> = context.dataStore.data
        .catch {
            if(this is Exception){
                emit(emptyPreferences())
            }
        }.map {
            val fullName = it[PreferenceKey.fullName] ?:""
            fullName
        }

    val readBio : Flow<String> = context.dataStore.data
        .catch {
            if(this is Exception){
                emit(emptyPreferences())
            }
        }.map {
            val bio = it[PreferenceKey.bio] ?:""
            bio
        }

    val readImage : Flow<String> = context.dataStore.data
        .catch {
            if(this is Exception){
                emit(emptyPreferences())
            }
        }.map {
            val image = it[PreferenceKey.image] ?:""
            image
        }
}