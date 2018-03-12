package com.crioprecipitati.androidpervasive1718.viewPresenter.member.taskMonitoring

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
import trikita.log.Log
import java.sql.Timestamp
import java.util.*
import kotlin.Comparator

open class TaskMonitoringPresenterImpl : BasePresenterImpl<TaskMonitoringContract.TaskMonitoringView>(), TaskMonitoringContract.TaskMonitoringPresenter, WSObserver {

    private val channels = listOf(WSOperations.NOTIFY,
        WSOperations.UPDATE,
        WSOperations.MEMBER_COMEBACK_RESPONSE,
        WSOperations.REMOVE_TASK,
        WSOperations.ADD_TASK,
        WSOperations.ANSWER)

    private val queueAssignedTask = PriorityQueue<TaskAssignment>(10, Comparator<TaskAssignment> { x, y ->
        x.augmentedTask.task.startTime.compareTo(y.augmentedTask.task.startTime)
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
            this.augmentedTask.task.statusId = Status.FINISHED.id
            this.augmentedTask.task.endTime = Timestamp(Date().time)
            TaskWSAdapter.send(PayloadWrapper(Prefs.sessionId, WSOperations.CHANGE_TASK_STATUS, this.toJson()).toJson())
            NotifierWSAdapter.send(PayloadWrapper(Prefs.sessionId, WSOperations.CLOSE, Member(Prefs.userCF).toJson()).toJson())
            removeAndUpdateCurrentTask()
        }
    }

    override fun update(payloadWrapper: PayloadWrapper) {
        with(payloadWrapper) {
            fun notifyHandling() {
                val notification: Notification = this.objectify(body)
                view?.showAlarmedTask(notification)
            }

            fun manageUpdate() {
                val update: Update = this.objectify(body)
                view?.updateHealthParameterValues(update.lifeParameter, update.value)
            }

            fun newTaskAssigned() {
                val task: TaskAssignment = this.objectify(body)
                queueAssignedTask.offer(task)
                currentAssignedTask ?: run {
                    removeAndUpdateCurrentTask()
                }
            }

            fun removeTask(passedTaskAssignment: TaskAssignment? = null) {
                val taskAssignment: TaskAssignment = passedTaskAssignment ?: this.objectify(body)
                currentAssignedTask?.let {
                    if (it.augmentedTask.task.name == taskAssignment.augmentedTask.task.name) removeAndUpdateCurrentTask()
                    else removeSpecificTask(taskAssignment)
                }
            }

            fun changeTaskStatusHandling() {
                val taskAssignment: TaskAssignment = this.objectify(body)
                if (taskAssignment.augmentedTask.task.statusId == Status.FINISHED.id ||
                    taskAssignment.augmentedTask.task.statusId == Status.ELIMINATED.id) {
                    removeTask(taskAssignment)
                }
            }

            fun loadMemberTasks() {
                val members: AugmentedMembersAdditionNotification = this.objectify(body)
                members.members.first().items?.forEach { queueAssignedTask.offer(TaskAssignment(Member(Prefs.userCF), it)) }
                this@TaskMonitoringPresenterImpl.removeAndUpdateCurrentTask()
            }

            when (payloadWrapper.subject) {
                WSOperations.NOTIFY -> notifyHandling()
                WSOperations.REMOVE_TASK -> removeTask()
                WSOperations.CHANGE_TASK_STATUS -> changeTaskStatusHandling()
                WSOperations.UPDATE -> manageUpdate()
                WSOperations.ADD_TASK -> newTaskAssigned()
                WSOperations.MEMBER_COMEBACK_RESPONSE -> loadMemberTasks()
                else -> null
            }
        }
    }

    private fun removeAndUpdateCurrentTask() {
        try {
            currentAssignedTask = queueAssignedTask.remove()
            updateViewAfterCurrentTaskUpdate()
        } catch (ex: NoSuchElementException) {
            currentAssignedTask = null
        }
    }

    private fun removeSpecificTask(taskAssignment: TaskAssignment) {
        try {
            queueAssignedTask.remove(taskAssignment)
        } catch (ex: NoSuchElementException) {
            Log.d("removeSpecificTask: no such task found: $taskAssignment")
        }
    }

    private fun updateViewAfterCurrentTaskUpdate() {
        currentAssignedTask?.run {
            view?.showNewTask(currentAssignedTask!!.augmentedTask)
            NotifierWSAdapter.sendSubscribeToParametersMessage(currentAssignedTask!!.augmentedTask.linkedParameters)
        } ?: run {
            view?.showEmptyTask()
        }
    }
}