package com.example.eventsapp.data.repository

import com.example.eventsapp.data.SicrediApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SicrediRepository {

    private val baseURL = "https://5f5a8f24d44d640016169133.mockapi.io/api/"

    fun makeRequest(): SicrediApi {
        return Retrofit
            .Builder()
            .baseUrl(baseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SicrediApi::class.java)
    }
}