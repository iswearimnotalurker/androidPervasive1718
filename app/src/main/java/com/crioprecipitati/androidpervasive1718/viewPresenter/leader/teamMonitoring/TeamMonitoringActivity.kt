package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.crioprecipitati.androidpervasive1718.R
import com.crioprecipitati.androidpervasive1718.model.AugmentedTask
import com.crioprecipitati.androidpervasive1718.model.LifeParameters
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.utils.*
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseActivity
import com.crioprecipitati.androidpervasive1718.viewPresenter.leader.activitySelection.ActivitySelectionActivity
import kotlinx.android.synthetic.main.activity_team_monitoring.*
import trikita.log.Log


class TeamMonitoringActivity : BaseActivity<TeamMonitoringContract.TeamMonitoringView, TeamMonitoringContract.TeamMonitoringPresenter>(), TeamMonitoringContract.TeamMonitoringView {

    override var presenter: TeamMonitoringContract.TeamMonitoringPresenter = TeamMonitoringPresenterImpl()
    override val layout: Int = R.layout.activity_team_monitoring
    private lateinit var itemOnClick: (View, Int, Int) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        savedInstanceState?.let { presenter.member = Unbundler.extractMember(it) }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener { onBackPressed() }

        btnCloseSession.setOnClickListener { presenter.onSessionCloseRequested() }

        itemOnClick = { _, position, _ -> presenter.onMemberSelected(position) }

    }

    override fun startLoadingState() {
        runOnUiThread {
            pbTeamSpinner.visibility = View.VISIBLE
        }
    }

    override fun stopLoadingState() {
        runOnUiThread {
            pbTeamSpinner.visibility = View.GONE
        }
    }

    override fun showAndUpdateMemberList() {
        runOnUiThread {
            with(rvMemberList) {
                adapter = null
                layoutManager = null
                layoutManager = android.support.v7.widget.LinearLayoutManager(this@TeamMonitoringActivity, android.widget.LinearLayout.VERTICAL, false)
                adapter = TeamMonitoringAdapter(presenter.memberList, itemOnClick)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun showAndUpdateTaskList(member: Member, task: AugmentedTask) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAndUpdateHealthParameters(lifeParameter: LifeParameters, value: Double) {
        with(value.toString()) {
            when (lifeParameter) {
                LifeParameters.SYSTOLIC_BLOOD_PRESSURE -> tvSYS.setHealthParameterValue(this)
                LifeParameters.DIASTOLIC_BLOOD_PRESSURE -> tvDIA.setHealthParameterValue(this)
                LifeParameters.HEART_RATE -> tvHR.setHealthParameterValue(this)
                LifeParameters.TEMPERATURE -> tvT.setHealthParameterValue(this)
                LifeParameters.OXYGEN_SATURATION -> tvSp02.setHealthParameterValue(this)
                LifeParameters.END_TIDAL_CARBON_DIOXIDE -> tvEtCO2.setHealthParameterValue(this)
            }
        }
    }

    override fun showActivitySelectionActivity() {
        val intent = Intent(this, ActivitySelectionActivity::class.java)
        intent.putExtra(BundleStrings.memberExtraString,Member(Prefs.userCF).generateBundle())
        startActivityForResult(intent, MagicNumbers.activitySelectionActivityLaunchIntentCode)
    }

    override fun onSessionClosed() {
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MagicNumbers.activitySelectionActivityLaunchIntentCode) {
            if (resultCode == Activity.RESULT_OK) {
                val member = Unbundler.extractMember(data!!.getBundleExtra(BundleStrings.memberExtraString))
                val activity = Unbundler.extractActivity(data!!.getBundleExtra(BundleStrings.activityExtraString))
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //TODO
            }
        }
    }
}