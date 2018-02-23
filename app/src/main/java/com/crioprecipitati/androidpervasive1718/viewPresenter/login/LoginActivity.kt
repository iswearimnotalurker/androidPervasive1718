package com.crioprecipitati.androidpervasive1718.viewPresenter.login

import android.content.Intent
import com.crioprecipitati.androidpervasive1718.base.BaseActivity
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.model.generateBundle
import com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring.TeamMonitoringActivity
import com.crioprecipitati.androidpervasive1718.viewPresenter.member.taskMonitoring.TaskMonitoringActivity

class LoginActivity : BaseActivity<LoginContract.LoginView, LoginContract.LoginPresenter>(), LoginContract.LoginView {

    override var presenter: LoginContract.LoginPresenter = LoginPresenterImpl()

    override fun showAndUpdateSessionList() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun toggleViewForMemberType(memberType: MemberType) {
        if(memberType == MemberType.LEADER){
            //TODO FAI COSE
        }else{
            //TODO FAI ALTRE COSE
        }
    }

    override fun startTaskMonitoringActivity(member: Member) {
        val intent = Intent(this, TaskMonitoringActivity::class.java)
        intent.putExtras(member.generateBundle())
        startActivity(intent)
    }

    override fun startTeamMonitoringActivity(member: Member) {
        val intent = Intent(this, TeamMonitoringActivity::class.java)
        intent.putExtras(member.generateBundle())
        startActivity(intent)
    }
}