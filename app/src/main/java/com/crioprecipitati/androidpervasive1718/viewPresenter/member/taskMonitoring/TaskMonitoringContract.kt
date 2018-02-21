package com.crioprecipitati.androidpervasive1718.viewPresenter.member.taskMonitoring

import com.crioprecipitati.androidpervasive1718.base.BasePresenter
import com.crioprecipitati.androidpervasive1718.base.BaseView

interface TaskMonitoringContract {

    interface TaskMonitoringView : BaseView {

        fun showNewTask()

        fun showEmptyTask()

        fun showAlarmedTask()

        fun updateHealthParameterValues()

    }

    interface TaskMonitoringPresenter : BasePresenter<TaskMonitoringView> {

        fun onTaskCompletionRequested()

    }

}