package com.crioprecipitati.androidpervasive1718.viewPresenter.login

import com.chibatching.kotpref.Kotpref
import com.crioprecipitati.androidpervasive1718.base.BasePresenterImpl
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.model.SessionDNS
import com.crioprecipitati.androidpervasive1718.networking.RestApiManager
import com.crioprecipitati.androidpervasive1718.networking.api.SessionApi
import com.crioprecipitati.androidpervasive1718.networking.webSockets.TaskWSAdapter
import com.crioprecipitati.androidpervasive1718.utils.Prefs
import com.crioprecipitati.androidpervasive1718.utils.WSObserver
import com.crioprecipitati.androidpervasive1718.utils.toJson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import model.*

class LoginPresenterImpl : BasePresenterImpl<LoginContract.LoginView>(), LoginContract.LoginPresenter, WSObserver {

    private lateinit var webSocketHelper: TaskWSAdapter
    private lateinit var member: Member

    init {
        Kotpref.init(view?.getContext()!!)
    }

    override fun onConnectRequested(memberType: MemberType, id: Int, name: String) {

        member = Member(id, name)

        val service = RestApiManager.createService(SessionApi::class.java)
        val observable: Observable<List<SessionDNS>>

        observable = if (memberType == MemberType.MEMBER)
            service.getAllSessions()
        else
            service.getAllSessionsByLeaderId(member.id)

        observable.observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { sessionList ->
                    view?.toggleViewForMemberType(memberType)
                    sessionList.forEach { it ->
                        println(it)
                    }
                },
                { e -> println(e.message) }
            )
    }

    override fun onNewSessionRequested(cf: String, memberType: MemberType) {

        if (memberType == MemberType.LEADER) {
            RestApiManager
                .createService(SessionApi::class.java)
                .createNewSession(cf, member.id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { sessionInfo ->
                        println(sessionInfo as SessionDNS)
                        onSessionCreated(memberType, sessionInfo.sessionId)
                        Prefs.sessionId = (sessionInfo.sessionId)
                    },
                    { e -> println(e.message) }
                )
        }
    }

    override fun onSessionSelected(memberType: MemberType, sessionId: Int) {
        // Recovery: session selected already exists and belongs to leader
        if (memberType == MemberType.LEADER) {
            val members: List<Member> = listOf(Member(1, "Leader")) // Set actual current leader
            val message = PayloadWrapper(sessionId, WSOperations.ADD_LEADER, MembersAdditionNotification(members).toJson())
            webSocketHelper.webSocket.send(message.toJson())
            // This action will trigger the response from MT with the list of members
        } else {
            val members: List<Member> = listOf(Member(2, "Member"))
            val message = PayloadWrapper(sessionId, WSOperations.ADD_MEMBER, MembersAdditionNotification(members).toJson())
            webSocketHelper.webSocket.send(message.toJson())
            view?.startTaskMonitoringActivity(member)
        }
    }

    override fun onSessionCreated(memberType: MemberType, sessionId: Int) {
        webSocketHelper = TaskWSAdapter
        if (memberType == MemberType.LEADER) {
            val members: List<Member> = listOf(Member(1, "Leader")) // Set actual current leader
            val message = PayloadWrapper(sessionId, WSOperations.ADD_LEADER, MembersAdditionNotification(members).toJson())
            webSocketHelper.webSocket.send(message.toJson())
        }
    }

    override fun onLeaderCreationResponse(response: GenericResponse) {
        if (response.message == "ok") {
            view?.startTeamMonitoringActivity(member)
        }
    }

    override fun update(payloadWrapper: PayloadWrapper) {
        with(payloadWrapper) {

            fun leaderResponseHandling() {
                val leaderResponse: GenericResponse = this.objectify(body)
                onLeaderCreationResponse(leaderResponse)
            }

            when (subject) {
                WSOperations.LEADER_RESPONSE -> leaderResponseHandling()
                else -> null
            }
        }
    }

}
