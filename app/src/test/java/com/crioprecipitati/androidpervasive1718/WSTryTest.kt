package com.crioprecipitati.androidpervasive1718

import com.crioprecipitati.androidpervasive1718.viewPresenter.login.LoginPresenterImpl
import com.crioprecipitati.androidpervasive1718.viewPresenter.login.MemberType
import org.junit.Test

/**
 * Created by Edoardo Antonini on 21/02/2018.
 */

class WSTryTest{

    @Test
    fun WSGetAllSessions(){
        val presenter = LoginPresenterImpl
        Thread(Runnable {
            presenter.onNewSessionRequested("erdf",MemberType.LEADER)
            //presenter.onSessionSelected(MemberType.LEADER, presenter.sessionId)
        }).start()
        Thread.sleep(10000)
    }
}
