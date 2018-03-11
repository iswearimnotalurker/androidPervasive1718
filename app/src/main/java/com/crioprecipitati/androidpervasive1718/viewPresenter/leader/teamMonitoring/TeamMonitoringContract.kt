package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import com.crioprecipitati.androidpervasive1718.model.Activity
import com.crioprecipitati.androidpervasive1718.model.AugmentedMember
import com.crioprecipitati.androidpervasive1718.model.LifeParameters
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BasePresenter
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseView


interface TeamMonitoringContract {

    interface TeamMonitoringView : BaseView {

        fun showAndUpdateMemberAndTaskList()

        fun showAndUpdateHealthParameters(lifeParameter: LifeParameters, value: Double)

        fun showTaskDialog()

        fun showActivitySelectionActivity(userCF: String)

        fun onSessionClosed()

    }

    interface TeamMonitoringPresenter : BasePresenter<TeamMonitoringView> {

        val memberList: MutableList<AugmentedMember>

        var member: Member?

        fun onTaskAdditionRequested(member: Member, activity: Activity)

        fun onTaskDeletionRequested()

        fun onTaskCompletionRequested()

        fun onMemberSelected(memberIndex: Int)

        fun onTaskSelected(memberIndex: Int, taskIndex: Int)

        fun onSessionCloseRequested()

        fun onWSRefreshRequested()

    }
}