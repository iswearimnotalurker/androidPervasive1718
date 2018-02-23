package com.crioprecipitati.androidpervasive1718.viewPresenter.member.taskMonitoring

import com.crioprecipitati.androidpervasive1718.base.BasePresenterImpl
import com.crioprecipitati.androidpervasive1718.model.Activity
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.model.Status
import com.crioprecipitati.androidpervasive1718.model.Task
import com.crioprecipitati.androidpervasive1718.networking.webSockets.NotifierWSAdapter
import com.crioprecipitati.androidpervasive1718.networking.webSockets.TaskWSAdapter
import com.crioprecipitati.androidpervasive1718.utils.Prefs
import com.crioprecipitati.androidpervasive1718.utils.toJson
import model.PayloadWrapper
import model.TaskAssignment
import model.WSOperations
import java.sql.Timestamp
import java.util.*

object TaskMonitoringPresenterImpl : BasePresenterImpl<TaskMonitoringContract.TaskMonitoringView>(), TaskMonitoringContract.TaskMonitoringPresenter {

    private val taskWebSocketHelper: TaskWSAdapter = TaskWSAdapter
    private val notifierWebSocketHelper: NotifierWSAdapter = NotifierWSAdapter

    override fun onTaskCompletionRequested() {
        //mock
        val member: Member = Member.emptyMember()
        val activity = Activity(1,"Ciao",1,"FGT",2)
        val task = Task(0,Prefs.sessionId,member.id, Timestamp(Date().time - 1000),Timestamp(Date().time),activity.id,Status.FINISHED.id)

        taskWebSocketHelper.webSocket.send(PayloadWrapper(Prefs.sessionId,WSOperations.CHANGE_TASK_STATUS,TaskAssignment(member,task).toJson()).toJson())
        notifierWebSocketHelper.webSocket.send(PayloadWrapper(Prefs.sessionId,WSOperations.CLOSE,Member.defaultMember().toJson()).toJson())
    }
}