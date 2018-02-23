package com.crioprecipitati.androidpervasive1718.utils

import com.crioprecipitati.androidpervasive1718.networking.webSockets.WSCallbacks
import model.PayloadWrapper
import model.WSOperations

interface WSObserver {

    fun update(payloadWrapper: PayloadWrapper)

}

interface WSSubject {

    val observers: MutableMap<String, MutableList<WSObserver>>

    fun attach(wsOperation: WSOperations, observer: WSObserver)

    fun attach(wsOperations: List<WSOperations>, observer: WSObserver)

    fun detach(wsOperation: WSOperations, observer: WSObserver)

    fun detach(wsOperations: List<WSOperations>, observer: WSObserver)

    fun notifyAllObservers(wsOperation: WSOperations, payloadWrapper: PayloadWrapper)

}

object CallbackHandler : WSCallbacks, WSSubject {

    override val observers: MutableMap<String, MutableList<WSObserver>> = mutableMapOf()

    override fun attach(wsOperation: WSOperations, observer: WSObserver) {
        observers[wsOperation.name]?.add(observer)
    }

    override fun attach(wsOperations: List<WSOperations>, observer: WSObserver) {
        wsOperations.forEach { attach(it, observer) }
    }

    override fun detach(wsOperation: WSOperations, observer: WSObserver) {
        observers[wsOperation.name]?.remove(observer)
    }

    override fun detach(wsOperations: List<WSOperations>, observer: WSObserver) {
        wsOperations.forEach { attach(it, observer) }
    }

    override fun notifyAllObservers(wsOperation: WSOperations, payloadWrapper: PayloadWrapper) {
        observers[wsOperation.name]?.forEach { it.update(payloadWrapper) }
    }

    override fun onMessageReceived(messageString: String?) {
        with(GsonInitializer.fromJson(messageString!!, PayloadWrapper::class.java)) {
            notifyAllObservers(this.subject, this)
        }
    }
}