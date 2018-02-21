package com.crioprecipitati.androidpervasive1718.networking

import trikita.log.Log

/**
 * Created by Edoardo Antonini on 21/02/2018.
 */

class TaskWSAdapter(private val messageCallback: WSLeaderCallbacks){
    lateinit var taskWS: BaseWebSocket

    var baseAddress: String = WS_DEFAULT_TASK_URI
        set(value) {
            field = "ws://$value"
            value.split("ws://").last()
            println("Now connecting to $value")
            Log.d("Now connecting to $value")
        }

    init {
        initWS()
    }

    fun initWS(){
        taskWS = BaseWebSocket(baseAddress,messageCallback::onMemberArrived)
        taskWS.send("dodo")
        println("dodo sent")
    }

    fun closeWS(){
        taskWS.close()
    }

}
