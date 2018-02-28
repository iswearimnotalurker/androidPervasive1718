package com.crioprecipitati.androidpervasive1718.viewPresenter.member.taskMonitoring

import com.crioprecipitati.androidpervasive1718.R
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseActivity

class TaskMonitoringActivity : BaseActivity<TaskMonitoringContract.TaskMonitoringView, TaskMonitoringContract.TaskMonitoringPresenter>(), TaskMonitoringContract.TaskMonitoringView {

    override var presenter: TaskMonitoringContract.TaskMonitoringPresenter = TaskMonitoringPresenterImpl()
    override val layout: Int = R.layout.activity_task_monitoring

    override fun showNewTask() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showEmptyTask() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAlarmedTask() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateHealthParameterValues() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}