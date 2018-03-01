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

        fun startTeamMonitoringActivity()

        fun startTaskMonitoringActivity()

        fun setupUserParams(memberType: MemberType, userCF: String, patientCF: String = "")

        fun toggleLeaderMode(isEnabled: Boolean)
    }

    interface LoginPresenter : BasePresenter<LoginView> {

        val sessionList: MutableList<SessionDNS>

        fun resumeView()

        fun onMemberTypeChanged(memberType: MemberType)

        fun onNewSessionRequested()

        fun onSessionCreated(sessionDNS: SessionDNS)

        fun onLeaderCreationResponse(response: GenericResponse)

        fun onSessionListRequested()

        fun onSessionSelected(sessionIndex: Int)

    }

}