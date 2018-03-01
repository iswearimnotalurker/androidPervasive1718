package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import com.crioprecipitati.androidpervasive1718.model.AugmentedTask
import com.crioprecipitati.androidpervasive1718.model.LifeParameters
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BasePresenter
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseView


interface TeamMonitoringContract {

    interface TeamMonitoringView : BaseView {

        fun showAndUpdateMemberList()

        fun showAndUpdateTaskList(member: Member, task: AugmentedTask)

        fun showAndUpdateHealthParameters(lifeParameter: LifeParameters, value: Double)

        fun showActivitySelectionActivity(member: Member)

        fun onSessionClosed()

    }

    interface TeamMonitoringPresenter : BasePresenter<TeamMonitoringView> {

        val memberList: MutableList<Member>

        var member: Member?

        fun onTaskDeleted()

        fun onMemberSelected(userIndex: Int)

        fun onSessionCloseRequested()

    }
}