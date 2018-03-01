package com.crioprecipitati.androidpervasive1718.viewPresenter.member.taskMonitoring

import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.networking.webSockets.NotifierWSAdapter
import com.crioprecipitati.androidpervasive1718.networking.webSockets.TaskWSAdapter
import com.crioprecipitati.androidpervasive1718.utils.CallbackHandler
import com.crioprecipitati.androidpervasive1718.utils.Prefs
import com.crioprecipitati.androidpervasive1718.utils.WSObserver
import com.crioprecipitati.androidpervasive1718.utils.toJson
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BasePresenterImpl
import model.*
import java.util.*

class TaskMonitoringPresenterImpl : BasePresenterImpl<TaskMonitoringContract.TaskMonitoringView>(), TaskMonitoringContract.TaskMonitoringPresenter, WSObserver {

    private val channels = listOf(WSOperations.NOTIFY)
    private val queueAssignedTask = PriorityQueue<TaskAssignment>()
    private var currentAssignedTask: TaskAssignment? = null

    override fun attachView(view: TaskMonitoringContract.TaskMonitoringView) {
        super.attachView(view)
        CallbackHandler.attach(channels, this)
    }

    override fun detachView() {
        super.detachView()
        CallbackHandler.detach(channels, this)
    }

    override fun onTaskCompletionRequested() {
        //mock
//        val member: Member = Member.emptyMember()
//        val activity = Activity(1, "Ciao", 1, "FGT", 2)
//        val task = Task(0, Prefs.sessionId, member.userCF, Timestamp(Date().time - 1000), Timestamp(Date().time), activity.id, Status.FINISHED.id)
//
//        TaskWSAdapter.send(PayloadWrapper(Prefs.sessionId, WSOperations.CHANGE_TASK_STATUS, TaskAssignment(member, task).toJson()).toJson())
//        NotifierWSAdapter.send(PayloadWrapper(Prefs.sessionId, WSOperations.CLOSE, Member.defaultMember().toJson()).toJson())TaskWSAdapter.send(PayloadWrapper(Prefs.sessionId, WSOperations.CHANGE_TASK_STATUS, TaskAssignment(member, task).toJson()).toJson())
//
        //TODO PERCHé DA UNA PARTE CHANGE TASK STATUS E DALL' ALTRA  CLOSE TAPIOCA
        currentAssignedTask?.also {
            TaskWSAdapter.send(PayloadWrapper(Prefs.sessionId, WSOperations.CHANGE_TASK_STATUS, it.toJson()).toJson())
            NotifierWSAdapter.send(PayloadWrapper(Prefs.sessionId, WSOperations.CLOSE, Member.defaultMember().toJson()).toJson())
        }
        try {
            currentAssignedTask = queueAssignedTask.remove()
        } catch (ex: NoSuchElementException) {
            currentAssignedTask = null
        }
    }

    override fun update(payloadWrapper: PayloadWrapper) {
        with(payloadWrapper) {
            fun notifyHandling() {
                val activityAddition: Notification = this.objectify(body)
                activityAddition.lifeParameter
            }

            fun manageUpdate() {
                val update: Update = this.objectify(body)
                view?.updateHealthParameterValues(update.lifeParameter, update.value)
            }

            fun newTaskAssigned() {
                queueAssignedTask.offer(this.objectify(body))
                currentAssignedTask ?: also {
                    currentAssignedTask = queueAssignedTask.remove()
                    view?.showNewTask(currentAssignedTask!!.augmentedTask)
                }
            }

            when (payloadWrapper.subject) {
                WSOperations.NOTIFY -> notifyHandling()
                WSOperations.UPDATE -> manageUpdate()
                WSOperations.ADD_TASK -> newTaskAssigned()
                else -> null
            }
        }
    }
}