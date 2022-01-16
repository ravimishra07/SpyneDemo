package com.ravi.spynedemo.data

import com.ravi.spynedemo.data.local.GIFDao
import com.ravi.spynedemo.data.local.entities.GIFEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val GIFDao: GIFDao
) {

    fun readGIF(): Flow<List<GIFEntity>> {
        return GIFDao.readGif()
    }

    suspend fun insertGIF(recipesEntity: GIFEntity) {
        GIFDao.insertGif(recipesEntity)
    }
}