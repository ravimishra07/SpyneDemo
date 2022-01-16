package com.ravi.spynedemo.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ravi.spynedemo.data.local.entities.GIFEntity

@Database(
    entities = [GIFEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(GifTypeConverter::class)
abstract class GIFDatabase: RoomDatabase() {
    abstract fun gifDao(): GIFDao
}