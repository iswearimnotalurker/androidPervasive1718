package com.crioprecipitati.androidpervasive1718.networking.handlers

import com.crioprecipitati.androidpervasive1718.model.SessionDNS
import com.crioprecipitati.androidpervasive1718.networking.RestApiManager
import io.reactivex.Observable


class SessionApiHandler(private val api: RestApiManager = RestApiManager()) {

    fun getAllSessions(): Observable<List<SessionDNS>> {
        return Observable.create {
            subscriber ->
            val callResponse = api.getAllSessions()
            val response = callResponse.execute()

            if (response.isSuccessful) {
                val sessions = response.body()!!
                subscriber.onNext(sessions)
                subscriber.onComplete()
            } else {
                subscriber.onError(Throwable(response.message()))
            }
        }
    }

    fun createNewSession(cf: String): Observable<SessionDNS> {
        return Observable.create {
            subscriber ->
            val callResponse = api.createNewSession(cf)
            val response = callResponse.execute()

            if (response.isSuccessful) {
                val session = response.body()!!.also {
                    SessionDNS(it.sessionId, it.patId, it.microTaskAddress)
                }
                subscriber.onNext(session)
                subscriber.onComplete()
            } else {
                subscriber.onError(Throwable(response.message()))
            }
        }
    }

    fun closeSessionBySessionId(sessionId: Int): Observable<String> {
        return Observable.create {
            subscriber ->
            val callResponse = api.closeSessionBySessionId(sessionId)
            val response = callResponse.execute()

            if (response.isSuccessful) {
                println("Session closed")
                subscriber.onNext(response.body().toString())
                subscriber.onComplete()
            } else {
                if (response.code()==404) {
                    println("Session not found")
                    subscriber.onNext(response.message())
                }
                else {
                    println("Internal server error")
                    subscriber.onError(Throwable(response.message()))
                }
            }
        }
    }
}