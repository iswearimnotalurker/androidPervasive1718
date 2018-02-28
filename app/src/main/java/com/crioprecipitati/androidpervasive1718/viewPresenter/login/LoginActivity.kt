package com.crioprecipitati.androidpervasive1718.viewPresenter.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.crioprecipitati.androidpervasive1718.R
import com.crioprecipitati.androidpervasive1718.utils.Prefs
import com.crioprecipitati.androidpervasive1718.utils.consumeSessionButton
import com.crioprecipitati.androidpervasive1718.utils.setTextWithBlankStringCheck
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseActivity
import com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring.TeamMonitoringActivity
import com.crioprecipitati.androidpervasive1718.viewPresenter.member.taskMonitoring.TaskMonitoringActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<LoginContract.LoginView, LoginContract.LoginPresenter>(), LoginContract.LoginView {

    override var presenter: LoginContract.LoginPresenter = LoginPresenterImpl()
    private lateinit var itemOnClick: (View, Int, Int) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupUserParams(Prefs.memberType, Prefs.userCF, Prefs.patientCF)

        itemOnClick = { _, position, _ -> presenter.onSessionSelected(position) }

        rgMemberType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbLeader -> presenter.onMemberTypeChanged(MemberType.LEADER)
                R.id.rbMember -> presenter.onMemberTypeChanged(MemberType.MEMBER)
            }
        }

        btnCreateNewSession.setOnClickListener { consumeSessionButton(etUsername.text.toString(), etPatient.text.toString()) { presenter.onNewSessionRequested() } }
        btnRequestOpenSessions.setOnClickListener { consumeSessionButton(etUsername.text.toString()) { presenter.onSessionJoinRequested() } }
    }

    //////////////////// LOADING
//    override fun startLoadingState() = urlFetchingWaitDialog.show()

//    override fun stopLoadingState() = urlFetchingWaitDialog.cancel()

    override fun toggleLeaderMode(isEnabled: Boolean) {
        btnCreateNewSession.isEnabled = isEnabled
        etPatient.isEnabled = isEnabled
    }

    override fun setupUserParams(memberType: MemberType, userCF: String, patientCF: String) {

        fun setupUIElements(leaderMode: Boolean) {
            rbLeader.isChecked = leaderMode
            rbMember.isChecked = !leaderMode
            toggleLeaderMode(leaderMode)
        }

        when (memberType) {
            MemberType.LEADER -> setupUIElements(leaderMode = true)
            MemberType.MEMBER -> setupUIElements(leaderMode = false)
        }

        etPatient.setTextWithBlankStringCheck(patientCF)
        etUsername.setTextWithBlankStringCheck(userCF)
    }

    override fun showAndUpdateSessionList() {
        runOnUiThread {
            with(rvSessionList) {
                adapter = null
                layoutManager = null
                layoutManager = android.support.v7.widget.LinearLayoutManager(this@LoginActivity, android.widget.LinearLayout.VERTICAL, false)
                adapter = SessionListAdapter(presenter.sessionList, itemOnClick)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun startTaskMonitoringActivity() {
        val intent = Intent(this, TaskMonitoringActivity::class.java)
        //intent.putExtras(member.generateBundle())
        startActivity(intent)
    }

    override fun startTeamMonitoringActivity() {
        val intent = Intent(this, TeamMonitoringActivity::class.java)
        //intent.putExtras(member.generateBundle())
        startActivity(intent)
    }
}