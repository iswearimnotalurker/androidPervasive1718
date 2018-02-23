package com.crioprecipitati.androidpervasive1718

import com.crioprecipitati.androidpervasive1718.viewPresenter.login.LoginPresenterImpl
import com.crioprecipitati.androidpervasive1718.viewPresenter.login.MemberType
import org.junit.Test

class WSTryTest{

    @Test
    fun WSGetAllSessions(){
        val presenter = LoginPresenterImpl()
        presenter.onConnectRequested(MemberType.LEADER, 0, "non andrà mai")
        Thread(Runnable {
            presenter.onNewSessionRequested("erdf",MemberType.LEADER)
            //presenter.onSessionSelected(MemberType.LEADER, presenter.sessionId)
        }).start()
        Thread.sleep(10000)
    }
}
