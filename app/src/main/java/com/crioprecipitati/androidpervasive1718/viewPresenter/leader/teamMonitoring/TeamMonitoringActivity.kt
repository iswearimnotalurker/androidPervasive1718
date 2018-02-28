package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import android.content.Intent
import android.os.Bundle
import com.crioprecipitati.androidpervasive1718.R
import com.crioprecipitati.androidpervasive1718.model.LifeParameters
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.model.Task
import com.crioprecipitati.androidpervasive1718.utils.Unbudler
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
        savedInstanceState?.let { presenter.member = Unbudler.extractMember(it) }
    }

    override fun showAndUpdateMemberList(members: List<Member>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAndUpdateTaskList(member: Member, task: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAndUpdateHealthParameters(lifeParameter: LifeParameters, value: Double) {
        when (lifeParameter) {
            LifeParameters.SYSTOLIC_BLOOD_PRESSURE -> tvSYS.setHealthParameterValue(value)
            LifeParameters.DIASTOLIC_BLOOD_PRESSURE -> tvDIA.setHealthParameterValue(value)
            LifeParameters.HEART_RATE -> tvHR.setHealthParameterValue(value)
            LifeParameters.TEMPERATURE -> tvT.setHealthParameterValue(value)
            LifeParameters.OXYGEN_SATURATION -> tvSp02.setHealthParameterValue(value)
            LifeParameters.END_TIDAL_CARBON_DIOXIDE -> tvEtCO2.setHealthParameterValue(value)
        }
    }

    override fun showActivitySelectionActivity(member: Member) {
        val intent = Intent(this, ActivitySelectionActivity::class.java)
        intent.putExtras(member.generateBundle())
        startActivity(intent)
    }


}