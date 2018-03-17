package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.activitySelection

import com.crioprecipitati.androidpervasive1718.model.Activity
import com.crioprecipitati.androidpervasive1718.networking.webSockets.TaskWSAdapter
import com.crioprecipitati.androidpervasive1718.utils.CallbackHandler
import com.crioprecipitati.androidpervasive1718.utils.WSObserver
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BasePresenterImpl
import model.ActivityAdditionNotification
import model.PayloadWrapper
import model.WSOperations
import model.objectify

class ActivitySelectionPresenterImpl : BasePresenterImpl<ActivitySelectionContract.ActivitySelectionView>(), ActivitySelectionContract.ActivitySelectionPresenter, WSObserver {

    override var activityList: MutableList<Activity> = mutableListOf()

    private val channels = listOf(WSOperations.SET_ALL_ACTIVITIES)

    override fun attachView(view: ActivitySelectionContract.ActivitySelectionView) {
        super.attachView(view)
        CallbackHandler.attach(channels, this)
        TaskWSAdapter.sendGetActivitiesRequest()
        view.startLoadingState()
    }

    override fun detachView() {
        super.detachView()
        CallbackHandler.detach(channels, this)
    }

    override fun onActivitySelected(activityId: Int) {
        view?.startTeamMonitoringActivity(activityList.first { it.id == activityId })
    }

    override fun update(payloadWrapper: PayloadWrapper) {
        with(payloadWrapper) {
            fun activityAdditionHandling() {
                val activityAddition: ActivityAdditionNotification = this.objectify(body)
                activityList.clear()
                activityList.addAll(activityAddition.activities.toMutableList())
                view?.showActivitiesList()
                view?.stopLoadingState()
            }

            when (payloadWrapper.subject) {
                WSOperations.SET_ALL_ACTIVITIES -> activityAdditionHandling()
                else -> null
            }
        }
    }
}