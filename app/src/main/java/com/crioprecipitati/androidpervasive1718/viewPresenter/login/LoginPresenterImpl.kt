package com.crioprecipitati.androidpervasive1718.viewPresenter.login

import com.crioprecipitati.androidpervasive1718.model.Activity
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.model.SessionDNS
import com.crioprecipitati.androidpervasive1718.networking.RestApiManager
import com.crioprecipitati.androidpervasive1718.networking.api.SessionApi
import com.crioprecipitati.androidpervasive1718.networking.webSockets.TaskWSAdapter
import com.crioprecipitati.androidpervasive1718.networking.webSockets.WSCallbacks
import com.crioprecipitati.androidpervasive1718.utils.GsonInitializer.gson
import com.crioprecipitati.androidpervasive1718.utils.toJson
import io.reactivex.android.schedulers.AndroidSchedulers
import model.MembersAdditionNotification
import model.PayloadWrapper
import model.WSOperations

class LoginPresenterImpl : LoginContract.LoginPresenter, WSCallbacks {

    private lateinit var loginView: LoginContract.LoginView
    private val webSocketHelper: TaskWSAdapter = TaskWSAdapter(this)
    // This will be removed from here
    lateinit var activities: List<Activity>

    // Should be used only from onSessionSelected
    override fun onMessageReceived(messageString: String?) {
        activities = gson.fromJson((gson.fromJson(messageString, PayloadWrapper::class.java).body), Array<Activity>::class.java).toList()
        if (activities.isNotEmpty()) println("We got activities!")
    }

    override fun attachView(view: LoginContract.LoginView) {
        loginView = view
    }

    override fun detachView() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onConnectRequested() {

        RestApiManager
            .createService(SessionApi::class.java)
            .getAllSessions()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { /*loginView?.startLoadingState()*/ }
            .doAfterTerminate { /*loginView?.stopLoadingState()*/ }
            .subscribe(
                    { sessionList ->
                        // View logic here
                        sessionList.forEach {
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
            RestApiManager
                .createService(SessionApi::class.java)
                .createNewSession(cf)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { /*loginView?.startLoadingState()*/ }
                .doAfterTerminate { /*loginView?.stopLoadingState()*/ }
                .subscribe(
                        { sessionInfo ->
                            // View logic here
                            println(sessionInfo as SessionDNS)
                        },
                        { e ->
                            //Snackbar.make(session_list, e.message ?: "", Snackbar.LENGTH_LONG).show()
                            println(e.message)
                        }
                )
        }
    }

    override fun onSessionSelected(memberType: MemberType) {
        if (memberType.equals(MemberType.LEADER)) {
            var members: List<Member> = listOf(Member(1,"Leader"))
            val message = PayloadWrapper(0, WSOperations.GET_ALL_ACTIVITIES, MembersAdditionNotification(members).toJson())
            webSocketHelper.webSocket.send(message.toJson())
        }
        else {
            TODO("not implemented")
        } //To change body of created functions use File | Settings | File Templates.
    }
}