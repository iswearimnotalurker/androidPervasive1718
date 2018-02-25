package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import android.content.Intent
import android.os.Bundle
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.model.Task
import com.crioprecipitati.androidpervasive1718.model.Unbudler
import com.crioprecipitati.androidpervasive1718.model.generateBundle
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseActivity
import com.crioprecipitati.androidpervasive1718.viewPresenter.leader.activitySelection.ActivitySelectionActivity
import model.LifeParameters


class TeamMonitoringActivity : BaseActivity<TeamMonitoringContract.TeamMonitoringView, TeamMonitoringContract.TeamMonitoringPresenter>(), TeamMonitoringContract.TeamMonitoringView {

    override var presenter: TeamMonitoringContract.TeamMonitoringPresenter = TeamMonitoringPresenterImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        savedInstanceState?.also { presenter.member = Unbudler.extractMember(it) }
    }

    override fun showAndUpdateMemberList(members: List<Member>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAndUpdateTaskList(member: Member, task: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAndUpdateHealthParameters(lifeParameter: LifeParameters, value: Double) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showActivitySelectionActivity(member: Member) {
        val intent = Intent(this, ActivitySelectionActivity::class.java)
        intent.putExtras(member.generateBundle())
        startActivity(intent)
    }

}