package com.crioprecipitati.androidpervasive1718.utils

import com.crioprecipitati.androidpervasive1718.networking.webSockets.WSCallbacks
import model.PayloadWrapper

interface WSObserver {

    fun update(payloadWrapper: PayloadWrapper)

}

interface WSSubject {

    val observers: MutableMap<String, MutableList<WSObserver>>

    fun attach(wsOperation: String, observer: WSObserver)

    fun detach(wsOperation: String, observer: WSObserver)

    fun notifyAllObservers(wsOperation: String, payloadWrapper: PayloadWrapper)

}

object CallbackHandler : WSCallbacks, WSSubject {

    override val observers: MutableMap<String, MutableList<WSObserver>> = mutableMapOf()

    override fun attach(wsOperation: String, observer: WSObserver) {
        observers[wsOperation]?.add(observer)
    }

    override fun detach(wsOperation: String, observer: WSObserver) {
        observers[wsOperation]?.remove(observer)
    }

    override fun notifyAllObservers(wsOperation: String, payloadWrapper: PayloadWrapper) {
        observers[wsOperation]?.forEach { it.update(payloadWrapper) }
    }

    override fun onMessageReceived(messageString: String?) {
        with(GsonInitializer.fromJson(messageString!!, PayloadWrapper::class.java)) {
            notifyAllObservers(this.subject.name, this)
        }
    }
}