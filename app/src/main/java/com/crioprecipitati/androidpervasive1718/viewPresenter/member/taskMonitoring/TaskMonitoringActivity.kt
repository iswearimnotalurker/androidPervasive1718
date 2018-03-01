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

    private val parametersViews: HashMap<LifeParameters, Pair<TextView, TextView>> = HashMap()


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
        //activityName.text = augmentedTask.activityName
        createNewTable(augmentedTask.linkedParameters)
    }

    override fun showEmptyTask() {
        lifeParametersLinearLayout.removeAllViews()
    }

    override fun showAlarmedTask() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateHealthParameterValues(parameter: LifeParameters, value: Double) {
        parametersViews[parameter]!!.second.setHealthParameterValue(value.toString())
    }

    private fun createNewTable(parameters: List<LifeParameters>) {
        lifeParametersLinearLayout.setBackgroundColor(Color.BLACK)

        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT / parameters.size,
                LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams.setMargins(0, 0, 0, 0)

        //lifeParametersLinearLayout.layoutParams

        parameters.forEach {
            //lifeParametersLinearLayout.addView(wrapParameterView(it))
            lifeParametersLinearLayout.addView(parametersViews[it]!!.first, layoutParams)
            lifeParametersLinearLayout.addView(parametersViews[it]!!.second, layoutParams)
        }
    }

    private fun initializeParametersViews() {
        LifeParameters.values().forEach { parametersViews[it] = createParameterView(it) }
    }

    private fun createParameterView(parameter: LifeParameters): Pair<TextView, TextView> {
        val parameterAcronymView = TextView(this)
        parameterAcronymView.setBackgroundColor(Color.GRAY)
        parameterAcronymView.gravity = Gravity.CENTER
        parameterAcronymView.setTextColor(Color.BLACK)
        parameterAcronymView.text = parameter.acronym

        val parameterValueView = TextView(this)
        parameterValueView.setBackgroundColor(Color.WHITE)
        parameterValueView.gravity = Gravity.CENTER
        parameterValueView.text = ""
        parameterValueView.setTextColor(Color.BLACK)


        //parameterAcronymView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        //parameterValueView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        return Pair(parameterAcronymView, parameterValueView)
    }


    private fun wrapParameterView(parameter: LifeParameters): LinearLayout {
        val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        layoutParams.setMargins(0, 0, 0, 0)

        val parameterLayout = LinearLayout(this)
        parameterLayout.orientation = LinearLayout.VERTICAL
        //parameterLayout.layoutParams = layoutParams
        parameterLayout.addView(parametersViews[parameter]!!.first)
        parameterLayout.addView(parametersViews[parameter]!!.second)
        return parameterLayout
        //val parameterView = ListView(this)

        //parameterView.addView(parametersViews[parameter]!!.first)
        //parameterView.addView(parametersViews[parameter]!!.first)
        //return parameterView
    }
}