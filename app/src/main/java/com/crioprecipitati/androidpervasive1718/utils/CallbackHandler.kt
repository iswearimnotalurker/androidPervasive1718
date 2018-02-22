package com.crioprecipitati.androidpervasive1718.utils

import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.networking.webSockets.WSCallbacks
import com.crioprecipitati.androidpervasive1718.viewPresenter.leader.activitySelection.ActivitySelectionContract
import com.crioprecipitati.androidpervasive1718.viewPresenter.leader.activitySelection.ActivitySelectionPresenterImpl
import com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring.TeamMonitoringContract
import com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring.TeamMonitoringPresenterImpl
import model.*

/**
 * Created by Famiglia Antonini on 22/02/2018.
 */
class CallbackHandler:WSCallbacks {

    val teamMonitoringPresenter : TeamMonitoringContract.TeamMonitoringPresenter = TeamMonitoringPresenterImpl
    val activitySelectionPresenter: ActivitySelectionContract.ActivitySelectionPresenter = ActivitySelectionPresenterImpl
    override fun onMessageReceived(messageString: String?) {

        val messageWrapper: PayloadWrapper = GsonInitializer.fromJson(messageString!!,PayloadWrapper::class.java)
        messageWrapper?.let {
            with(messageWrapper) {

                fun taskAssignementHanding(){
                    val taskAssignment: TaskAssignment = it.objectify(body)
                    teamMonitoringPresenter.view.showAndUpdateTaskList(taskAssignment.member,taskAssignment.task)
                }

                fun taskErrorHandling(){
                    val taskError: TaskError = it.objectify(body)
                    teamMonitoringPresenter.view.showError(taskError.error)
                }

                fun updateHandling(){
                    val update: Update = it.objectify(body)
                    teamMonitoringPresenter.view.showAndUpdateHealthParameters(update.lifeParameter,update.value)
                }

                fun memberAdditionHandling(){

                    val membersAddition: MembersAdditionNotification = it.objectify(body)
                    teamMonitoringPresenter.view.showAndUpdateMemberList(membersAddition.members)
                }

                fun activityAdditionHandling(){
                    val activityAddition: ActivityAdditionNotification = it.objectify(body)
                    activitySelectionPresenter.activityList = activityAddition.activities
                }

                when (subject) {
                    WSOperations.ADD_MEMBER -> memberAdditionHandling()
                    WSOperations.ADD_TASK -> taskAssignementHanding()
                    WSOperations.CHANGE_TASK_STATUS -> taskAssignementHanding()
                    WSOperations.REMOVE_TASK -> taskAssignementHanding()
                    WSOperations.ERROR_CHANGING_STATUS -> taskErrorHandling()
                    WSOperations.ERROR_REMOVING_TASK -> taskErrorHandling()
                    WSOperations.UPDATE -> updateHandling()
                    WSOperations.SET_ALL_ACTIVITIES -> activityAdditionHandling()
                    W
                    }
                }
                }
    }


}