package com.crioprecipitati.androidpervasive1718.networking.api

import com.crioprecipitati.androidpervasive1718.model.SessionDNS
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface SessionApi {

    @GET("session/all")
    fun getAllSessions(): Observable<List<SessionDNS>>

    @GET("session/all/{leaderCF}")
    fun getAllSessionsByLeaderId(@Path("leaderCF") userCF: String): Observable<List<SessionDNS>>

    @DELETE("session/close/{sessionId}")
    fun closeSessionBySessionId(@Path("sessionId") sessionId: Int): Observable<ResponseBody>

}