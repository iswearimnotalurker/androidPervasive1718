package com.crioprecipitati.androidpervasive1718.viewPresenter.member.taskMonitoring

import com.crioprecipitati.androidpervasive1718.model.AugmentedTask
import com.crioprecipitati.androidpervasive1718.model.LifeParameters
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BasePresenter
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseView
import model.Notification

interface TaskMonitoringContract {

    interface TaskMonitoringView : BaseView {

        fun showNewTask(augmentedTask: AugmentedTask)

        fun showEmptyTask()

        fun showAlarmedTask(notification : Notification)

        fun updateHealthParameterValues(parameter: LifeParameters, value: Double)

    }

    interface TaskMonitoringPresenter : BasePresenter<TaskMonitoringView> {

        fun onTaskCompletionRequested()

    }

}