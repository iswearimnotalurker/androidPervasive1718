package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import android.content.Intent
import android.os.Bundle
import com.crioprecipitati.androidpervasive1718.R
import com.crioprecipitati.androidpervasive1718.model.AugmentedTask
import com.crioprecipitati.androidpervasive1718.model.LifeParameters
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.utils.generateBundle
import com.crioprecipitati.androidpervasive1718.utils.setHealthParameterValue
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseActivity
import com.crioprecipitati.androidpervasive1718.viewPresenter.leader.activitySelection.ActivitySelectionActivity
import kotlinx.android.synthetic.main.activity_team_monitoring.*


class TeamMonitoringActivity : BaseActivity<TeamMonitoringContract.TeamMonitoringView, TeamMonitoringContract.TeamMonitoringPresenter>(), TeamMonitoringContract.TeamMonitoringView {

    override var presenter: TeamMonitoringContract.TeamMonitoringPresenter = TeamMonitoringPresenterImpl()
    override val layout: Int = R.layout.activity_team_monitoring

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        savedInstanceState?.let { presenter.member = Unbudler.extractMember(it) }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener { onBackPressed() }

        btnCloseSession.setOnClickListener { presenter.onSessionCloseRequested() }

    }

    override fun showAndUpdateMemberList(members: List<Member>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

    override fun showActivitySelectionActivity(member: Member) {
        val intent = Intent(this, ActivitySelectionActivity::class.java)
        intent.putExtras(member.generateBundle())
        startActivity(intent)
    }

    override fun onSessionClosed() {
        onBackPressed()
    }
}