package com.ravi.spynedemo.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ravi.spynedemo.model.GIF
import com.ravi.spynedemo.util.Constants.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
class GIFEntity(
    var gifs: GIF
) {
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}