package com.example.eventsapp.model

data class Event (
    val id: String,
    val title: String?,
    val date: Long,
    val price: String?,
    val description: String?,
    val image: String?,
    val longitude: Double?,
    val latitude: Double?,
)