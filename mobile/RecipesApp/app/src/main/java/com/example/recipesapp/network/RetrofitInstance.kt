package com.example.recipesapp.network

import android.content.Context
import com.example.recipesapp.RecipesApp
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://77.221.155.11:3000/"

    // Logging interceptor to log request and response details
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client: OkHttpClient
        get() {
            val sharedPreferences = RecipesApp.instance.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val token = sharedPreferences.getString("auth_token", null)

            return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor) // Add logging interceptor
                .addInterceptor { chain ->
                    val original: Request = chain.request()
                    val requestBuilder: Request.Builder = original.newBuilder()

                    // Add Authorization header if token is available
                    token?.let {
                        requestBuilder.addHeader("Authorization", "Bearer $it")
                    }

                    val request: Request = requestBuilder.build()
                    chain.proceed(request)
                }
                .build()
        }

    val api: RecipesApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecipesApi::class.java)
    }
}
