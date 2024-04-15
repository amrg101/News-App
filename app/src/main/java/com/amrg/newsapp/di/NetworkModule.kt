package com.amrg.newsapp.di

import com.amrg.newsapp.BuildConfig
import com.amrg.newsapp.data.remote.ApiService
import com.amrg.newsapp.data.remote.AuthenticationInterceptor
import com.amrg.newsapp.data.remote.Constants.BASE_API_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @ApiKey
    @Provides
    fun provideApiKey(): String = BuildConfig.API_KEY

    @Singleton
    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideAuthenticationInterceptor(@ApiKey apiKey: String) = AuthenticationInterceptor(apiKey)

    @Singleton
    @Provides
    fun provideApiService(
        gsonFactory: GsonConverterFactory,
        authenticationInterceptor: AuthenticationInterceptor
    ): ApiService {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authenticationInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .client(client)
            .addConverterFactory(gsonFactory)
            .build()
            .create(ApiService::class.java)
    }
}