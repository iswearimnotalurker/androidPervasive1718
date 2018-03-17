package com.crioprecipitati.androidpervasive1718.viewPresenter.member.taskMonitoring

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import com.crioprecipitati.androidpervasive1718.R
import com.crioprecipitati.androidpervasive1718.model.AugmentedTask
import com.crioprecipitati.androidpervasive1718.model.LifeParameters
import com.crioprecipitati.androidpervasive1718.utils.setHealthParameterValue
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseActivity
import kotlinx.android.synthetic.main.activity_task_monitoring.*
import model.Notification
import org.jetbrains.anko.textColor

open class TaskMonitoringActivity : BaseActivity<TaskMonitoringContract.TaskMonitoringView, TaskMonitoringContract.TaskMonitoringPresenter>(), TaskMonitoringContract.TaskMonitoringView {

    override var presenter: TaskMonitoringContract.TaskMonitoringPresenter = TaskMonitoringPresenterImpl()
    override val layout: Int = R.layout.activity_task_monitoring

    private val parametersViews: HashMap<LifeParameters, Pair<TextView, TextView>> = HashMap()

    private lateinit var oldColors: ColorStateList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnEndOperation.setOnClickListener { presenter.onTaskCompletionRequested() }

        oldColors = activityName.textColors
    }

    override fun onResume() {
        super.onResume()
        initializeParametersViews()
        showEmptyTask()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            presenter.onTaskCompletionRequested()
            return true
        }
        return super.onKeyDown(keyCode, event)
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

    override fun showAlarmedTask(notification: Notification) {
        runOnUiThread {
            with(parametersViews[notification.lifeParameter]!!) {
                first.textColor = Color.RED
                second.textColor = Color.RED
            }
        }
    }

    override fun updateHealthParameterValues(parameter: LifeParameters, value: Double) {
        runOnUiThread {
            with(parametersViews[parameter]!!) {
                first.textColor = oldColors.defaultColor

                with(second) {
                    textColor = oldColors.defaultColor
                    setHealthParameterValue(value.toString())
                }
            }
        }
    }

    private fun createNewTable(parameters: List<LifeParameters>) {
        lifeParametersLinearLayout.setBackgroundColor(Color.WHITE)

        parameters.forEach {
            val customWidth = lifeParametersLinearLayout.width / (parameters.size * 2)
            with(parametersViews[it]!!) {
                with(first) {
                    height = lifeParametersLinearLayout.height
                    width = customWidth
                }

                with(second) {
                    height = lifeParametersLinearLayout.height
                    width = customWidth
                }
            }

            with(lifeParametersLinearLayout) {
                addView(parametersViews[it]!!.first)
                addView(parametersViews[it]!!.second)
            }

        }
    }

    private fun initializeParametersViews() {
        LifeParameters.values().forEach { parametersViews[it] = createParameterView(it) }
    }

    private fun createParameterView(parameter: LifeParameters): Pair<TextView, TextView> {
        val parameterAcronymView = TextView(this)
        with(parameterAcronymView) {
            setBackgroundColor(Color.LTGRAY)
            gravity = Gravity.CENTER
            setTextColor(Color.BLACK)
            textSize = 20.0F
            text = parameter.acronym
        }

        val parameterValueView = TextView(this)
        with(parameterValueView) {
            setBackgroundColor(Color.WHITE)
            gravity = Gravity.CENTER
            textSize = 26.0F
            text = "0"
            setTextColor(Color.BLACK)
        }

        return Pair(parameterAcronymView, parameterValueView)
    }
}