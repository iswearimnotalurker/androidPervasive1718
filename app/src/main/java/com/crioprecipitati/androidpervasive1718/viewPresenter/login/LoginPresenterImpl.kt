package com.crioprecipitati.androidpervasive1718.viewPresenter.login

import com.crioprecipitati.androidpervasive1718.base.BasePresenterImpl
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.model.SessionAssignment
import com.crioprecipitati.androidpervasive1718.model.SessionDNS
import com.crioprecipitati.androidpervasive1718.networking.RestApiManager
import com.crioprecipitati.androidpervasive1718.networking.api.SessionApi
import com.crioprecipitati.androidpervasive1718.networking.webSockets.NotifierWSAdapter
import com.crioprecipitati.androidpervasive1718.networking.webSockets.SessionWSAdapter
import com.crioprecipitati.androidpervasive1718.networking.webSockets.TaskWSAdapter
import com.crioprecipitati.androidpervasive1718.utils.CallbackHandler
import com.crioprecipitati.androidpervasive1718.utils.Prefs
import com.crioprecipitati.androidpervasive1718.utils.WSObserver
import com.crioprecipitati.androidpervasive1718.utils.toJson
import io.reactivex.android.schedulers.AndroidSchedulers
import model.*
import trikita.log.Log

class LoginPresenterImpl : BasePresenterImpl<LoginContract.LoginView>(), LoginContract.LoginPresenter, WSObserver {

    private lateinit var member: Member

    private val channels = listOf(
        WSOperations.LEADER_RESPONSE,
        WSOperations.SESSION_HANDLER_RESPONSE,
        WSOperations.SESSION_HANDLER_ERROR_RESPONSE)

    override fun attachView(view: LoginContract.LoginView) {
        super.attachView(view)
        CallbackHandler.attach(channels, this)
    }

    override fun detachView() {
        super.detachView()
        CallbackHandler.detach(channels, this)
    }

    override fun onConnectRequested(memberType: MemberType, id: Int, name: String) {

        member = Member(id, name)

        with(RestApiManager.createService(SessionApi::class.java)) {
            when (memberType) {
                MemberType.LEADER -> this.getAllSessionsByLeaderId(member.id)
                MemberType.MEMBER -> this.getAllSessions()
            }.observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { sessionList ->
                        view?.toggleViewForMemberType(memberType)
                        sessionList.forEach { it -> Log.d(it) }
                    }, { e -> Log.d(e.message) })
        }
    }

    override fun onNewSessionRequested(cf: String, memberType: MemberType) {
//        SessionWSAdapter.send(PayloadWrapper(Prefs.sessionId, WSOperations.NEW_SESSION, SessionAssignment(cf, member.id).toJson()).toJson())
//        SessionWSAdapter.initWS()
        member = Member(0, "leader supremo")
        SessionWSAdapter.send(PayloadWrapper(0, WSOperations.NEW_SESSION, SessionAssignment(cf, member.id).toJson()).toJson())
    }

    override fun onSessionSelected(memberType: MemberType, sessionId: Int) {
        // Recovery: session selected already exists and belongs to leader
        when (memberType) {
            MemberType.LEADER -> {
                val members: List<Member> = listOf(Member(1, "Leader")) // Set actual current leader
                val message = PayloadWrapper(sessionId, WSOperations.ADD_LEADER, MembersAdditionNotification(members).toJson())
                TaskWSAdapter.send(message.toJson())
                // This action will trigger the response from MT with the list of members
            }
            MemberType.MEMBER -> {
                val members: List<Member> = listOf(Member(2, "Member"))
                val message = PayloadWrapper(sessionId, WSOperations.ADD_MEMBER, MembersAdditionNotification(members).toJson())
                TaskWSAdapter.send(message.toJson())
                view?.startTaskMonitoringActivity(member)
            }
        }
    }

    override fun onSessionCreated(sessionId: Int) {
        Log.d("created session $sessionId")
        Prefs.sessionId = sessionId
        val members: List<Member> = listOf(Member(1, "Leader")) // Set actual current leader
        val message = PayloadWrapper(sessionId, WSOperations.ADD_LEADER, MembersAdditionNotification(members).toJson())
        TaskWSAdapter.send(message.toJson())
    }

    override fun onLeaderCreationResponse(response: GenericResponse) {
        if (response.message == "ok") view?.startTeamMonitoringActivity(member)
    }

    override fun update(payloadWrapper: PayloadWrapper) {
        with(payloadWrapper) {
            fun leaderResponseHandling() {
                val leaderResponse: GenericResponse = this.objectify(body)
                onLeaderCreationResponse(leaderResponse)
            }

            fun sessionResponseHandling() {
                TaskWSAdapter.initWS()
                NotifierWSAdapter.initWS()
                SessionWSAdapter.closeWS()
                val sessionDNSResponse: SessionDNS = this.objectify(body)
                onSessionCreated(sessionDNSResponse.sessionId)
            }

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
}
