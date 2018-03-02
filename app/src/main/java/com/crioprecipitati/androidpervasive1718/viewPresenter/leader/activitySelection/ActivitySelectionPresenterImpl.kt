package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.activitySelection

import android.content.Intent
import com.crioprecipitati.androidpervasive1718.model.Activity
import com.crioprecipitati.androidpervasive1718.model.AugmentedTask
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.networking.webSockets.NotifierWSAdapter
import com.crioprecipitati.androidpervasive1718.networking.webSockets.TaskWSAdapter
import com.crioprecipitati.androidpervasive1718.utils.CallbackHandler
import com.crioprecipitati.androidpervasive1718.utils.Prefs
import com.crioprecipitati.androidpervasive1718.utils.WSObserver
import com.crioprecipitati.androidpervasive1718.utils.toJson
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BasePresenterImpl
import model.*

class ActivitySelectionPresenterImpl : BasePresenterImpl<ActivitySelectionContract.ActivitySelectionView>(), ActivitySelectionContract.ActivitySelectionPresenter, WSObserver {

    override var activityList: MutableList<Activity> = mutableListOf()

    private val channels = listOf(WSOperations.SET_ALL_ACTIVITIES)

    override fun attachView(view: ActivitySelectionContract.ActivitySelectionView) {
        super.attachView(view)
        CallbackHandler.attach(channels, this)
    }

    override fun detachView() {
        super.detachView()
        CallbackHandler.detach(channels, this)
    }

    override fun onActivityTypeSelected(activityTypeId: Int) {
        TaskWSAdapter.sendGetActivitiesRequest(activityTypeId)
        view?.startLoadingState()
    }



    override fun onActivitySelected(activityIndex: Int) {
        view?.startTeamMonitoringActivity(activityList[activityIndex])
    }

    override fun update(payloadWrapper: PayloadWrapper) {
        with(payloadWrapper) {
            fun activityAdditionHandling() {
                val activityAddition: ActivityAdditionNotification = this.objectify(body)
                activityList = activityAddition.activities.toMutableList()
                view?.showActivityByActivityType()
                view?.stopLoadingState()
            }

            when (payloadWrapper.subject) {
                WSOperations.SET_ALL_ACTIVITIES -> activityAdditionHandling()
                else -> null
            }
        }
    }
}