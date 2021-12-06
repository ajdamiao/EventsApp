package com.example.eventsapp.data

import com.example.eventsapp.model.CheckIn
import com.example.eventsapp.model.Event
import com.example.eventsapp.model.EventDetail
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface SicrediApi {

    @GET("events/")
    suspend fun getEvents(): ArrayList<Event>

    @GET("events/{id}")
    suspend fun getEventDetail(@Path("id") id: String): EventDetail

    @POST("checkin")
    suspend fun checkInEvent(@Body checkIn: CheckIn): Response<*>
}