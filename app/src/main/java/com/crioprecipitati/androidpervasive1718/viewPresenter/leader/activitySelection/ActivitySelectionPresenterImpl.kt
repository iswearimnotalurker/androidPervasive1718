package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.activitySelection

import com.crioprecipitati.androidpervasive1718.base.BasePresenter
import com.crioprecipitati.androidpervasive1718.base.BasePresenterImpl
import com.crioprecipitati.androidpervasive1718.model.Activity
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.model.Task
import com.crioprecipitati.androidpervasive1718.networking.webSockets.NotifierWSAdapter
import com.crioprecipitati.androidpervasive1718.networking.webSockets.TaskWSAdapter
import com.crioprecipitati.androidpervasive1718.utils.toJson
import com.crioprecipitati.androidpervasive1718.viewPresenter.login.LoginContract
import com.crioprecipitati.androidpervasive1718.viewPresenter.login.LoginPresenterImpl
import model.MembersAdditionNotification
import model.PayloadWrapper
import model.TaskAssignment
import model.WSOperations

object ActivitySelectionPresenterImpl : BasePresenterImpl<ActivitySelectionContract.ActivitySelectionView>(), ActivitySelectionContract.ActivitySelectionPresenter {

    override var activityList: List<Activity> = listOf()
    private val taskWebSocketHelper: TaskWSAdapter = TaskWSAdapter
    private val notifierWebSocket: NotifierWSAdapter = NotifierWSAdapter
    private val loginPresenter: LoginContract.LoginPresenter = LoginPresenterImpl
    override var view: ActivitySelectionContract.ActivitySelectionView? = null

    override fun onActivityTypeSelected(activityTypeId: Int) {
        val selectedActivities: List<Activity> = activityList.filter { it.activityTypeId == activityTypeId }
        view?.showActivityByActivityType(selectedActivities)

    }

    override fun onActivitySelected(member: Member) {
        taskWebSocketHelper.webSocket.send(PayloadWrapper(loginPresenter.sessionId,WSOperations.ADD_TASK,TaskAssignment(member, Task.emptyTask()).toJson()).toJson())
        notifierWebSocket.webSocket.send(PayloadWrapper(loginPresenter.sessionId,WSOperations.SUBSCRIBE,member.toJson()).toJson())

    }

    override fun getActivityByActivityType() {
        var members: List<Member> = listOf(Member(1,"Leader"))
        taskWebSocketHelper.webSocket.send(PayloadWrapper(loginPresenter.sessionId,WSOperations.GET_ALL_ACTIVITIES,MembersAdditionNotification(members).toJson()).toJson())
    }
}