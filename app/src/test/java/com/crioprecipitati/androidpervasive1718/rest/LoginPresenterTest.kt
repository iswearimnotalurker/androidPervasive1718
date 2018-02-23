package com.crioprecipitati.androidpervasive1718.rest

import com.crioprecipitati.androidpervasive1718.viewPresenter.login.LoginContract
import com.crioprecipitati.androidpervasive1718.viewPresenter.login.LoginPresenterImpl
import com.crioprecipitati.androidpervasive1718.viewPresenter.login.MemberType
import org.junit.Before
import org.junit.Test

class LoginPresenterTest {

    private lateinit var presenter: LoginContract.LoginPresenter

    @Before
    fun create() {
        presenter = LoginPresenterImpl()
    }


    @Test
    fun openSessionApi() {
        presenter.onConnectRequested(MemberType.LEADER,1,"Leader")
        Thread.sleep(10000)
    }

    @Test
    fun newSessionApiShouldOpen() {
        presenter.onNewSessionRequested("asd", MemberType.LEADER)
        Thread.sleep(10000)
    }

    @Test
    fun newSessionApiShouldNotOpen() {
        presenter.onNewSessionRequested("asd", MemberType.MEMBER)
        Thread.sleep(10000)
    }
}