package com.crioprecipitati.androidpervasive1718.viewPresenter.login

import com.crioprecipitati.androidpervasive1718.model.SessionDNS
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

    override fun onConnectRequested() {
        SessionApiHandler().getAllSessions()
                .subscribeOn(Schedulers.io())
                .subscribe (
                        { retrievedSessions ->
                            //(session_list.adapter as SessionAdapter).addSession(retrievedSessions)
                            (retrievedSessions as List<SessionDNS>).forEach{
                                it -> println(it)
                            }
                        },
                        { e ->
                            //Snackbar.make(session_list, e.message ?: "", Snackbar.LENGTH_LONG).show()
                            println(e.message)
                        }
                )
    }

    override fun onNewSessionRequested(cf: String, memberType: MemberType) {
        if (memberType.equals(MemberType.LEADER)) {
            println("Creating new session with CF "+cf)
            SessionApiHandler().createNewSession(cf)
                    .subscribeOn(Schedulers.io())
                    .subscribe(
                            { sessionInfo ->
                                //(session_list.adapter as SessionAdapter).addSession(retrievedSessions)
                                println(sessionInfo as SessionDNS)
                                // MT.addLeader(myself)
                            },
                            { e ->
                                //Snackbar.make(session_list, e.message ?: "", Snackbar.LENGTH_LONG).show()
                                println(e.message)
                            }
                    )
        }
    }

    override fun onSessionSelected() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}