package com.crioprecipitati.androidpervasive1718.viewPresenter.member.taskMonitoring

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.crioprecipitati.androidpervasive1718.R
import com.crioprecipitati.androidpervasive1718.model.AugmentedTask
import com.crioprecipitati.androidpervasive1718.model.LifeParameters
import com.crioprecipitati.androidpervasive1718.utils.setHealthParameterValue
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseActivity
import kotlinx.android.synthetic.main.activity_task_monitoring.*

class TaskMonitoringActivity : BaseActivity<TaskMonitoringContract.TaskMonitoringView, TaskMonitoringContract.TaskMonitoringPresenter>(), TaskMonitoringContract.TaskMonitoringView {

    override var presenter: TaskMonitoringContract.TaskMonitoringPresenter = TaskMonitoringPresenterImpl()
    override val layout: Int = R.layout.activity_task_monitoring

    private val parametersViews: HashMap<LifeParameters, TextView> = HashMap()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //sv = ScrollView(this)

        //setContentView(R.layout.activity_task_monitoring)
        initializeParametersViews()
        createNewTable(LifeParameters.values().toList())
        //sv.addView(hsv)

        //clearContent()
        //createNewTable()
    }

    override fun showNewTask(augmentedTask: AugmentedTask) {

    }

    override fun showEmptyTask() {
        lifeParametersTable.removeAllViews()
    }

    override fun showAlarmedTask() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateHealthParameterValues(parameter: LifeParameters, value: Double) {
        parametersViews[parameter]!!.setHealthParameterValue(parameter.acronym + " " + value)
    }

    private fun createNewTable(parameters: List<LifeParameters>) {
        lifeParametersTable.setBackgroundColor(Color.BLACK)

        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(1, 1, 1, 1)

        lifeParametersTable.layoutParams

        parameters.forEach {
            lifeParametersTable.addView(parametersViews[it], layoutParams)
        }
    }

    private fun initializeParametersViews() {
        LifeParameters.values().forEach { parametersViews[it] = createParameterView(it) }
    }

    private fun createParameterView(parameter: LifeParameters): TextView {
        val parameterView = TextView(this)
        //  textView.setText(String.valueOf(j));
        parameterView.setBackgroundColor(Color.WHITE)
        parameterView.gravity = Gravity.CENTER

        parameterView.text = parameter.acronym
        parameterView.setTextColor(Color.BLACK)
        return parameterView
    }
}