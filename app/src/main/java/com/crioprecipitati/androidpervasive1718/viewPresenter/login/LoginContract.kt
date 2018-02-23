package com.crioprecipitati.androidpervasive1718.viewPresenter.login

import com.crioprecipitati.androidpervasive1718.base.BasePresenter
import com.crioprecipitati.androidpervasive1718.base.BaseView
import com.crioprecipitati.androidpervasive1718.model.Member
import model.GenericResponse

enum class MemberType {
    LEADER, MEMBER
}

interface LoginContract {

    interface LoginView : BaseView {

        fun showAndUpdateSessionList()

        fun toggleViewForMemberType(memberType: MemberType)

        fun startTeamMonitoringActivity(member: Member)

        fun startTaskMonitoringActivity(member: Member)

    }

    interface LoginPresenter : BasePresenter<LoginView> {

        fun onConnectRequested(memberType: MemberType, id: Int, name: String)

        fun onNewSessionRequested(cf: String, memberType: MemberType)

        fun onSessionSelected(memberType: MemberType, sessionId: Int) // TODO dovrà prendere un parametro sessione o qualcosa del genere

        fun onSessionCreated(memberType: MemberType, sessionId: Int)

        fun onLeaderCreationResponse(response: GenericResponse)

    }

}