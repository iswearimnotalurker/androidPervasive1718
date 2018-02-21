package com.crioprecipitati.androidpervasive1718.rest

import com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring.TeamMonitoringContract
import com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring.TeamMonitoringPresenterImpl
import com.crioprecipitati.androidpervasive1718.viewPresenter.login.LoginContract
import com.crioprecipitati.androidpervasive1718.viewPresenter.login.LoginPresenterImpl
import org.junit.Before
import org.junit.Test

class TeamMonitoringPresenterTest {

    private lateinit var teamMonitoringPresenter: TeamMonitoringContract.TeamMonitoringPresenter
    private lateinit var loginPresenter: LoginContract.LoginPresenter

    @Before
    fun create() {
        teamMonitoringPresenter = TeamMonitoringPresenterImpl()
        loginPresenter = LoginPresenterImpl()
    }

    /* Should print on console Server Error */
    @Test
    fun deleteNotExistingSession() {
        teamMonitoringPresenter.onSessionClosed(-1)
        Thread.sleep(10000)
    }
}