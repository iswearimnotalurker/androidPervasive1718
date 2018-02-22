package com.crioprecipitati.androidpervasive1718.viewPresenter.login

import com.crioprecipitati.androidpervasive1718.base.BasePresenterImpl
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.model.SessionDNS
import com.crioprecipitati.androidpervasive1718.networking.RestApiManager
import com.crioprecipitati.androidpervasive1718.networking.api.SessionApi
import com.crioprecipitati.androidpervasive1718.networking.webSockets.TaskWSAdapter
import com.crioprecipitati.androidpervasive1718.utils.toJson
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import model.GenericResponse
import model.MembersAdditionNotification
import model.PayloadWrapper
import model.WSOperations

object LoginPresenterImpl : BasePresenterImpl<LoginContract.LoginView>(), LoginContract.LoginPresenter {

    private val webSocketHelper: TaskWSAdapter = TaskWSAdapter
    override var sessionId: Int = -1
    private lateinit var member: Member
    override var view: LoginContract.LoginView? = null


    override fun onConnectRequested(memberType: MemberType, id: Int, name: String) {

        member = Member(id, name)

        val service = RestApiManager.createService(SessionApi::class.java)
        val observable: Observable<List<SessionDNS>>

        if (memberType == MemberType.MEMBER)
            observable = service.getAllSessions()
        else
            observable = service.getAllSessionsByLeaderId(member.id)

        observable.observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { /*view
           ?.startLoadingState()*/ }
            .doAfterTerminate { /*view
           ?.stopLoadingState()*/ }
            .subscribe(
                    { sessionList ->
                        view?.toggleViewForMemberType(memberType)
                        // View logic here
                        sessionList.forEach {
                            it -> println(it)
                        }
                    },
                    { e ->
                        //Snackbar.make(session_list, e.message ?: "", Snackbar.LENGTH_LONG).show()
                        println(e.message)
                    }
            )
    }

    override fun onNewSessionRequested(cf: String, memberType: MemberType) {

        if (memberType == MemberType.LEADER) {
            RestApiManager
                .createService(SessionApi::class.java)
                .createNewSession(cf)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { /*view
               ?.startLoadingState()*/ }
                .doAfterTerminate { /*view
               ?.stopLoadingState()*/ }
                .subscribe(
                        { sessionInfo ->
                            println(sessionInfo as SessionDNS)
                            onSessionCreated(memberType, sessionInfo.sessionId)
                            sessionId = sessionInfo.sessionId
                        },
                        { e ->
                            //Snackbar.make(session_list, e.message ?: "", Snackbar.LENGTH_LONG).show()
                            println(e.message)
                        }
                )
        }
    }

    override fun onSessionSelected(memberType: MemberType, sessionId: Int) {
        // Recovery: session selected already exists and belongs to leader
        if (memberType == MemberType.LEADER) {
            val members: List<Member> = listOf(Member(1,"Leader")) // Set actual current leader
            val message = PayloadWrapper(sessionId, WSOperations.ADD_LEADER, MembersAdditionNotification(members).toJson())
            webSocketHelper.webSocket.send(message.toJson())
            // This action will trigger the response from MT with the list of members
        }
        else {
            val members: List<Member> = listOf(Member(2,"Member"))
            val message = PayloadWrapper(sessionId, WSOperations.ADD_MEMBER, MembersAdditionNotification(members).toJson())
            webSocketHelper.webSocket.send(message.toJson())
        }
    }

    override fun onSessionCreated(memberType: MemberType, sessionId: Int) {
        if (memberType == MemberType.LEADER) {
            val members: List<Member> = listOf(Member(1,"Leader")) // Set actual current leader
            val message = PayloadWrapper(sessionId, WSOperations.ADD_LEADER, MembersAdditionNotification(members).toJson())
            webSocketHelper.webSocket.send(message.toJson())
        }
    }

    override fun onLeaderCreationResponse(response: GenericResponse){
        if(response.message == "ok"){
            //TODO startActivity teamMonitoring
        }
    }
}