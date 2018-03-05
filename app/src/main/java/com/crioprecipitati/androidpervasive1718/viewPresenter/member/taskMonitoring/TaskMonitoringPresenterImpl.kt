package com.crioprecipitati.androidpervasive1718.viewPresenter.member.taskMonitoring

import android.util.Log
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.model.Status
import com.crioprecipitati.androidpervasive1718.networking.webSockets.NotifierWSAdapter
import com.crioprecipitati.androidpervasive1718.networking.webSockets.TaskWSAdapter
import com.crioprecipitati.androidpervasive1718.utils.CallbackHandler
import com.crioprecipitati.androidpervasive1718.utils.Prefs
import com.crioprecipitati.androidpervasive1718.utils.WSObserver
import com.crioprecipitati.androidpervasive1718.utils.toJson
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BasePresenterImpl
import model.*
import java.util.*
import kotlin.Comparator

open class TaskMonitoringPresenterImpl : BasePresenterImpl<TaskMonitoringContract.TaskMonitoringView>(), TaskMonitoringContract.TaskMonitoringPresenter, WSObserver {

    private val channels = listOf(WSOperations.NOTIFY,
            WSOperations.UPDATE,
                                    WSOperations.MEMBER_COMEBACK_RESPONSE,
            WSOperations.ADD_TASK,
            WSOperations.ANSWER)

    private val queueAssignedTask = PriorityQueue<TaskAssignment>(10, Comparator<TaskAssignment>{
        x,y -> x.task.task.startTime.compareTo(y.task.task.startTime)
    })
    private var currentAssignedTask: TaskAssignment? = null

    override fun attachView(view: TaskMonitoringContract.TaskMonitoringView) {
        super.attachView(view)
        Log.d("Task monitoring pres", "Mi sto attaccando alla view")
        CallbackHandler.attach(channels, this)
        TaskWSAdapter.sendAddMemberMessage()
    }

    override fun detachView() {
        super.detachView()
        Log.d("Task monitoring pres", "Mi sto staccando dalla view")
        CallbackHandler.detach(channels, this)
    }

    override fun onTaskCompletionRequested() {
        currentAssignedTask?.run {
            this.task.task.statusId = Status.FINISHED.id
            TaskWSAdapter.send(PayloadWrapper(Prefs.sessionId, WSOperations.CHANGE_TASK_STATUS, this.toJson()).toJson())
            NotifierWSAdapter.send(PayloadWrapper(Prefs.sessionId, WSOperations.CLOSE, Member(Prefs.userCF).toJson()).toJson())
            updateTheCurrentTask()
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
                val task: TaskAssignment = this.objectify(body)
                queueAssignedTask.offer(task)
                currentAssignedTask ?: run {
                    updateTheCurrentTask()
                }
            }

            fun loadMemberTasks(){
                val memberTaskList: AugmentedMembersAdditionNotification = this.objectify(body)
                memberTaskList.members.first().items?.forEach { queueAssignedTask.offer(TaskAssignment(Member(Prefs.userCF),it)) }
                updateTheCurrentTask()
            }

            when (payloadWrapper.subject) {
                WSOperations.NOTIFY -> notifyHandling()
                WSOperations.UPDATE -> manageUpdate()
                WSOperations.ADD_TASK -> newTaskAssigned()
                WSOperations.MEMBER_COMEBACK_RESPONSE -> loadMemberTasks()
                else -> null
            }
        }
    }

    private fun updateTheCurrentTask() {
        try {
            currentAssignedTask = queueAssignedTask.remove()
        } catch (ex: NoSuchElementException) {
            currentAssignedTask = null
        }
        currentAssignedTask?. run {
            view?.showNewTask(currentAssignedTask!!.task)
            NotifierWSAdapter.sendSubscribeToParametersMessage(currentAssignedTask!!.task.linkedParameters)
        } ?: run {
            view?.showEmptyTask()
        }
    }
}