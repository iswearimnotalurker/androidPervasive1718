package com.crioprecipitati.androidpervasive1718.networking.api

import com.crioprecipitati.androidpervasive1718.model.SessionDNS
import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface SessionApi {

    @GET("sessions")
    fun getAllSessions(): Observable<List<SessionDNS>>

    @GET("sessions/{leaderCF}")
    fun getAllSessionsByLeaderId(@Path("leaderCF") userCF: String): Observable<List<SessionDNS>>

    @DELETE("sessions/{sessionId}")
    fun closeSessionBySessionId(@Path("sessionId") sessionId: Int): Observable<ResponseBody>

}