package com.crioprecipitati.androidpervasive1718.networking.webSockets

import com.crioprecipitati.androidpervasive1718.model.LifeParameters
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.model.SessionAssignment
import com.crioprecipitati.androidpervasive1718.utils.*
import model.*
import trikita.log.Log

abstract class WSAdapter(@Volatile var baseAddress: String) {
    private var webSocket: BaseWebSocket? = null

    fun initWS() {
        webSocket ?: run {
            Log.d("[START WS] $baseAddress")
            webSocket = BaseWebSocket(baseAddress, CallbackHandler::onMessageReceived)
        }
    }

    fun send(message: String) {
        Log.d("[ $baseAddress --> ] $message")
        webSocket?.send(message)
    }

    fun closeWS() {
        Log.d("[CLOSE WS] $baseAddress")
        webSocket?.close()
        webSocket = null
    }

    abstract fun changeAddress()
}

object SessionWSAdapter : WSAdapter(WS_DEFAULT_SESSION_URI) {

    fun sendNewSessionMessage() =
        SessionWSAdapter.send(PayloadWrapper(Prefs.sessionId, WSOperations.NEW_SESSION, SessionAssignment(Prefs.patientCF, Prefs.userCF).toJson()).toJson())

    override fun changeAddress() {
        super.baseAddress = WS_DEFAULT_SESSION_URI
    }

}

object TaskWSAdapter : WSAdapter(WS_DEFAULT_TASK_URI) {

    private fun sendCustomMessage(operation: WSOperations) {
        TaskWSAdapter.send(
            PayloadWrapper(
                Prefs.sessionId,
                operation,
                MembersAdditionNotification(listOf(Member(Prefs.userCF))).toJson()).toJson())
    }

    fun sendAddMemberMessage() = sendCustomMessage(WSOperations.ADD_MEMBER)

    fun sendAddLeaderMessage() = sendCustomMessage(WSOperations.ADD_LEADER)

    fun sendAllMembersRequest() = TaskWSAdapter.send(
        PayloadWrapper(
            Prefs.sessionId,
            WSOperations.LIST_MEMBERS_REQUEST,
            Unit.toJson()).toJson()
    )

    fun sendGetActivitiesRequest(activityTypeId: Int) = TaskWSAdapter.send(
            PayloadWrapper(
                Prefs.sessionId,
                WSOperations.GET_ALL_ACTIVITIES,
                    ActivityRequest(activityTypeId).toJson()).toJson()
    )

    override fun changeAddress(){
        super.baseAddress = "ws://$CURRENT_LOCAL_IP:820${Prefs.instanceId}/task"
    }


}

object NotifierWSAdapter : WSAdapter(WS_DEFAULT_NOTIFIER_URI) {

    fun sendSubscribeToAllParametersMessage() {
        NotifierWSAdapter.send(
            PayloadWrapper(
                Prefs.sessionId,
                WSOperations.SUBSCRIBE,
                Subscription(Member(Prefs.userCF), LifeParameters.values().toList()).toJson()).toJson())
    }

    fun sendSubscribeToParametersMessage(parameters: List<LifeParameters>) {
        NotifierWSAdapter.send(
            PayloadWrapper(
                Prefs.sessionId,
                WSOperations.SUBSCRIBE,
                Subscription(Member(Prefs.userCF), parameters).toJson()).toJson())
    }


    override fun changeAddress(){
        super.baseAddress = "ws://$CURRENT_LOCAL_IP:830${Prefs.instanceId}/instanceid/${Prefs.instanceId}/notifier"
    }
}

object WSHelper {

    var alreadyOpened = false

    fun initStartingPointWS() {
        SessionWSAdapter.initWS()
    }

    fun setupWSAfterSessionHandshake() {
        if (alreadyOpened) {
            TaskWSAdapter.closeWS()
            NotifierWSAdapter.closeWS()
        }
        SessionWSAdapter.closeWS()
        TaskWSAdapter.initWS()
        NotifierWSAdapter.initWS()
        alreadyOpened = true
    }

}