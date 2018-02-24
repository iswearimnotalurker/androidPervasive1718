package com.crioprecipitati.androidpervasive1718.networking.webSockets

import com.crioprecipitati.androidpervasive1718.utils.CallbackHandler
import com.crioprecipitati.androidpervasive1718.utils.WS_DEFAULT_NOTIFIER_URI
import com.crioprecipitati.androidpervasive1718.utils.WS_DEFAULT_SESSION_URI
import com.crioprecipitati.androidpervasive1718.utils.WS_DEFAULT_TASK_URI
import trikita.log.Log

abstract class WSAdapter(private val baseAddress: String) {
    private lateinit var webSocket: BaseWebSocket

    fun initWS() {
        Log.d("[START WS] $baseAddress")
        webSocket = BaseWebSocket(baseAddress, CallbackHandler::onMessageReceived)
    }

    fun send(message: String) {
        Log.d("[ $baseAddress --> ] $message")
        webSocket.send(message)
    }

    fun closeWS() = webSocket.close()
}

object SessionWSAdapter : WSAdapter(WS_DEFAULT_SESSION_URI)

object TaskWSAdapter : WSAdapter(WS_DEFAULT_TASK_URI)

object NotifierWSAdapter : WSAdapter(WS_DEFAULT_NOTIFIER_URI)
