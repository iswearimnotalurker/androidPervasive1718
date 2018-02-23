package com.crioprecipitati.androidpervasive1718.networking

import com.crioprecipitati.androidpervasive1718.BuildConfig
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

class RestApiManager {

    companion object{

        fun <T : Any?> createService(clazz: Class<T>): T {

            val client = OkHttpClient().newBuilder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
                    })
                    .build()

            val retrofit = Retrofit.Builder()
                    .baseUrl("http://localhost:8500/")
                    .client(client)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .build()

            return retrofit.create(clazz)
        }
    }
}