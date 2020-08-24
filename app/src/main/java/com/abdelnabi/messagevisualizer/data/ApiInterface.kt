package com.abdelnabi.messagevisualizer.data

import retrofit2.Call
import com.abdelnabi.messagevisualizer.model.MessageModel
import retrofit2.http.GET


interface ApiInterface {

    @GET("feeds/list/0Ai2EnLApq68edEVRNU0xdW9QX1BqQXhHRl9sWDNfQXc/od6/public/basic?alt=json")
    fun getMessage(): Call<MessageModel>

}