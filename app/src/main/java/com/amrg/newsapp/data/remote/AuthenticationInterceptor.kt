package com.amrg.newsapp.data.remote

import com.amrg.newsapp.di.ApiKey
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor(@ApiKey private val apiKey: String) :
    Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response =
        chain.request().let { originalRequest ->

            val newRequest = originalRequest.newBuilder()
                .addHeader("X-Api-Key", apiKey)
                .build()

            chain.proceed(newRequest)
        }
}