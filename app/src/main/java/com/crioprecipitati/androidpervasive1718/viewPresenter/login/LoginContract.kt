package com.crioprecipitati.androidpervasive1718.viewPresenter.login

import com.crioprecipitati.androidpervasive1718.base.BasePresenter
import com.crioprecipitati.androidpervasive1718.base.BaseView

enum class MemberType {
    LEADER, MEMBER
}

interface LoginContract {

    interface LoginView : BaseView {

        fun showAndUpdateSessionList()

        fun toggleViewForMemberType(memberType: MemberType)

    }

    interface LoginPresenter : BasePresenter<LoginView> {

        fun onConnectRequested()

        fun onNewSessionRequested(cf: String, memberType: MemberType)

        fun onSessionSelected(memberType: MemberType, sessionId: Int) // TODO dovrà prendere un parametro sessione o qualcosa del genere

        fun onSessionCreated(memberType: MemberType, sessionId: Int)

    }

}