package com.crioprecipitati.androidpervasive1718.networking.webSockets

import com.crioprecipitati.androidpervasive1718.utils.WS_DEFAULT_TASK_URI
import com.crioprecipitati.androidpervasive1718.utils.CallbackHandler

/**
 * Created by Edoardo Antonini on 21/02/2018.
 */
abstract class WSAdapter(val baseAddress:String, val messageCallback: WSCallbacks){
    lateinit var webSocket: BaseWebSocket

    init {
        initWS()
    }

    fun initWS(){
        webSocket = BaseWebSocket(baseAddress, messageCallback::onMessageReceived)
    }

    fun closeWS(){
        webSocket.close()
    }
}
class TaskWSAdapter:WSAdapter(WS_DEFAULT_TASK_URI, CallbackHandler())

class NotifierWSAdapter:WSAdapter(WS_DEFAULT_TASK_URI,CallbackHandler())
