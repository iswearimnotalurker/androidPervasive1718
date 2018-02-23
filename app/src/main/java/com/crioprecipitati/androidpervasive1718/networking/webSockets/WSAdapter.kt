package com.crioprecipitati.androidpervasive1718.networking.webSockets

import com.crioprecipitati.androidpervasive1718.utils.CallbackHandler
import com.crioprecipitati.androidpervasive1718.utils.WS_DEFAULT_NOTIFIER_URI
import com.crioprecipitati.androidpervasive1718.utils.WS_DEFAULT_SESSION_URI
import com.crioprecipitati.androidpervasive1718.utils.WS_DEFAULT_TASK_URI

abstract class WSAdapter(private val baseAddress: String) {
    lateinit var webSocket: BaseWebSocket

    init {
        initWS()
    }

    fun initWS() {
        webSocket = BaseWebSocket(baseAddress, CallbackHandler::onMessageReceived)
    }

    fun closeWS() = webSocket.close()
}

object TaskWSAdapter : WSAdapter(WS_DEFAULT_TASK_URI)

object NotifierWSAdapter : WSAdapter(WS_DEFAULT_NOTIFIER_URI)

object SessionWSAdapter : WSAdapter(WS_DEFAULT_SESSION_URI)
