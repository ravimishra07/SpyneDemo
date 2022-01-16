package com.ravi.spynedemo.data

import com.ravi.spynedemo.model.GIF
import com.ravi.spynedemo.util.Constants.Companion.API_KEY
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val api: RetrofitApi
    ) {
    suspend fun getGifData(limit: Int, offSet: Int): Response<GIF> {
        return api.getGifs(API_KEY,limit,offSet)
    }
    suspend fun getSearchedGifData(limit: Int, query:String): Response<GIF> {
        return api.getGifSearch(API_KEY,limit,query)
    }
}