package com.crioprecipitati.androidpervasive1718.networking.webSockets

import com.crioprecipitati.androidpervasive1718.networking.utils.WS_DEFAULT_NOTIFIER_URI
import com.crioprecipitati.androidpervasive1718.networking.utils.WS_DEFAULT_TASK_URI
import trikita.log.Log

/**
 * Created by Edoardo Antonini on 21/02/2018.
 */
abstract class WSAdapter(val messageCallback: WSCallbacks){
    lateinit var webSocket: BaseWebSocket
    protected lateinit var baseAddress:String
    init {
        initializeBaseAddress()
        initWS()
    }

    abstract fun initializeBaseAddress()

    fun initWS(){
        webSocket = BaseWebSocket(baseAddress, messageCallback::onMessageReceived)
    }

    fun closeWS(){
        webSocket.close()
    }
}
class TaskWSAdapter(messageCallback: WSCallbacks):WSAdapter(messageCallback){

    override fun initializeBaseAddress() {
        baseAddress = WS_DEFAULT_TASK_URI
    }

}

class NotifierWSAdapter(messageCallback: WSCallbacks):WSAdapter(messageCallback){

    override fun initializeBaseAddress() {
        baseAddress = WS_DEFAULT_TASK_URI
    }
}
