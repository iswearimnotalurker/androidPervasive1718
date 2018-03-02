package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import com.crioprecipitati.androidpervasive1718.model.AugmentedTask
import com.crioprecipitati.androidpervasive1718.model.Member
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

class TeamMonitoringPresenterImpl : BasePresenterImpl<TeamMonitoringContract.TeamMonitoringView>(), TeamMonitoringContract.TeamMonitoringPresenter, WSObserver {

    override var member: Member? = null
    override val memberList: MutableList<Member> = mutableListOf()

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
    }

    override fun detachView() {
        super.detachView()
        CallbackHandler.detach(channels, this)
        TaskWSAdapter.closeWS()
        NotifierWSAdapter.send(PayloadWrapper(Prefs.sessionId, WSOperations.CLOSE, Member(Prefs.userCF).toJson()).toJson())
        NotifierWSAdapter.closeWS()
    }

    override fun onMemberSelected(userIndex: Int) {
        Log.d("onMemberSelected: ${memberList[userIndex]}")
        view?.showActivitySelectionActivity()
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

    override fun update(payloadWrapper: PayloadWrapper) {
        with(payloadWrapper) {

            fun healthParameterUpdateHandling() {
                val update: Update = this.objectify(body)
                view?.showAndUpdateHealthParameters(update.lifeParameter, update.value)
            }

            fun memberAdditionHandling() {
                val membersAddition: MembersAdditionNotification = this.objectify(body)
                view?.showAndUpdateMemberList()
            }

            fun memberListAdditionHandling() {
                val membersAddition: MembersAdditionNotification = this.objectify(body)
                with(memberList) {
                    clear()
                    addAll(membersAddition.members)
                }
                view?.showAndUpdateMemberList()
            }

            fun taskAssignmentHandling() {
                val taskAssignment: TaskAssignment = this.objectify(body)
                view?.showAndUpdateTaskList(taskAssignment.member, taskAssignment.augmentedTask)
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
