package com.crioprecipitati.androidpervasive1718.viewPresenter.member.taskMonitoring

import com.crioprecipitati.androidpervasive1718.base.BasePresenter
import com.crioprecipitati.androidpervasive1718.networking.webSockets.TaskWSAdapter

object TaskMonitoringPresenterImpl : BasePresenter<TaskMonitoringContract.TaskMonitoringView>, TaskMonitoringContract.TaskMonitoringPresenter {

    private val webSocketHelper: TaskWSAdapter = TaskWSAdapter
    override fun attachView(view: TaskMonitoringContract.TaskMonitoringView) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun detachView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onTaskCompletionRequested() {
        TODO("change status to finished") //To change body of created functions use File | Settings | File Templates.
    }
}