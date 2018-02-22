package com.crioprecipitati.androidpervasive1718.viewPresenter.login

import com.crioprecipitati.androidpervasive1718.base.BaseActivity

class LoginActivity : BaseActivity<LoginContract.LoginView, LoginContract.LoginPresenter>(), LoginContract.LoginView {

    override var presenter: LoginContract.LoginPresenter = LoginPresenterImpl

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
}