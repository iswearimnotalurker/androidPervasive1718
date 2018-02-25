package com.crioprecipitati.androidpervasive1718.rest

import com.crioprecipitati.androidpervasive1718.viewPresenter.login.LoginContract
import com.crioprecipitati.androidpervasive1718.viewPresenter.login.LoginPresenterImpl
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
        presenter.onSessionJoinRequested()
        Thread.sleep(10000)
    }

    @Test
    fun newSessionApiShouldOpen() {
        presenter.onNewSessionRequested()
        Thread.sleep(10000)
    }

    @Test
    fun newSessionApiShouldNotOpen() {
        presenter.onNewSessionRequested()
        Thread.sleep(10000)
    }
}