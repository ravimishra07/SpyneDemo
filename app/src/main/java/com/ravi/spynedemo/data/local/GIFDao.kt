package com.ravi.spynedemo.data.local

import androidx.room.*
import com.ravi.spynedemo.data.local.entities.GIFEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GIFDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGif(GIFEntity: GIFEntity)

    @Query("SELECT * FROM gif_table ORDER BY id ASC")
    fun readGif(): Flow<List<GIFEntity>>

}