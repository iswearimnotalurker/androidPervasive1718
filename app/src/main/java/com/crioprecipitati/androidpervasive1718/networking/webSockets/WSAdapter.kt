package com.crioprecipitati.androidpervasive1718.networking.webSockets

import com.crioprecipitati.androidpervasive1718.model.LifeParameters
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.model.SessionAssignment
import com.crioprecipitati.androidpervasive1718.utils.*
import model.MembersAdditionNotification
import model.PayloadWrapper
import model.Subscription
import model.WSOperations
import trikita.log.Log

abstract class WSAdapter(private val baseAddress: String) {
    private var webSocket: BaseWebSocket? = null

    fun initWS() {
        if (webSocket == null) {
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
}

object SessionWSAdapter : WSAdapter(WS_DEFAULT_SESSION_URI) {

    fun sendNewSessionMessage() {
        Log.d("sendNewSessionMessage: AAA MANDATO SESSION")
        SessionWSAdapter.send(PayloadWrapper(Prefs.sessionId, WSOperations.NEW_SESSION, SessionAssignment(Prefs.patientCF, Prefs.userCF).toJson()).toJson())
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

    fun sendAddMemberMessage() {
        sendCustomMessage(WSOperations.ADD_MEMBER)
    }

    fun sendAddLeaderMessage() {
        Log.d("sendAddLeaderMessage: AAA MANDATO LEADER")
        sendCustomMessage(WSOperations.ADD_LEADER)
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

}
