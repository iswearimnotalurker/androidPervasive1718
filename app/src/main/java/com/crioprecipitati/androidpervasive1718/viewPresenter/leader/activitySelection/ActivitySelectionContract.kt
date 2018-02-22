package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.activitySelection

import com.crioprecipitati.androidpervasive1718.base.BasePresenter
import com.crioprecipitati.androidpervasive1718.base.BaseView
import com.crioprecipitati.androidpervasive1718.model.Activity
import com.crioprecipitati.androidpervasive1718.model.Member


interface ActivitySelectionContract {

    interface ActivitySelectionView : BaseView {

        fun showActivityByActivityType(activityList: List<Activity>)

    }

    interface ActivitySelectionPresenter : BasePresenter<ActivitySelectionView> {

        var activityList: List<Activity>

        fun onActivityTypeSelected(activityTypeId: Int)

        fun onActivitySelected(currentMember: Member)

        fun getActivityByActivityType()
    }

}