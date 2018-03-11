package com.crioprecipitati.androidpervasive1718.viewPresenter.leader.teamMonitoring

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.view.View
import com.crioprecipitati.androidpervasive1718.R
import com.crioprecipitati.androidpervasive1718.model.LifeParameters
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.utils.*
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseActivity
import com.crioprecipitati.androidpervasive1718.viewPresenter.leader.activitySelection.ActivitySelectionActivity
import kotlinx.android.synthetic.main.activity_team_monitoring.*
import kotlinx.android.synthetic.main.dialog_task.view.*


class TeamMonitoringActivity : BaseActivity<TeamMonitoringContract.TeamMonitoringView, TeamMonitoringContract.TeamMonitoringPresenter>(), TeamMonitoringContract.TeamMonitoringView {

    override var presenter: TeamMonitoringContract.TeamMonitoringPresenter = TeamMonitoringPresenterImpl()
    override val layout: Int = R.layout.activity_team_monitoring
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var memberOnClick: (View, Int, Int) -> Unit
    private lateinit var taskOnClick: (View, Int, Int, Int) -> Unit

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener { onBackPressed() }

        btnCloseSession.setOnClickListener { presenter.onSessionCloseRequested() }

        val bottomSheetView = layoutInflater.inflate(R.layout.dialog_task, null)
        bottomSheetDialog = BottomSheetDialog(getContext())
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetView.parent as View)
        bottomSheetBehavior.peekHeight = 1000

        bottomSheetView.llTerminateTask.setOnClickListener { bottomSheetDialog.consumeBottomSheetDialog { presenter.onTaskCompletionRequested() } }
        bottomSheetView.llDeleteTask.setOnClickListener { bottomSheetDialog.consumeBottomSheetDialog { presenter.onTaskDeletionRequested() } }

        memberOnClick = { _, position, _ -> presenter.onMemberSelected(position) }
        taskOnClick = { _, memberPosition, taskPosition, _ -> presenter.onTaskSelected(memberPosition, taskPosition) }

    }

    override fun onResume() {
        super.onResume()
        presenter.onWSRefreshRequested()
    }

    override fun startLoadingState() {
        runOnUiThread {
            pbTeamSpinner.visibility = View.VISIBLE
            rvMemberList.visibility = View.GONE
        }
    }

    override fun stopLoadingState() {
        runOnUiThread {
            pbTeamSpinner.visibility = View.GONE
            rvMemberList.visibility = View.VISIBLE
        }
    }

    override fun showAndUpdateHealthParameters(lifeParameter: LifeParameters, value: Double) {
        with(value.toString()) {
            when (lifeParameter) {
                LifeParameters.SYSTOLIC_BLOOD_PRESSURE -> tvSYS.setHealthParameterValue(this)
                LifeParameters.DIASTOLIC_BLOOD_PRESSURE -> tvDIA.setHealthParameterValue(this)
                LifeParameters.HEART_RATE -> tvHR.setHealthParameterValue(this)
                LifeParameters.TEMPERATURE -> tvT.setHealthParameterValue(this)
                LifeParameters.OXYGEN_SATURATION -> tvSp02.setHealthParameterValue(this)
                LifeParameters.END_TIDAL_CARBON_DIOXIDE -> tvEtCO2.setHealthParameterValue(this)
            }
        }
    }

    override fun showTaskDialog() {
        runOnUiThread { bottomSheetDialog.show() }
    }

    override fun showAndUpdateMemberAndTaskList() {
        stopLoadingState()
        runOnUiThread {
            with(rvMemberList) {
                adapter = null
                layoutManager = null
                layoutManager = android.support.v7.widget.LinearLayoutManager(this@TeamMonitoringActivity, android.widget.LinearLayout.VERTICAL, false)
                adapter = TeamMonitoringAdapter(presenter.memberList, memberOnClick, taskOnClick)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun showActivitySelectionActivity(userCF: String) {
        val intent = Intent(this, ActivitySelectionActivity::class.java)
        intent.putExtra(BundleStrings.memberExtraString, Member(userCF).generateBundle())
        startActivityForResult(intent, MagicNumbers.activitySelectionActivityLaunchIntentCode)
    }

    override fun onSessionClosed() = finish()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == MagicNumbers.activitySelectionActivityLaunchIntentCode) {
            if (resultCode == Activity.RESULT_OK) {
                startLoadingState()
                val member = Unbundler.extractMember(data!!.getBundleExtra(BundleStrings.memberExtraString))
                val activity = Unbundler.extractActivity(data.getBundleExtra(BundleStrings.activityExtraString))
                presenter.onTaskAdditionRequested(member, activity)
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //TODO
            }
        }
    }
}