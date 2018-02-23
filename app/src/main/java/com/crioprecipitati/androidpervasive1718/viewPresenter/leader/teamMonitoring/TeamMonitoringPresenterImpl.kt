package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import com.crioprecipitati.androidpervasive1718.base.BasePresenterImpl
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.model.Task
import com.crioprecipitati.androidpervasive1718.networking.RestApiManager
import com.crioprecipitati.androidpervasive1718.networking.api.SessionApi
import com.crioprecipitati.androidpervasive1718.networking.webSockets.TaskWSAdapter
import com.crioprecipitati.androidpervasive1718.utils.CallbackHandler
import com.crioprecipitati.androidpervasive1718.utils.Prefs
import com.crioprecipitati.androidpervasive1718.utils.WSObserver
import com.crioprecipitati.androidpervasive1718.utils.toJson
import io.reactivex.android.schedulers.AndroidSchedulers
import model.*

class TeamMonitoringPresenterImpl : BasePresenterImpl<TeamMonitoringContract.TeamMonitoringView>(), TeamMonitoringContract.TeamMonitoringPresenter, WSObserver {

    override var member: Member? = null

    private val channels = listOf(
        WSOperations.LIST_MEMBERS,
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
    }

    override fun detachView() {
        super.detachView()
        CallbackHandler.detach(channels, this)
    }

    override fun onTaskDeleted() {
        TaskWSAdapter.send(PayloadWrapper(Prefs.sessionId, WSOperations.REMOVE_TASK, TaskAssignment(Member.defaultMember(), Task.defaultTask()).toJson()).toJson())
    }

    override fun onMemberSelected() {
        member?.also { view?.showActivitySelectionActivity(it) }
    }

    //per fine intervento
    override fun onSessionClosed(sessionId: Int) {

        RestApiManager
            .createService(SessionApi::class.java)
            .closeSessionBySessionId(sessionId)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { message -> println(message) },
                { e -> println(e.message) }
            )
    }

    override fun update(payloadWrapper: PayloadWrapper) {
        with(payloadWrapper) {

            fun taskAssignmentHandling() {
                val taskAssignment: TaskAssignment = this.objectify(body)
                view?.showAndUpdateTaskList(taskAssignment.member, taskAssignment.task)
            }

            fun taskErrorHandling() {
                val taskError: TaskError = this.objectify(body)
                view?.showError(taskError.error)
            }

            fun updateHandling() {
                val update: Update = this.objectify(body)
                view?.showAndUpdateHealthParameters(update.lifeParameter, update.value)
            }

            fun memberAdditionHandling() {
                val membersAddition: MembersAdditionNotification = this.objectify(body)
                view?.showAndUpdateMemberList(membersAddition.members)
            }

            when (subject) {
                WSOperations.LIST_MEMBERS -> memberAdditionHandling() // teamMonitoring
                WSOperations.ADD_MEMBER -> memberAdditionHandling() // teamMonitoring
                WSOperations.ADD_TASK -> taskAssignmentHandling() // teamMonitoring
                WSOperations.CHANGE_TASK_STATUS -> taskAssignmentHandling()
                WSOperations.REMOVE_TASK -> taskAssignmentHandling()
                WSOperations.ERROR_CHANGING_STATUS -> taskErrorHandling()
                WSOperations.ERROR_REMOVING_TASK -> taskErrorHandling()
                WSOperations.UPDATE -> updateHandling()
                else -> null
            }
        }
    }

}
