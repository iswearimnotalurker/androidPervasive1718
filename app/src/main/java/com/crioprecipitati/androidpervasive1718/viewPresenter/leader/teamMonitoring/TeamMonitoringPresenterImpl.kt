package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.networking.webSockets.TaskWSAdapter
import com.crioprecipitati.androidpervasive1718.networking.handlers.SessionApiHandler
import com.crioprecipitati.androidpervasive1718.networking.webSockets.WSCallbacks
import com.crioprecipitati.androidpervasive1718.utils.toJson
import io.reactivex.schedulers.Schedulers
import model.MembersAdditionNotification
import model.PayloadWrapper
import model.WSOperations

object TeamMonitoringPresenterImpl : TeamMonitoringContract.TeamMonitoringPresenter, WSCallbacks {

    private val webSocketHelper: TaskWSAdapter = TaskWSAdapter()
    override lateinit var view:TeamMonitoringContract.TeamMonitoringView

    override fun onSessionClosed(sessionId: Int) {
        SessionApiHandler().closeSessionBySessionId(sessionId)
                .subscribeOn(Schedulers.io())
                .subscribe(
                        { message ->
                            //(session_list.adapter as SessionAdapter).addSession(retrievedSessions)
                            println(message)
                            // MT.addLeader(myself)
                        },
                        { e ->
                            //Snackbar.make(session_list, e.message ?: "", Snackbar.LENGTH_LONG).show()
                            println(e.message)
                        }
                )
    }



    override fun attachView(view: TeamMonitoringContract.TeamMonitoringView) {
        this.view = view
    }

    override fun detachView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTaskDeleted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMemberSelected() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMessageReceived(messageString: String?) {
        println("Ricevuto: "+messageString) //To change body of created functions use File | Settings | File Templates.
    }

    fun sendAddLeaderRequest(){
        var members: List<Member> = listOf(Member(1,"Leader"))
        val message = PayloadWrapper(0, WSOperations.ADD_LEADER, MembersAdditionNotification(members).toJson())
        webSocketHelper.webSocket.send(message.toJson())
    }


}
