package com.test.ocbc.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.test.ocbc.BuildConfig
import com.test.ocbc.BuildConfig.DEBUG
import com.test.ocbc.data.source.network.OCBCService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val CLIENT_TIME_OUT = 10_000L // milliseconds

    @Singleton
    @Provides
    fun providesOCBCService(
        retrofit: Retrofit
    ): OCBCService = retrofit.create(OCBCService::class.java)

    @Singleton
    @Provides
    fun providesRetrofit(
        gson: Gson,
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_API)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Singleton
    @Provides
    fun providesOkHttpClient(
        interceptor: HttpLoggingInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
            .writeTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(CLIENT_TIME_OUT, TimeUnit.SECONDS)
            .followRedirects(true)
            .followSslRedirects(true)
            .retryOnConnectionFailure(true)
            .build()
    }

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Timber.i(message)
        }.apply {
            level =
                if (DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }
}