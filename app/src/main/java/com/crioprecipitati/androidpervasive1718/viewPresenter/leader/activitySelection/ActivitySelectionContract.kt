package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.activitySelection

import com.crioprecipitati.androidpervasive1718.model.Activity
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BasePresenter
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseView


interface ActivitySelectionContract {

    interface ActivitySelectionView : BaseView {

        fun showActivitiesList()

        fun startTeamMonitoringActivity(activity: Activity)

    }

    interface ActivitySelectionPresenter : BasePresenter<ActivitySelectionView> {

        var activityList: MutableList<Activity>

        fun onActivitySelected(activityId: Int)
    }

}