package com.crioprecipitati.androidpervasive1718.viewPresenter.login

import com.crioprecipitati.androidpervasive1718.model.SessionDNS
import com.crioprecipitati.androidpervasive1718.networking.RestApiManager
import com.crioprecipitati.androidpervasive1718.networking.api.SessionApi
import com.crioprecipitati.androidpervasive1718.networking.webSockets.NotifierWSAdapter
import com.crioprecipitati.androidpervasive1718.networking.webSockets.SessionWSAdapter
import com.crioprecipitati.androidpervasive1718.networking.webSockets.TaskWSAdapter
import com.crioprecipitati.androidpervasive1718.networking.webSockets.WSHelper
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

    override fun resumeView() {
        WSHelper.initStartingPointWS()
        onSessionListRequested()
        view?.setupUserParams()
    }

    override fun detachView() {
        super.detachView()
        CallbackHandler.detach(channels, this)
    }

    override fun onIpChanged() {
        WSHelper.initAfterIpChange()
        onSessionListRequested()
    }

    override fun onMemberTypeChanged(memberType: MemberType) {
        Prefs.memberType = memberType
        onSessionListRequested()
        view?.toggleLeaderMode(memberType.isLeader())
    }

    override fun onNewSessionRequested() {
        SessionWSAdapter.sendNewSessionMessage()
        view?.startLoadingState()
    }

    override fun onSessionCreated(sessionDNS: SessionDNS) {
        Prefs.instanceId = sessionDNS.instanceId
        Prefs.sessionId = sessionDNS.sessionId
        TaskWSAdapter.changeAddress()
        NotifierWSAdapter.changeAddress()
        WSHelper.setupWSAfterSessionHandshake()
        TaskWSAdapter.sendAddLeaderMessage()
    }

    override fun onLeaderCreationResponse(response: GenericResponse) {
        when {
            response.message == "ok" -> view?.startTeamMonitoringActivity()
        }
    }

    override fun onSessionListRequested() {
        with(RestApiManager.createService(SessionApi::class.java)) {
            when (Prefs.memberType) {
                MemberType.LEADER -> this.getAllSessionsByLeaderId(Prefs.userCF)
                MemberType.MEMBER -> this.getAllSessions()
            }
                .doOnSubscribe { view?.startLoadingState() }
                .doAfterTerminate { view?.stopLoadingState() }
                .observeOn(AndroidSchedulers.mainThread())
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
        Prefs.sessionId = this.sessionList[sessionIndex].sessionId

        TaskWSAdapter.changeAddress()
        NotifierWSAdapter.changeAddress()
        WSHelper.setupWSAfterSessionHandshake()
        view?.stopLoadingState()

        when (Prefs.memberType) {
            MemberType.LEADER -> {
                TaskWSAdapter.sendAddLeaderMessage()
            }
            MemberType.MEMBER -> {
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
            }

            when (subject) {
                WSOperations.LEADER_RESPONSE -> leaderResponseHandling()
                WSOperations.SESSION_HANDLER_RESPONSE -> sessionResponseHandling()
                WSOperations.SESSION_HANDLER_ERROR_RESPONSE -> sessionErrorResponseHandling()
                else -> Log.d("MESSAGE NOT HANDLED: $subject")
            }
        }
    }
}
