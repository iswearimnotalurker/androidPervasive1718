package com.crioprecipitati.androidpervasive1718.networking.webSockets

import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.networking.utils.WS_DEFAULT_TASK_URI
import com.crioprecipitati.androidpervasive1718.utils.toJson
import model.MembersAdditionNotification
import model.PayloadWrapper
import model.WSOperations
import trikita.log.Log

/**
 * Created by Edoardo Antonini on 21/02/2018.
 */

class TaskWSAdapter(private val messageCallback: WSLeaderCallbacks){
    lateinit var taskWS: BaseWebSocket

    var members:List<Member> = listOf()

    var baseAddress: String = WS_DEFAULT_TASK_URI
        set(value) {
            field = "ws://$value"
            value.split("ws://").last()
            println("Now connecting to $value")
            Log.d("Now connecting to $value")
        }

    init {
        members += (Member(1,"Leader"))
        initWS()
    }

    fun initWS(){
        taskWS = BaseWebSocket(baseAddress, messageCallback::onMemberArrived)
        val message = PayloadWrapper(0,
                WSOperations.ADD_LEADER, MembersAdditionNotification(members).toJson())
        taskWS.send(message.toJson())
    }

    fun closeWS(){
        taskWS.close()
    }

}
