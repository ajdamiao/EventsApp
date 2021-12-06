package com.example.eventsapp.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventsapp.data.repository.SicrediRepository
import com.example.eventsapp.exception.CustomException
import com.example.eventsapp.model.CheckIn
import com.example.eventsapp.model.EventDetail
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.Exception

class EventDetailViewModel: ViewModel() {
    val apiRepository = SicrediRepository().makeRequest()
    private val eventDetailsResponse: MutableLiveData<Any> = MutableLiveData()
    private val eventPostResponse: MutableLiveData<Any> = MutableLiveData()

    fun getEventDetailResponse() : MutableLiveData<Any> {
        return eventDetailsResponse
    }

    fun getEventCheckInResponse(): MutableLiveData<Any> {
        return eventPostResponse
    }

    fun getEventDetail(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val eventDetail = apiRepository.getEventDetail(id)

                eventDetailsResponse.postValue(eventDetail)
            } catch (exception: Exception)
            {
                print(exception)
                when (exception) {
                    is HttpException -> {
                        val jsonParsed = JSONObject(exception.response()?.errorBody()!!.string())
                        val gson = Gson()
                        val cException = gson.fromJson(jsonParsed.toString(), CustomException::class.java)

                        eventDetailsResponse.postValue(cException)
                    }
                }
            }
        }
    }

    fun checkInEvent(checkIn: CheckIn) {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val checkInResponse = apiRepository.checkInEvent(checkIn)

                println(checkInResponse)

                if(checkInResponse.code() != 200 || checkInResponse.code() != 204) {
                    println("entrou no if view")
                    eventPostResponse.postValue(checkInResponse.isSuccessful)
                }

            } catch (exception: Exception) {
                when(exception) {
                    is HttpException -> {
                        val jsonParsed = JSONObject(exception.response()?.errorBody()!!.toString())
                        val gson = Gson()
                        val cException = gson.fromJson(jsonParsed.toString(), CustomException::class.java)

                        eventPostResponse.postValue(cException)
                    }
                }
            }
        }
    }
}