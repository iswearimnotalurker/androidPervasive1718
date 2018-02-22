package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.model.Task
import com.crioprecipitati.androidpervasive1718.networking.RestApiManager
import com.crioprecipitati.androidpervasive1718.networking.api.SessionApi
import com.crioprecipitati.androidpervasive1718.networking.webSockets.NotifierWSAdapter
import com.crioprecipitati.androidpervasive1718.networking.webSockets.TaskWSAdapter
import com.crioprecipitati.androidpervasive1718.utils.toJson
import com.crioprecipitati.androidpervasive1718.viewPresenter.login.LoginContract
import com.crioprecipitati.androidpervasive1718.viewPresenter.login.LoginPresenterImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import model.PayloadWrapper
import model.TaskAssignment
import model.WSOperations

object TeamMonitoringPresenterImpl : TeamMonitoringContract.TeamMonitoringPresenter {

    private val taskWebSocketHelper: TaskWSAdapter = TaskWSAdapter
    private val notifierWebSocketHelper: NotifierWSAdapter = NotifierWSAdapter
    private val loginPresenter: LoginContract.LoginPresenter = LoginPresenterImpl
    override lateinit var view:TeamMonitoringContract.TeamMonitoringView

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



    override fun attachView(view: TeamMonitoringContract.TeamMonitoringView) {
        this.view = view
    }

    override fun detachView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTaskDeleted() {
        taskWebSocketHelper.webSocket.send(PayloadWrapper(loginPresenter.sessionId,WSOperations.REMOVE_TASK,TaskAssignment(Member.defaultMember(), Task.defaultTask()).toJson()).toJson())
    }

    override fun onMemberSelected() {
        TODO("Start ActivitySelectionActivity") //To change body of created functions use File | Settings | File Templates.
    }

}
