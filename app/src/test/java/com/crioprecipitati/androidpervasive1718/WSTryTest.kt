package com.crioprecipitati.androidpervasive1718

import com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring.TeamMonitoringPresenterImpl
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
}