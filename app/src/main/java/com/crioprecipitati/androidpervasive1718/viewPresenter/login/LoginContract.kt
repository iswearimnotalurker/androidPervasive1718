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

        fun onConnectRequested(cf: String, memberType: MemberType)

        fun onNewSessionRequested()

        fun onSessionSelected() // TODO dovrà prendere un parametro sessione o qualcosa del genere

    }

}