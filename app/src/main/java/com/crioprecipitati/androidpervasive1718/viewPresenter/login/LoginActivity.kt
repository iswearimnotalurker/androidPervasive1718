package com.crioprecipitati.androidpervasive1718.viewPresenter.login

import android.os.Bundle
import com.crioprecipitati.androidpervasive1718.R
import com.crioprecipitati.androidpervasive1718.base.BaseActivity
import com.crioprecipitati.androidpervasive1718.model.Member
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<LoginContract.LoginView, LoginContract.LoginPresenter>(), LoginContract.LoginView {

    override var presenter: LoginContract.LoginPresenter = LoginPresenterImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnCreateNewSession.setOnClickListener { presenter.onNewSessionRequested("gntlrt94b21g479u", MemberType.LEADER) }

        btnRequestOpenSessions.setOnClickListener { presenter.onConnectRequested(MemberType.LEADER, 0, "leader supremo") }

    }

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
//        val intent = Intent(this, TaskMonitoringActivity::class.java)
//        intent.putExtras(member.generateBundle())
//        startActivity(intent)
    }

    override fun startTeamMonitoringActivity(member: Member) {
//        val intent = Intent(this, TeamMonitoringActivity::class.java)
//        intent.putExtras(member.generateBundle())
//        startActivity(intent)
    }
}