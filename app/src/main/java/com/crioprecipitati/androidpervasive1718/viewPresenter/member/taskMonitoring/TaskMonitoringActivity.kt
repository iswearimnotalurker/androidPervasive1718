package com.crioprecipitati.androidpervasive1718.viewPresenter.member.taskMonitoring

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.crioprecipitati.androidpervasive1718.R
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseActivity
import kotlinx.android.synthetic.main.activity_task_monitoring.*
import model.LifeParameters

class TaskMonitoringActivity : BaseActivity<TaskMonitoringContract.TaskMonitoringView, TaskMonitoringContract.TaskMonitoringPresenter>(), TaskMonitoringContract.TaskMonitoringView {

    override var presenter: TaskMonitoringContract.TaskMonitoringPresenter = TaskMonitoringPresenterImpl()
    override val layout: Int = R.layout.activity_task_monitoring

    private val parametersViewer: HashMap<LifeParameters, TextView> = HashMap()
    private var rowSize: Int = 0
    private var colSize: Int = 0

    //private lateinit var sv: ScrollView
    //private lateinit var hsv: HorizontalScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        //sv = ScrollView(this)

        setContentView(R.layout.activity_task_monitoring)
        createNewTable()
        //sv.addView(hsv)

        //clearContent()
        //createNewTable()


    }
    override fun showNewTask() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showEmptyTask() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showAlarmedTask() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateHealthParameterValues() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun clearContent() {
        columns = arrayOf()
        colSize = columns.size
    }

    /*fun setHeaderTitle(tableLayout: TableLayout, rowIndex: Int, columnIndex: Int) {

        // get rows from table with rowIndex
        val tableRow = tableLayout.getChildAt(rowIndex) as TableRow

        // get cell from rows with columnIndex
        val textView = tableRow.getChildAt(columnIndex) as TextView

        textView.setText("Hello")
    }*/

    private fun createNewTable() {
        // 1) Create a tableLayout and its params
        val tableLayoutParams = TableLayout.LayoutParams()
        lifeParametersTable.setBackgroundColor(Color.BLACK)

        // 2) create tableRow params
        val tableRowParams = TableRow.LayoutParams()
        tableRowParams.setMargins(1, 1, 1, 1)
        tableRowParams.weight = 1.0F


        // 3) create tableRow
        val tableRow = TableRow(this)
        tableRow.setBackgroundColor(Color.BLACK)
        //var params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        //params.setMargins(0,0, 0, 0)
        lifeParametersTable.layoutParams

        // 4) create textView


        // 5) add textView to tableRow
        tableRow.addView(createParameterView(LifeParameters.DIASTOLIC_BLOOD_PRESSURE), tableRowParams)
        tableRow.addView(createParameterView(LifeParameters.SYSTOLIC_BLOOD_PRESSURE), tableRowParams)
        tableRow.addView(createParameterView(LifeParameters.HEART_RATE), tableRowParams)


        // 6) add tableRow to tableLayout
        lifeParametersTable.addView(tableRow, tableLayoutParams)

    }

    private fun createParameterView(parameter: LifeParameters): TextView {
        val parameterView = TextView(this)
        //  textView.setText(String.valueOf(j));
        parameterView.setBackgroundColor(Color.WHITE)
        parameterView.gravity = Gravity.CENTER

        parameterView.text = parameter.acronym
        parameterView.setTextColor(Color.CYAN)
        return parameterView
    }
}