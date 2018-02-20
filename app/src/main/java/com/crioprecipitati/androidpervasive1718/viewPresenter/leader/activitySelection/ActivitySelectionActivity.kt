package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.activitySelection

import com.crioprecipitati.androidpervasive1718.base.BaseActivity

class ActivitySelectionActivity : BaseActivity<ActivitySelectionContract.ActivitySelectionView, ActivitySelectionContract.ActivitySelectionPresenter>(), ActivitySelectionContract.ActivitySelectionView {

    override var presenter: ActivitySelectionContract.ActivitySelectionPresenter = ActivitySelectionPresenterImpl()

    override fun showActivityByActivityType(activityTypes: ActivityTypes) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}