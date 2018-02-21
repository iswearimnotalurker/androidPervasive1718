package com.crioprecipitati.androidpervasive1718

import com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring.TeamMonitoringPresenterImpl
import com.crioprecipitati.androidpervasive1718.viewPresenter.login.LoginPresenterImpl
import com.crioprecipitati.androidpervasive1718.viewPresenter.login.MemberType
import org.junit.Test

/**
 * Created by Edoardo Antonini on 21/02/2018.
 */

class WSTryTest{
    @Test
    fun WSStart(){
        Thread(Runnable { TeamMonitoringPresenterImpl().sendAddLeaderRequest() }).start()
        Thread.sleep(20000)
    }

    @Test
    fun WSGetAllSessions(){
        val presenter = LoginPresenterImpl()
        Thread(Runnable { LoginPresenterImpl().onSessionSelected(MemberType.LEADER) }).start()
        Thread.sleep(10000)
    }
}
