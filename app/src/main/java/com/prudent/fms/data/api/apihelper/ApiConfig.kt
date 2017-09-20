package com.prudent.fms.data.api.apihelper

import com.google.gson.GsonBuilder
import com.prudent.fms.data.api.apihelper.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Dharmik Patel on 20-Jul-17.
 */
class ApiConfig {

    lateinit var service: ApiService

    val client = OkHttpClient().newBuilder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://27.109.20.118/Salonapi/")
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .client(client)
                .build()
        createServices(retrofit)
    }

    fun createServices(retrofit: Retrofit) {
        service = retrofit.create(ApiService::class.java)
    }
}