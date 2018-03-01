package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.activitySelection

import com.crioprecipitati.androidpervasive1718.model.Activity
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BasePresenter
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseView


interface ActivitySelectionContract {

    interface ActivitySelectionView : BaseView {

        fun showActivityByActivityType()

    }

    interface ActivitySelectionPresenter : BasePresenter<ActivitySelectionView> {

        var activityList: MutableList<Activity>

        fun onActivityTypeSelected(activityTypeId: Int)

        fun onActivitySelected(activityIndex: Int)

        fun getActivityByActivityType()
    }

}