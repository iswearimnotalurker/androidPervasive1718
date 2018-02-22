package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.activitySelection

import com.crioprecipitati.androidpervasive1718.base.BasePresenter
import com.crioprecipitati.androidpervasive1718.model.Activity
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.model.Task
import com.crioprecipitati.androidpervasive1718.networking.webSockets.TaskWSAdapter
import com.crioprecipitati.androidpervasive1718.utils.toJson
import model.MembersAdditionNotification
import model.PayloadWrapper
import model.TaskAssignment
import model.WSOperations

object ActivitySelectionPresenterImpl : BasePresenter<ActivitySelectionContract.ActivitySelectionView>, ActivitySelectionContract.ActivitySelectionPresenter {

    override var activityList: List<Activity> = listOf()
    override lateinit var view: ActivitySelectionContract.ActivitySelectionView
    private val webSocketHelper: TaskWSAdapter = TaskWSAdapter

    override fun attachView(view: ActivitySelectionContract.ActivitySelectionView) {
        this.view=view
    }

    override fun detachView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivityTypeSelected(activityTypes: ActivityTypes) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onActivitySelected(member: Member) {
        webSocketHelper.webSocket.send(PayloadWrapper(0,WSOperations.ADD_TASK,TaskAssignment(member, Task.emptyTask()).toJson()).toJson())
    }

    override fun getActivityByActivityType() {
        var members: List<Member> = listOf(Member(1,"Leader"))
        webSocketHelper.webSocket.send(PayloadWrapper(0,WSOperations.GET_ALL_ACTIVITIES,MembersAdditionNotification(members).toJson()).toJson())
    }
}