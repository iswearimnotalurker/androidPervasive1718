package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import com.crioprecipitati.androidpervasive1718.networking.TaskWSAdapter
import com.crioprecipitati.androidpervasive1718.networking.WSLeaderCallbacks

class TeamMonitoringPresenterImpl : TeamMonitoringContract.TeamMonitoringPresenter,WSLeaderCallbacks {

    private val webSocketHelper: TaskWSAdapter = TaskWSAdapter(this)


    override fun attachView(view: TeamMonitoringContract.TeamMonitoringView) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun detachView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTaskDeleted() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onMemberSelected() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onAlarmReceived(alarmString: String?) {
        println(alarmString)
    }

    override fun onMemberArrived(memberString: String?) {
        println(memberString)
    }

    override fun onTaskCompleted(taskCompletedString: String?) {
        println(taskCompletedString)
    }
}