package com.crioprecipitati.androidpervasive1718.networking.api

import com.crioprecipitati.androidpervasive1718.model.SessionDNS
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET

interface SessionApi {

    @GET("session/all")
    fun getAllSessions(): Call<List<SessionDNS>>;

}