package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.activitySelection

import android.os.Bundle
import android.view.View
import com.crioprecipitati.androidpervasive1718.R
import com.crioprecipitati.androidpervasive1718.model.Activites
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseActivity
import kotlinx.android.synthetic.main.activity_activity_selection.*

class ActivitySelectionActivity : BaseActivity<ActivitySelectionContract.ActivitySelectionView, ActivitySelectionContract.ActivitySelectionPresenter>(), ActivitySelectionContract.ActivitySelectionView {

    override var presenter: ActivitySelectionContract.ActivitySelectionPresenter = ActivitySelectionPresenterImpl()
    override val layout: Int = R.layout.activity_activity_selection
    private lateinit var itemOnClick: (View, Int, Int) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        itemOnClick = { _ , position , _ -> presenter.onActivitySelected(position)}

        btnDrugs.setOnClickListener{presenter.onActivityTypeSelected(Activites.DRUGS.id)}
        btnManeuvers.setOnClickListener{presenter.onActivityTypeSelected(Activites.MANEUVERS.id)}

    }

    override fun startLoadingState() {
        runOnUiThread {
            pbActivitiesSpinner.visibility = View.VISIBLE
        }
    }

    override fun stopLoadingState() {
        runOnUiThread {
            pbActivitiesSpinner.visibility = View.GONE
        }
    }

    override fun showActivityByActivityType() {
        runOnUiThread {
            with(rvActivityList) {
                adapter = null
                layoutManager = null
                layoutManager = android.support.v7.widget.LinearLayoutManager(this@ActivitySelectionActivity, android.widget.LinearLayout.VERTICAL, false)
                adapter = ActivityListAdapter(presenter.activityList, itemOnClick)
                adapter.notifyDataSetChanged()
            }

        }
    }
}