package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.model.Task
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BasePresenter
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseView
import model.LifeParameters


interface TeamMonitoringContract {

    interface TeamMonitoringView : BaseView {

        fun showAndUpdateMemberList(members:List<Member>)

        fun showAndUpdateTaskList(member: Member, task: Task)

        fun showAndUpdateHealthParameters(lifeParameter: LifeParameters, value: Double)

        fun showActivitySelectionActivity(member: Member)

    }

    interface TeamMonitoringPresenter : BasePresenter<TeamMonitoringView> {

        var member: Member?

        fun onTaskDeleted()

        fun onMemberSelected()

        fun onSessionClosed(sessionId: Int)

    }
}