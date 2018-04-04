package com.crioprecipitati.androidpervasive1718.networking.webSockets

import com.crioprecipitati.androidpervasive1718.model.LifeParameters
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.model.SessionAssignment
import com.crioprecipitati.androidpervasive1718.utils.*
import model.PayloadWrapper
import model.Subscription
import model.WSOperations
import trikita.log.Log

abstract class WSAdapter(@Volatile var baseAddress: String) {
    private var webSocket: BaseWebSocket? = null

    private fun instantiateWS() {
        Log.d("[START WS] $baseAddress")
        webSocket = BaseWebSocket(baseAddress, CallbackHandler::onMessageReceived)
    }

    fun initWS(forceReInit: Boolean = false) {
        when {
            forceReInit -> instantiateWS()
            webSocket == null -> instantiateWS()
            else -> webSocket
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
        super.baseAddress = "ws://${Prefs.ip}:8501/session"
    }

}

object TaskWSAdapter : WSAdapter(WS_DEFAULT_TASK_URI) {

    private fun sendCustomMessage(operation: WSOperations) {
        TaskWSAdapter.send(
            PayloadWrapper(
                Prefs.sessionId,
                operation,
                Member(Prefs.userCF).toJson()).toJson())
    }

    fun sendAddMemberMessage() = sendCustomMessage(WSOperations.ADD_MEMBER)

    fun sendAddLeaderMessage() = sendCustomMessage(WSOperations.ADD_LEADER)

    fun sendAllMembersRequest() = TaskWSAdapter.send(
        PayloadWrapper(
            Prefs.sessionId,
            WSOperations.LIST_MEMBERS_REQUEST,
            Unit.toJson()).toJson()
    )

    fun sendGetActivitiesRequest() = TaskWSAdapter.send(
            PayloadWrapper(
                Prefs.sessionId,
                WSOperations.GET_ALL_ACTIVITIES,
                Unit.toJson()).toJson()
    )

    override fun changeAddress(){
        super.baseAddress = "ws://${Prefs.ip}:820${Prefs.instanceId}/task"
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
        super.baseAddress = "ws://${Prefs.ip}:830${Prefs.instanceId}/instanceid/${Prefs.instanceId}/notifier"
    }
}

object WSHelper {

    private var alreadyOpened = false

    fun initStartingPointWS() {
        SessionWSAdapter.changeAddress()
        SessionWSAdapter.initWS()
    }

    fun initAfterIpChange() {
        SessionWSAdapter.changeAddress()
        SessionWSAdapter.initWS(true)
        alreadyOpened = false
    }

    fun setupWSAfterSessionHandshake() {
        if (alreadyOpened) {
            TaskWSAdapter.closeWS()
            NotifierWSAdapter.closeWS()
        }
        SessionWSAdapter.closeWS()
        TaskWSAdapter.changeAddress()
        NotifierWSAdapter.changeAddress()
        TaskWSAdapter.initWS()
        NotifierWSAdapter.initWS()
        alreadyOpened = true
    }

}