package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.activitySelection

import com.crioprecipitati.androidpervasive1718.model.Activity
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseActivity

class ActivitySelectionActivity : BaseActivity<ActivitySelectionContract.ActivitySelectionView, ActivitySelectionContract.ActivitySelectionPresenter>(), ActivitySelectionContract.ActivitySelectionView {

    override var presenter: ActivitySelectionContract.ActivitySelectionPresenter = ActivitySelectionPresenterImpl()

    override fun showActivityByActivityType(activityList: List<Activity>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}