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
                val sessions = response.body()!!.map {
                    SessionDNS(it.sessionId, it.patId, it.microTaskAddress)
                }
                subscriber.onNext(sessions)
                subscriber.onComplete()
            } else {
                subscriber.onError(Throwable(response.message()))
            }
        }
    }
}