package com.crioprecipitati.androidpervasive1718.viewPresenter.login

import com.chibatching.kotpref.blockingBulk
import com.crioprecipitati.androidpervasive1718.model.SessionDNS
import com.crioprecipitati.androidpervasive1718.networking.RestApiManager
import com.crioprecipitati.androidpervasive1718.networking.api.SessionApi
import com.crioprecipitati.androidpervasive1718.networking.webSockets.NotifierWSAdapter
import com.crioprecipitati.androidpervasive1718.networking.webSockets.SessionWSAdapter
import com.crioprecipitati.androidpervasive1718.networking.webSockets.TaskWSAdapter
import com.crioprecipitati.androidpervasive1718.utils.CallbackHandler
import com.crioprecipitati.androidpervasive1718.utils.Prefs
import com.crioprecipitati.androidpervasive1718.utils.WSObserver
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BasePresenterImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import model.GenericResponse
import model.PayloadWrapper
import model.WSOperations
import model.objectify
import trikita.log.Log

class LoginPresenterImpl : BasePresenterImpl<LoginContract.LoginView>(), LoginContract.LoginPresenter, WSObserver {

    override val sessionList: MutableList<SessionDNS> = mutableListOf()

    private val channels = listOf(
        WSOperations.LEADER_RESPONSE,
        WSOperations.SESSION_HANDLER_RESPONSE,
        WSOperations.SESSION_HANDLER_ERROR_RESPONSE)

    override fun attachView(view: LoginContract.LoginView) {
        super.attachView(view)
        CallbackHandler.attach(channels, this)
    }

    override fun resumeView(){
        SessionWSAdapter.initWS()
        view?.setupUserParams(Prefs.memberType, Prefs.userCF, Prefs.patientCF)
    }

    override fun detachView() {
        super.detachView()
        CallbackHandler.detach(channels, this)
    }

    override fun onMemberTypeChanged(memberType: MemberType) {
        Prefs.memberType = memberType
        view?.toggleLeaderMode(memberType.isLeader())
    }

    override fun onNewSessionRequested() = SessionWSAdapter.sendNewSessionMessage()

    override fun onSessionCreated(sessionDNS: SessionDNS) {
        Prefs.blockingBulk {
            instanceId = sessionDNS.instanceId
            sessionId = sessionDNS.sessionId
        }
        setupWSAfterSessionHandshake()
        TaskWSAdapter.sendAddLeaderMessage()
    }

    override fun onLeaderCreationResponse(response: GenericResponse) {
        when {
            response.message == "ok" -> view?.startTeamMonitoringActivity()
        }
    }

    override fun onSessionJoinRequested() {
        with(RestApiManager.createService(SessionApi::class.java)) {
            when (Prefs.memberType) {
                MemberType.LEADER -> this.getAllSessionsByLeaderId(Prefs.userCF)
                MemberType.MEMBER -> this.getAllSessions()
            }.observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { sessionList ->
                        with(this@LoginPresenterImpl.sessionList) {
                            clear()
                            addAll(sessionList)
                            view?.showAndUpdateSessionList()
                            forEach { Log.d(it) }
                        }
                    }, { Log.d(it.message) })
        }
    }

    override fun onSessionSelected(sessionIndex: Int) {

        Prefs.instanceId = this.sessionList[sessionIndex].instanceId

        setupWSAfterSessionHandshake()

        when (Prefs.memberType) {
            MemberType.LEADER -> {
                TaskWSAdapter.sendAddLeaderMessage()
                view?.startTeamMonitoringActivity()
            }
            MemberType.MEMBER -> {
                TaskWSAdapter.sendAddMemberMessage()
                view?.startTaskMonitoringActivity()
            }
        }
    }

    override fun update(payloadWrapper: PayloadWrapper) {
        with(payloadWrapper) {
            fun leaderResponseHandling() = onLeaderCreationResponse(GenericResponse(body))

            fun sessionResponseHandling() = onSessionCreated(this.objectify(body))

            fun sessionErrorResponseHandling() {
                val sessionDNSErrorResponse: GenericResponse = this.objectify(body)
                Log.d("RECEIVED ERROR $sessionDNSErrorResponse")
                // TODO fai le cose
            }

            when (subject) {
                WSOperations.LEADER_RESPONSE -> leaderResponseHandling()
                WSOperations.SESSION_HANDLER_RESPONSE -> sessionResponseHandling()
                WSOperations.SESSION_HANDLER_ERROR_RESPONSE -> sessionErrorResponseHandling()
                else -> Log.d("MESSAGE NOT HANDLED: $subject")
            }
        }
    }

    private fun setupWSAfterSessionHandshake() {
        SessionWSAdapter.closeWS()
        TaskWSAdapter.initWS()
        NotifierWSAdapter.initWS()
    }
}
