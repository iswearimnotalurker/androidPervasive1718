package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import com.crioprecipitati.androidpervasive1718.model.*
import com.crioprecipitati.androidpervasive1718.networking.RestApiManager
import com.crioprecipitati.androidpervasive1718.networking.api.SessionApi
import com.crioprecipitati.androidpervasive1718.networking.webSockets.NotifierWSAdapter
import com.crioprecipitati.androidpervasive1718.networking.webSockets.TaskWSAdapter
import com.crioprecipitati.androidpervasive1718.utils.CallbackHandler
import com.crioprecipitati.androidpervasive1718.utils.Prefs
import com.crioprecipitati.androidpervasive1718.utils.WSObserver
import com.crioprecipitati.androidpervasive1718.utils.toJson
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BasePresenterImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import model.*
import model.Status
import trikita.log.Log
import utils.newTask

class TeamMonitoringPresenterImpl : BasePresenterImpl<TeamMonitoringContract.TeamMonitoringView>(), TeamMonitoringContract.TeamMonitoringPresenter, WSObserver {

    data class SelectedTask(val memberIndex: Int, val taskIndex: Int)

    override var member: Member? = null
    override var memberList: MutableList<AugmentedMember> = mutableListOf()

    private lateinit var selectedTask: SelectedTask

    private val channels = listOf(
        WSOperations.LIST_MEMBERS_RESPONSE,
        WSOperations.ADD_MEMBER_NOTIFICATION,
        WSOperations.ADD_MEMBER,
        WSOperations.ADD_TASK,
        WSOperations.CHANGE_TASK_STATUS,
        WSOperations.REMOVE_TASK,
        WSOperations.ERROR_CHANGING_STATUS,
        WSOperations.ERROR_REMOVING_TASK,
        WSOperations.UPDATE,
        WSOperations.NOTIFY)

    override fun attachView(view: TeamMonitoringContract.TeamMonitoringView) {
        super.attachView(view)
        CallbackHandler.attach(channels, this)
        NotifierWSAdapter.sendSubscribeToAllParametersMessage()
        TaskWSAdapter.sendAllMembersRequest()
        view.startLoadingState()
    }

    override fun detachView() {
        super.detachView()
        CallbackHandler.detach(channels, this)
        TaskWSAdapter.closeWS()
        NotifierWSAdapter.send(PayloadWrapper(Prefs.sessionId, WSOperations.CLOSE, Member(Prefs.userCF).toJson()).toJson())
        NotifierWSAdapter.closeWS()
    }

    override fun onTaskAdditionRequested(member: Member, activity: Activity) {

        val assignment = TaskAssignment(member, AugmentedTask(
            Task.Companion.newTask(Prefs.sessionId, activity.id, member.userCF),
            LifeParameters.values().filter { activity.healthParameterIds.contains(it.id) },
            activity.name
        ))

        val message = PayloadWrapper(
            Prefs.instanceId,
            WSOperations.ADD_TASK,
            assignment.toJson()
        )
        TaskWSAdapter.send(message.toJson())
    }

    private fun getSelectedTaskMember() =
        Member(this.memberList[selectedTask.memberIndex].member.userCF)

    private fun getSelectedTask() =
        this.memberList[selectedTask.memberIndex].items[selectedTask.taskIndex]


    override fun onTaskCompletionRequested() {
        TaskWSAdapter.send(PayloadWrapper(Prefs.sessionId, WSOperations.CHANGE_TASK_STATUS, TaskAssignment(getSelectedTaskMember(), getSelectedTask().apply { this.task.statusId = Status.FINISHED.id }).toJson()).toJson())
        view?.startLoadingState()
    }

    override fun onTaskDeletionRequested() {
        TaskWSAdapter.send(PayloadWrapper(Prefs.sessionId, WSOperations.REMOVE_TASK, TaskAssignment(getSelectedTaskMember(), getSelectedTask()).toJson()).toJson())
        view?.startLoadingState()
    }

    override fun onMemberSelected(memberIndex: Int) {
        view?.showActivitySelectionActivity(this.memberList[memberIndex].member.userCF)
    }

    override fun onTaskSelected(memberIndex: Int, taskIndex: Int) {
        selectedTask = SelectedTask(memberIndex, taskIndex)
        view?.showTaskDialog()
    }

    override fun onSessionCloseRequested() {
        RestApiManager
            .createService(SessionApi::class.java)
            .closeSessionBySessionId(Prefs.sessionId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    Log.d(it)
                    view?.onSessionClosed()
                },
                { Log.d(it.message) }
            )
    }

    override fun update(payloadWrapper: PayloadWrapper) {
        with(payloadWrapper) {

            fun healthParameterUpdateHandling() {
                val update: Update = this.objectify(body)
                view?.showAndUpdateHealthParameters(update.lifeParameter, update.value)
            }

            fun memberAdditionHandling() {
                val newMember: MemberWithNameSurname = this.objectify(body)
                memberList.add(AugmentedMember(newMember))
                view?.showAndUpdateMemberAndTaskList()
                view?.stopLoadingState()
            }

            fun memberListAdditionHandling() {
                val newMembers: AugmentedMembersAdditionNotification = this.objectify(body)
                with(memberList) {
                    clear()
                    if (newMembers.members.isNotEmpty()) newMembers.members.forEach {
                        this.add(AugmentedMember(it.member, it.items
                                ?: mutableListOf()))
                    }
                }
                view?.showAndUpdateMemberAndTaskList()
                view?.stopLoadingState()
            }

            fun taskAssignmentHandling() {
                val taskAssignment: TaskAssignment = this.objectify(body)
                if (taskAssignment.augmentedTask.task.statusId == Status.FINISHED.id ||
                    taskAssignment.augmentedTask.task.statusId == Status.ELIMINATED.id) {
                    val items = memberList.firstOrNull { it.member.userCF == taskAssignment.member.userCF }?.items
                    items?.remove(items.firstOrNull { it.task.name == taskAssignment.augmentedTask.task.name })
                }else {
                    memberList.firstOrNull { it.member.userCF == taskAssignment.member.userCF }?.items?.add(taskAssignment.augmentedTask)
                }
                view?.stopLoadingState()
                view?.showAndUpdateMemberAndTaskList()
            }

            fun taskErrorHandling() {
                val taskError: TaskError = this.objectify(body)
                view?.showError(taskError.error)
            }

            fun notifyHandling() {
                val notification: Notification = this.objectify(body)
                view?.showAlarmedTask(notification)
            }

            when (subject) {
                WSOperations.NOTIFY -> notifyHandling()
                WSOperations.UPDATE -> healthParameterUpdateHandling()
                WSOperations.LIST_MEMBERS_RESPONSE -> memberListAdditionHandling()
                WSOperations.ADD_MEMBER_NOTIFICATION -> memberAdditionHandling()
                WSOperations.ADD_TASK -> taskAssignmentHandling()
                WSOperations.CHANGE_TASK_STATUS -> taskAssignmentHandling()
                WSOperations.REMOVE_TASK -> taskAssignmentHandling()
                WSOperations.ERROR_CHANGING_STATUS -> taskErrorHandling()
                WSOperations.ERROR_REMOVING_TASK -> taskErrorHandling()
                else -> null
            }
        }

    }

    override fun onWSRefreshRequested() {
        TaskWSAdapter.changeAddress()
        NotifierWSAdapter.changeAddress()
    }

}