package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.activitySelection

import com.crioprecipitati.androidpervasive1718.base.BasePresenter
import com.crioprecipitati.androidpervasive1718.base.BaseView

enum class ActivityTypes {
    DRUGS, MANOEUVRES, DIAGNOSTICS
}

interface ActivitySelectionContract {

    interface ActivitySelectionView : BaseView {

        fun showActivityByActivityType(activityTypes: ActivityTypes)

    }

    interface ActivitySelectionPresenter : BasePresenter<ActivitySelectionView> {

        fun onActivityTypeSelected(activityTypes: ActivityTypes)

        fun onActivitySelected()

    }

}