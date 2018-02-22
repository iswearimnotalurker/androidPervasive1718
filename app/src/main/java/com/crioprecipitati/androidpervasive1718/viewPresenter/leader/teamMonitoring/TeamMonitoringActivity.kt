package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import com.crioprecipitati.androidpervasive1718.base.BaseActivity
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.model.Task
import model.LifeParameters


class TeamMonitoringActivity : BaseActivity<TeamMonitoringContract.TeamMonitoringView, TeamMonitoringContract.TeamMonitoringPresenter>(), TeamMonitoringContract.TeamMonitoringView {

    override var presenter: TeamMonitoringContract.TeamMonitoringPresenter = TeamMonitoringPresenterImpl

    override fun showAndUpdateMemberList(members: List<Member>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAndUpdateTaskList(member: Member, task: Task) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAndUpdateHealthParameters(lifeParameter: LifeParameters, value: Double) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}