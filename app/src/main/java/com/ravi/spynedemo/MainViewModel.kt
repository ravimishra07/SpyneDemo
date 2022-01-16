package com.ravi.spynedemo

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.*
import com.ravi.spynedemo.data.Repository
import com.ravi.spynedemo.data.local.entities.GIFEntity
import com.ravi.spynedemo.model.GIF
import com.ravi.spynedemo.model.GIFData
import com.ravi.spynedemo.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    var gifData: MutableLiveData<NetworkResult<MutableList<GIFData>>> = MutableLiveData()
    var gifSearchedData: MutableLiveData<NetworkResult<MutableList<GIFData>>> = MutableLiveData()

    /** ROOM DATABASE */

    val readGifs: LiveData<List<GIFEntity>> = repository.local.readGIF().asLiveData()
    private fun insertGifs(gifEntity: GIFEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertGIF(gifEntity)
        }

    /** RETROFIT */

    fun getGifData(offSet: Int) = viewModelScope.launch {
        getGifDataSafeCall(offSet)
    }

    fun getSearchedGifData(query: String) = viewModelScope.launch {
        getSearchedGifDataSafeCall(query)
    }

    private suspend fun getGifDataSafeCall(offSet: Int) {
        gifData.value = NetworkResult.Loading()

        try {
            val response = repository.remote.getGifData(25, offSet)
            gifData.value = handleGIFResponse(response)
            val gifData = gifData.value!!.data
            if (gifData != null) {
                offlineQuestionCaching(GIF(gifData))
            }
        } catch (e: java.lang.Exception) {
            gifData.value = NetworkResult.Error("data not found.")
        }
    }

    private fun offlineQuestionCaching(questions: GIF) {
        val gifEntity = GIFEntity(questions)
        insertGifs(gifEntity)
    }

    private suspend fun getSearchedGifDataSafeCall(query: String) {
        gifData.value = NetworkResult.Loading()
        try {
            val response = repository.remote.getSearchedGifData(25, query)
            gifData.value = handleGIFResponse(response)
        } catch (e: Exception) {
            gifData.value = NetworkResult.Error("data not found.")
        }
    }

    private fun handleGIFResponse(response: Response<GIF>): NetworkResult<MutableList<GIFData>> {
        return when {
            response.isSuccessful -> NetworkResult.Success(response.body()!!.data)
            else ->  NetworkResult.Error(response.message())
        }
    }
}