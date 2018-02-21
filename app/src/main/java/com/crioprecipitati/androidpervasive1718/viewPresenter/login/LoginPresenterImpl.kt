package com.crioprecipitati.androidpervasive1718.viewPresenter.login

import android.support.design.widget.Snackbar
import com.crioprecipitati.androidpervasive1718.networking.handlers.SessionApiHandler
import io.reactivex.schedulers.Schedulers

class LoginPresenterImpl : LoginContract.LoginPresenter {

    private lateinit var loginView: LoginContract.LoginView

    override fun attachView(view: LoginContract.LoginView) {
        loginView = view
    }

    override fun detachView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectRequested(cf: String, memberType: MemberType) {
        val subscription = SessionApiHandler().getAllSessions()
            .subscribeOn(Schedulers.io())
            .subscribe (
                    { retrievedSessions ->
                        //(session_list.adapter as SessionAdapter).addSession(retrievedSessions)
                        println(retrievedSessions.first())
                    },
                    { e ->
                        //Snackbar.make(session_list, e.message ?: "", Snackbar.LENGTH_LONG).show()
                        println(e.message)
                    }
            )
    }

    override fun onNewSessionRequested() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onSessionSelected() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}