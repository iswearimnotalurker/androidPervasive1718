package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.activitySelection

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.crioprecipitati.androidpervasive1718.R
import com.crioprecipitati.androidpervasive1718.model.Activites
import com.crioprecipitati.androidpervasive1718.model.Activity
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.utils.BundleStrings
import com.crioprecipitati.androidpervasive1718.utils.Unbundler
import com.crioprecipitati.androidpervasive1718.utils.generateBundle
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseActivity
import kotlinx.android.synthetic.main.activity_activity_selection.*

class ActivitySelectionActivity : BaseActivity<ActivitySelectionContract.ActivitySelectionView, ActivitySelectionContract.ActivitySelectionPresenter>(), ActivitySelectionContract.ActivitySelectionView {

    override var presenter: ActivitySelectionContract.ActivitySelectionPresenter = ActivitySelectionPresenterImpl()
    override val layout: Int = R.layout.activity_activity_selection
    private lateinit var activityOnClick: (Int) -> Unit
    private lateinit var activitiesOnClick: (Int) -> Unit
    private lateinit var member: Member

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityOnClick = { activityId -> presenter.onActivitySelected(activityId) }

        member = Unbundler.extractMember(intent.getBundleExtra(BundleStrings.memberExtraString))

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

    override fun showActivitiesList() {
        runOnUiThread {
            fun renderList(rv: RecyclerView, list: List<Activity>) {
                with(rv) {
                    adapter = null
                    layoutManager = null
                    layoutManager = android.support.v7.widget.LinearLayoutManager(this@ActivitySelectionActivity, android.widget.LinearLayout.VERTICAL, false)
                    adapter = ActivityListAdapter(list, activityOnClick)
                    adapter.notifyDataSetChanged()
                }
            }

            renderList(rvDrugsList, presenter.activityList.filter { it.activityTypeId == Activites.DRUGS.id })
            renderList(rvManouversList, presenter.activityList.filter { it.activityTypeId == Activites.MANEUVERS.id })

        }
    }

    override fun startTeamMonitoringActivity(activity: Activity) {
        val resultIntent = Intent()
        resultIntent.putExtra(BundleStrings.memberExtraString,member.generateBundle())
        resultIntent.putExtra(BundleStrings.activityExtraString,activity.generateBundle())
        setResult(android.app.Activity.RESULT_OK,resultIntent)
        finish()
    }
}