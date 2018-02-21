package com.crioprecipitati.androidpervasive1718.networking.api

import com.crioprecipitati.androidpervasive1718.model.SessionDNS
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface SessionApi {

    @GET("session/all")
    fun getAllSessions(): Call<List<SessionDNS>>;

    @POST("session/new/{patId}")
    fun createNewSession(@Path("patId") patId: String): Call<SessionDNS>;

    @DELETE("session/close/{sessionId}")
    fun closeSessionBySessionId(@Path("sessionId") sessionId: Int): Call<ResponseBody>

}