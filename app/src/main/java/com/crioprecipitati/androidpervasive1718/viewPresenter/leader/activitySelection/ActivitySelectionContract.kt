package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.activitySelection

import com.crioprecipitati.androidpervasive1718.base.BasePresenter
import com.crioprecipitati.androidpervasive1718.base.BaseView
import com.crioprecipitati.androidpervasive1718.model.Activity

enum class ActivityTypes {
    DRUGS, MANOEUVRES, DIAGNOSTICS
}

interface ActivitySelectionContract {

    interface ActivitySelectionView : BaseView {

        fun showActivityByActivityType(activityTypes: ActivityTypes)

    }

    interface ActivitySelectionPresenter : BasePresenter<ActivitySelectionView> {

        var activityList: List<Activity>
        var view: ActivitySelectionContract.ActivitySelectionView

        fun onActivityTypeSelected(activityTypes: ActivityTypes)

        fun onActivitySelected()

        fun getActivityByActivityType()
    }

}