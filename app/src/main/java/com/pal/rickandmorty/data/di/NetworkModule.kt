package com.pal.rickandmorty.data.di

import android.content.Context
import android.content.pm.ApplicationInfo
import com.pal.rickandmorty.data.network.BASE_URL
import com.pal.rickandmorty.data.network.ResultCallAdapterFactory
import com.pal.rickandmorty.data.network.RickAndMortyApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Reusable
    @Provides
    fun provideOkhttpClient(
        @ApplicationContext context: Context
    ): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (isDebuggable(context)) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }

        val builder = OkHttpClient.Builder()
            .connectTimeout(5L, TimeUnit.SECONDS)
            .readTimeout(5L, TimeUnit.SECONDS)
            .writeTimeout(5L, TimeUnit.SECONDS)
            .callTimeout(5L, TimeUnit.SECONDS)
            .addNetworkInterceptor(loggingInterceptor)

        return builder.build()
    }

    @Reusable
    @Provides
    fun getRetrofit(okHttpClient: OkHttpClient): Retrofit {

        val moshi = Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory()).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(ResultCallAdapterFactory())
            .client(okHttpClient)
            .build()
    }

    @Reusable
    @Provides
    fun getAPI(retrofit: Retrofit): RickAndMortyApi =
        retrofit.create(RickAndMortyApi::class.java)

    private fun isDebuggable(context: Context): Boolean {
        return (context.applicationInfo.flags
                and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }
}