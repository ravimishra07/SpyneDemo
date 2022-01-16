package com.ravi.spynedemo.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ravi.spynedemo.model.GIF
import com.ravi.spynedemo.model.GIFData

class GifTypeConverter {

    var gson = Gson()

    @TypeConverter
    fun gifToString(gifs: GIF): String {
        return gson.toJson(gifs)
    }

    @TypeConverter
    fun stringToGifs(data: String): GIF {
        val listType = object : TypeToken<GIF>() {}.type
        return gson.fromJson(data, listType)
    }

}