package com.crioprecipitati.androidpervasive1718.networking

import com.crioprecipitati.androidpervasive1718.model.SessionDNS
import com.crioprecipitati.androidpervasive1718.networking.api.SessionApi
import io.reactivex.schedulers.Schedulers
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

private lateinit var sessionApi: SessionApi

class RestApiManager {
    init {

        /*val client = OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
                })
                .build()*/

        val retrofit = Retrofit.Builder()
                .baseUrl("http://localhost:8500/")
                .addConverterFactory(MoshiConverterFactory.create())
                //.client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build()

        sessionApi = retrofit.create(SessionApi::class.java)
    }

    fun getAllSessions(): Call<List<SessionDNS>> = sessionApi.getAllSessions()

    fun createNewSession(cf: String): Call<SessionDNS> = sessionApi.createNewSession(cf)

    fun closeSessionBySessionId(sessionId: Int): Call<ResponseBody> = sessionApi.closeSessionBySessionId(sessionId)
}