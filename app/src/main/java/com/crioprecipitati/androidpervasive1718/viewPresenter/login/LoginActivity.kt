package com.crioprecipitati.androidpervasive1718.viewPresenter.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import com.crioprecipitati.androidpervasive1718.R
import com.crioprecipitati.androidpervasive1718.utils.Prefs
import com.crioprecipitati.androidpervasive1718.utils.consumeSessionButton
import com.crioprecipitati.androidpervasive1718.utils.setTextWithBlankStringCheck
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseActivity
import com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring.TeamMonitoringActivity
import com.crioprecipitati.androidpervasive1718.viewPresenter.member.taskMonitoring.TaskMonitoringActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*

class LoginActivity : BaseActivity<LoginContract.LoginView, LoginContract.LoginPresenter>(), LoginContract.LoginView {

    override var presenter: LoginContract.LoginPresenter = LoginPresenterImpl()
    override val layout: Int = R.layout.activity_login
    private lateinit var itemOnClick: (View, Int, Int) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        itemOnClick = { _, position, _ -> presenter.onSessionSelected(position) }

        rgMemberType.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rbLeader -> presenter.onMemberTypeChanged(MemberType.LEADER)
                R.id.rbMember -> presenter.onMemberTypeChanged(MemberType.MEMBER)
            }
        }

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

        btnCreateNewSession.setOnClickListener { consumeSessionButton(etUsername.text.toString(), etPatient.text.toString()) { presenter.onNewSessionRequested() } }
        btnRequestOpenSessions.setOnClickListener { consumeSessionButton(etUsername.text.toString()) { presenter.onSessionListRequested() } }

        btnShowIPDialog.setOnClickListener {
            alert("Change main IP address") {
                var ipAddress = EditText(ctx)
                customView {
                    ipAddress = editText(Prefs.ip)
                }
                yesButton {
                    Prefs.ip = ipAddress.text.toString()
                    toast("New IP set to: ${Prefs.ip}")
                    presenter.onIpChanged()
                }
                noButton { "Cancel" }
            }.show()
        }

        etUsername.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                Prefs.userCF = etUsername.text.toString()
                presenter.onSessionListRequested()
            }
        }

        etPatient.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                Prefs.patientCF = etPatient.text.toString()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.resumeView()
    }

    override fun startLoadingState() {
        runOnUiThread {
            pbLoadingSpinner.visibility = View.VISIBLE
            rvSessionList.visibility = View.GONE
            btnCreateNewSession.isEnabled = false
            btnRequestOpenSessions.isEnabled = false
        }
    }

    override fun stopLoadingState() {
        runOnUiThread {
            pbLoadingSpinner.visibility = View.GONE
            rvSessionList.visibility = View.VISIBLE
            btnCreateNewSession.isEnabled = Prefs.memberType == MemberType.LEADER
            btnRequestOpenSessions.isEnabled = true
        }
    }

    override fun toggleLeaderMode(isEnabled: Boolean) {
        btnCreateNewSession.isEnabled = isEnabled
        etPatient.isEnabled = isEnabled
    }

    override fun setupUserParams() {

        fun setupUIElements(leaderMode: Boolean) {
            rbLeader.isChecked = leaderMode
            rbMember.isChecked = !leaderMode
            toggleLeaderMode(leaderMode)
        }

        when (Prefs.memberType) {
            MemberType.LEADER -> setupUIElements(leaderMode = true)
            MemberType.MEMBER -> setupUIElements(leaderMode = false)
        }

        etPatient.setTextWithBlankStringCheck(Prefs.patientCF)
        etUsername.setTextWithBlankStringCheck(Prefs.userCF)
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
        startActivity(intent)
    }

    override fun startTeamMonitoringActivity() {
        val intent = Intent(this, TeamMonitoringActivity::class.java)
        startActivity(intent)
    }
}