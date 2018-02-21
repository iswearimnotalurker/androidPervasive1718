package com.crioprecipitati.androidpervasive1718

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
        presenter.onConnectRequested("asd", MemberType.MEMBER)
        Thread.sleep(100000)
    }
}