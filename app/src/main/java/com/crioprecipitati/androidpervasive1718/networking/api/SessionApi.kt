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

    @GET("session/all/{leaderId}")
    fun getAllSessionsByLeaderId(@Path("leaderId") memberId: Int): Observable<List<SessionDNS>>

    @POST("session/new/{patId}/leaderid/{leaderId}")
    fun createNewSession(@Path("patId") patId: String,
                         @Path("leaderId") leaderId: Int): Observable<SessionDNS>;

    @DELETE("session/close/{sessionId}")
    fun closeSessionBySessionId(@Path("sessionId") sessionId: Int): Observable<ResponseBody>

}