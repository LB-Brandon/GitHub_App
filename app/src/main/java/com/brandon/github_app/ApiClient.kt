package com.brandon.github_app

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private val okHttpClient = OkHttpClient.Builder().addInterceptor {
        val request =
            it.request().newBuilder().addHeader("Authorization", "${BuildConfig.API_KEY}").build()
        it.proceed(request)
    }.build()

    private const val BASE_URL = "https://api.github.com/"
    val retrofit: Retrofit = Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()


}