package com.ravi.spynedemo.data

import com.ravi.spynedemo.model.GIF
import com.ravi.spynedemo.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitApi {

@GET(Constants.TRENDING)
   suspend fun getGifs(
    @Query("api_key") apiKey: String,
    @Query("limit") limit: Int,
    @Query("offset") offSet: Int
    ): Response<GIF>

   @GET(Constants.SEARCH_GIF)
    suspend fun getGifSearch(
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int,
        @Query("q") query: String
    ): Response<GIF>
}