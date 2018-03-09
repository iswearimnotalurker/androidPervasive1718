package com.crioprecipitati.androidpervasive1718.viewPresenter.member.taskMonitoring

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.crioprecipitati.androidpervasive1718.R
import com.crioprecipitati.androidpervasive1718.model.AugmentedTask
import com.crioprecipitati.androidpervasive1718.model.LifeParameters
import com.crioprecipitati.androidpervasive1718.utils.setHealthParameterValue
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseActivity
import kotlinx.android.synthetic.main.activity_task_monitoring.*
import model.Notification
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.textColor

open class TaskMonitoringActivity : BaseActivity<TaskMonitoringContract.TaskMonitoringView, TaskMonitoringContract.TaskMonitoringPresenter>(), TaskMonitoringContract.TaskMonitoringView {

    override var presenter: TaskMonitoringContract.TaskMonitoringPresenter = TaskMonitoringPresenterImpl()
    override val layout: Int = R.layout.activity_task_monitoring

    private val parametersViews: HashMap<LifeParameters, Pair<TextView, TextView>> = HashMap()

    private lateinit var oldColors: ColorStateList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnEndOperation.onClick {
            presenter.onTaskCompletionRequested()
        }

        oldColors = activityName.textColors
    }

    override fun onResume() {
        super.onResume()
        initializeParametersViews()
        //createNewTable(LifeParameters.values().map { it.toString() }.toList())
        showEmptyTask()
    }

    override fun startLoadingState() {
        runOnUiThread {
            pbTaskMonitoringSpinner.visibility = View.VISIBLE
        }
    }

    override fun stopLoadingState() {
        runOnUiThread {
            pbTaskMonitoringSpinner.visibility = View.GONE
        }
    }

    override fun showNewTask(augmentedTask: AugmentedTask) {
        runOnUiThread {
            activityName.text = augmentedTask.activityName
            lifeParametersLinearLayout.removeAllViews()
            createNewTable(augmentedTask.linkedParameters)
            btnEndOperation.isEnabled = true
        }
    }

    override fun showEmptyTask() {
        runOnUiThread {
            activityName.text = "In attesa..."
            lifeParametersLinearLayout.removeAllViews()
            btnEndOperation.isEnabled = false
        }
    }

    override fun showAlarmedTask(notification : Notification) {
        runOnUiThread {
            parametersViews[notification.lifeParameter]!!.first.textColor = Color.RED
            parametersViews[notification.lifeParameter]!!.second.textColor = Color.RED
        }
    }

    override fun updateHealthParameterValues(parameter: LifeParameters, value: Double) {
        runOnUiThread {
            parametersViews[parameter]!!.first.textColor = oldColors.defaultColor
            parametersViews[parameter]!!.second.textColor = oldColors.defaultColor
            parametersViews[parameter]!!.second.setHealthParameterValue(value.toString())
        }
    }

    private fun createNewTable(parameters: List<LifeParameters>) {
        lifeParametersLinearLayout.setBackgroundColor(Color.LTGRAY)

        parameters.forEach {
            //Log.d("Ora sono: ", lifeParameter.toString())

            val customWidth = lifeParametersLinearLayout.width / (parameters.size * 2)

            parametersViews[it]!!.first.height = lifeParametersLinearLayout.height
            parametersViews[it]!!.first.width = customWidth
            parametersViews[it]!!.second.height = lifeParametersLinearLayout.height
            parametersViews[it]!!.second.width = customWidth

//            val paramWrapper = LinearLayout(this)
//            paramWrapper.layoutParams = ViewGroup.LayoutParams(lifeParametersLinearLayout.width/parameters.size, lifeParametersLinearLayout.height)
//            paramWrapper.addView(parametersViews[it]!!.first)
//            paramWrapper.addView(parametersViews[it]!!.second)
//            lifeParametersLinearLayout.addView(paramWrapper, ViewGroup.LayoutParams.MATCH_PARENT)

            lifeParametersLinearLayout.addView(parametersViews[it]!!.first)
            lifeParametersLinearLayout.addView(parametersViews[it]!!.second)

        }
    }

    private fun initializeParametersViews() {
        LifeParameters.values().forEach { parametersViews[it] = createParameterView(it) }
    }

    private fun initializeParametersViews(parameters: List<LifeParameters>) {
        parameters.forEach { parametersViews[it] = createParameterView(it) }
    }

    private fun createParameterView(parameter: LifeParameters): Pair<TextView, TextView> {
        val parameterAcronymView = TextView(this)
        parameterAcronymView.setBackgroundColor(Color.GRAY)
        parameterAcronymView.gravity = Gravity.CENTER
        parameterAcronymView.setTextColor(Color.BLACK)
        parameterAcronymView.textSize = 18.0F
        parameterAcronymView.text = parameter.acronym

        val parameterValueView = TextView(this)
        parameterValueView.setBackgroundColor(Color.WHITE)
        parameterValueView.gravity = Gravity.CENTER
        parameterAcronymView.textSize = 25.0F
        parameterValueView.text = "0"
        parameterValueView.setTextColor(Color.BLACK)

        return Pair(parameterAcronymView, parameterValueView)
    }
}