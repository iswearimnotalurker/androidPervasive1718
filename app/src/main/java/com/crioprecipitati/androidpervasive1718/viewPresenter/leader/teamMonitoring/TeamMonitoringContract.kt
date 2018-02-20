package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import com.crioprecipitati.androidpervasive1718.base.BasePresenter
import com.crioprecipitati.androidpervasive1718.base.BaseView


interface TeamMonitoringContract {

    interface TeamMonitoringView : BaseView {

        fun showAndUpdateMemberList()

        fun showAndUpdateTaskList()

        fun showAndUpdateHealthParameters()

    }

    interface TeamMonitoringPresenter : BasePresenter<TeamMonitoringView> {

        fun onTaskDeleted()

        fun onMemberSelected()

    }
}