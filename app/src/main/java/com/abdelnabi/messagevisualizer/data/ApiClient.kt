package com.abdelnabi.messagevisualizer.data

import com.google.gson.GsonBuilder
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class  ApiClient private constructor() {
    companion object {
        var instance: ApiClient? = null
            private set
            get() {
                field = field ?: ApiClient()
                return field
            }

    }

    private val BASE_URL: String = "https://spreadsheets.google.com/"
    private var apiInterface: ApiInterface

    init {

        val okClient = OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(
                LoggingInterceptor.Builder()
                .setLevel(Level.BODY)
                .build())
            .build()
        val gson = GsonBuilder()
            .setLenient()
            .create()
        val retrofitBuild = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okClient)
            .build()
        apiInterface = retrofitBuild.create(ApiInterface::class.java)
    }


}