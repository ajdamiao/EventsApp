package com.example.eventsapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.eventsapp.data.repository.SicrediRepository
import com.example.eventsapp.model.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class HomeViewModel: ViewModel() {
    private val apiRepository = SicrediRepository().makeRequest()
    private val _eventsResponse: MutableLiveData<Any> = MutableLiveData()
    val eventResponse: LiveData<Any> = _eventsResponse

    fun getEvents() {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                val events = apiRepository.getEvents()

                _eventsResponse.postValue(events)
            } catch (exception: Exception) {
                _eventsResponse.postValue(exception)
            }
        }
    }
}