package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import com.crioprecipitati.androidpervasive1718.base.BaseActivity

class TeamMonitoringActivity : BaseActivity<TeamMonitoringContract.TeamMonitoringView, TeamMonitoringContract.TeamMonitoringPresenter>(), TeamMonitoringContract.TeamMonitoringView {

    override var presenter: TeamMonitoringContract.TeamMonitoringPresenter = TeamMonitoringPresenterImpl()

    override fun showAndUpdateMemberList() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAndUpdateTaskList() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAndUpdateHealthParameters() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}