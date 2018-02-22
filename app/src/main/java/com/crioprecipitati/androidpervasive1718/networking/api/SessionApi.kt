package com.crioprecipitati.androidpervasive1718.networking.api

import com.crioprecipitati.androidpervasive1718.model.SessionDNS
import io.reactivex.Observable
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface SessionApi {

    @GET("session/all")
    fun getAllSessions(): Observable<List<SessionDNS>>;

    @POST("session/new/{patId}")
    fun createNewSession(@Path("patId") patId: String): Observable<SessionDNS>;

    @DELETE("session/close/{sessionId}")
    fun closeSessionBySessionId(@Path("sessionId") sessionId: Int): Observable<ResponseBody>

}