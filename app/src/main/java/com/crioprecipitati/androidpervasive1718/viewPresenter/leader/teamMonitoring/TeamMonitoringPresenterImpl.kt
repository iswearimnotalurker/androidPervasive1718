package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.networking.RestApiManager
import com.crioprecipitati.androidpervasive1718.networking.api.SessionApi
import com.crioprecipitati.androidpervasive1718.networking.webSockets.TaskWSAdapter
import com.crioprecipitati.androidpervasive1718.networking.webSockets.WSCallbacks
import com.crioprecipitati.androidpervasive1718.utils.toJson
import io.reactivex.android.schedulers.AndroidSchedulers
import model.MembersAdditionNotification
import model.PayloadWrapper
import model.WSOperations

class TeamMonitoringPresenterImpl : TeamMonitoringContract.TeamMonitoringPresenter, WSCallbacks {

    override fun onSessionClosed(sessionId: Int) {

        RestApiManager
                .createService(SessionApi::class.java)
                .closeSessionBySessionId(sessionId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { /*loginView?.startLoadingState()*/ }
                .doAfterTerminate { /*loginView?.stopLoadingState()*/ }
                .subscribe(
                        { message ->
                            // View logic here
                            println(message)
                        },
                        { e ->
                            //Snackbar.make(session_list, e.message ?: "", Snackbar.LENGTH_LONG).show()
                            println(e.message)
                        }
                )
    }

    private val webSocketHelper: TaskWSAdapter = TaskWSAdapter(this)


    override fun attachView(view: TeamMonitoringContract.TeamMonitoringView) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
