package com.crioprecipitati.androidpervasive1718.viewPresenter.login

import com.crioprecipitati.androidpervasive1718.model.SessionDNS
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BasePresenter
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseView
import model.GenericResponse

enum class MemberType {
    LEADER, MEMBER;

    fun isLeader(): Boolean = this == LEADER
}

interface LoginContract {

    interface LoginView : BaseView {

        fun showAndUpdateSessionList()

        fun toggleViewForMemberType(memberType: MemberType)

        fun startTeamMonitoringActivity()

        fun startTaskMonitoringActivity()

        fun setupUserParams(memberType: MemberType, userCF: String, patientCF: String = "")
        fun toggleLeaderMode(isEnabled: Boolean)
    }

    interface LoginPresenter : BasePresenter<LoginView> {

        fun onMemberTypeChanged(memberType: MemberType)

        fun onNewSessionRequested()

        fun onSessionCreated(sessionDNS: SessionDNS)

        fun onLeaderCreationResponse(response: GenericResponse)

        fun onSessionJoinRequested()

        fun onSessionSelected(memberType: MemberType, sessionId: Int) // TODO dovrà prendere un parametro sessione o qualcosa del genere

    }

}