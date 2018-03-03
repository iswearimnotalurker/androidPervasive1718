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
import trikita.log.Log
import java.sql.Timestamp
import java.util.*

class TeamMonitoringPresenterImpl : BasePresenterImpl<TeamMonitoringContract.TeamMonitoringView>(), TeamMonitoringContract.TeamMonitoringPresenter, WSObserver {

    override var member: Member? = null
    override val memberList: MutableList<AugmentedMember> = mutableListOf()

    private val channels = listOf(
        WSOperations.LIST_MEMBERS_RESPONSE,
        WSOperations.ADD_MEMBER,
        WSOperations.ADD_TASK,
        WSOperations.CHANGE_TASK_STATUS,
        WSOperations.REMOVE_TASK,
        WSOperations.ERROR_CHANGING_STATUS,
        WSOperations.ERROR_REMOVING_TASK,
        WSOperations.UPDATE)

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

    override fun onMemberSelected(userIndex: Int) {
        view?.showActivitySelectionActivity(this.memberList.get(userIndex).userCF)
    }

    override fun onTaskDeleted() {
        TaskWSAdapter.send(PayloadWrapper(Prefs.sessionId, WSOperations.REMOVE_TASK, TaskAssignment(Member.defaultMember(), AugmentedTask.defaultAugmentedTask()).toJson()).toJson())
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

    override fun addTask(member: Member, activity : Activity) {

        val assignment = TaskAssignment(member, AugmentedTask(
                    Task(0,
                            Prefs.sessionId,
                            member.userCF,
                            Timestamp(Date().time),
                            Timestamp(Date().time + 1000),
                            activity.id,
                            Status.RUNNING.id),
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

    override fun update(payloadWrapper: PayloadWrapper) {
        with(payloadWrapper) {

            fun healthParameterUpdateHandling() {
                val update: Update = this.objectify(body)
                view?.showAndUpdateHealthParameters(update.lifeParameter, update.value)
            }

            fun memberAdditionHandling() {
                val membersAddition: MembersAdditionNotification = this.objectify(body)
                if (membersAddition.members.isNotEmpty()) memberList.add(AugmentedMember(membersAddition.members.first().userCF))
                view?.showAndUpdateMemberAndTaskList()
                view?.stopLoadingState()
            }

            fun memberListAdditionHandling() {
                val membersAddition: AugmentedMembersAdditionNotification = this.objectify(body)
                with(memberList) {
                    clear()
                    if (membersAddition.members.isNotEmpty()) membersAddition.members.forEach {
                        this.add(AugmentedMember(it.userCF, it.items
                                ?: mutableListOf<AugmentedTask>()))
                    }
                }
                view?.showAndUpdateMemberAndTaskList()
                view?.stopLoadingState()
            }

            fun taskAssignmentHandling() {
                val taskAssignment: TaskAssignment = this.objectify(body)
                memberList.firstOrNull {it.userCF == taskAssignment.member.userCF}?.items?.add(taskAssignment.task)
                view?.showAndUpdateMemberAndTaskList()
            }

            fun taskErrorHandling() {
                val taskError: TaskError = this.objectify(body)
                view?.showError(taskError.error)
            }

            when (subject) {
                WSOperations.UPDATE -> healthParameterUpdateHandling()
                WSOperations.LIST_MEMBERS_RESPONSE -> memberListAdditionHandling()
                WSOperations.ADD_MEMBER -> memberAdditionHandling()
                WSOperations.ADD_TASK -> taskAssignmentHandling()
                WSOperations.CHANGE_TASK_STATUS -> taskAssignmentHandling()
                WSOperations.REMOVE_TASK -> taskAssignmentHandling()
                WSOperations.ERROR_CHANGING_STATUS -> taskErrorHandling()
                WSOperations.ERROR_REMOVING_TASK -> taskErrorHandling()
                else -> null
            }
        }
    }

}
