package com.crioprecipitati.androidpervasive1718.viewPresenter.member.taskMonitoring

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.*
import com.crioprecipitati.androidpervasive1718.R
import com.crioprecipitati.androidpervasive1718.viewPresenter.base.BaseActivity

class TaskMonitoringActivity : BaseActivity<TaskMonitoringContract.TaskMonitoringView, TaskMonitoringContract.TaskMonitoringPresenter>(), TaskMonitoringContract.TaskMonitoringView {

    override var presenter: TaskMonitoringContract.TaskMonitoringPresenter = TaskMonitoringPresenterImpl()

    private var rows = arrayOf("ROW1", "ROW2", "Row3", "Row4", "Row 5", "Row 6", "Row 7")
    private var columns = arrayOf("COLUMN1", "COLUMN2", "COLUMN3", "COLUMN4", "COLUMN5", "COLUMN6")
    private var rowSize: Int = 0
    private var colSize: Int = 0

    private lateinit var sv: ScrollView
    private lateinit var tableLayout: TableLayout
    private lateinit var hsv: HorizontalScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Log.d("--", "R-Lenght--$rl   C-Lenght--$cl")

        sv = ScrollView(this)
        tableLayout = findViewById<TableLayout>(R.id.lifeParametersTable)
        hsv = HorizontalScrollView(this)

        hsv.addView(tableLayout)
        sv.addView(hsv)
        setContentView(sv)

        //clearContent()
        createNewTable()


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
        rows = arrayOf()
        columns = arrayOf()
        rowSize = rows.size
        colSize = columns.size
    }

    /*fun setHeaderTitle(tableLayout: TableLayout, rowIndex: Int, columnIndex: Int) {

        // get rows from table with rowIndex
        val tableRow = tableLayout.getChildAt(rowIndex) as TableRow

        // get cell from rows with columnIndex
        val textView = tableRow.getChildAt(columnIndex) as TextView

        textView.setText("Hello")
    }*/

    private fun createNewTable(): TableLayout {
        // 1) Create a tableLayout and its params
        val tableLayoutParams = TableLayout.LayoutParams()
        val tableLayout = TableLayout(this)
        tableLayout.setBackgroundColor(Color.BLACK)

        // 2) create tableRow params
        val tableRowParams = TableRow.LayoutParams()
        tableRowParams.setMargins(1, 1, 1, 1)
        tableRowParams.weight = 1.0F

        for (i in 0 until rowSize) {
            // 3) create tableRow
            val tableRow = TableRow(this)
            tableRow.setBackgroundColor(Color.BLACK)

            for (j in 0 until colSize) {
                // 4) create textView
                val textView = TextView(this)
                //  textView.setText(String.valueOf(j));
                textView.setBackgroundColor(Color.WHITE)
                textView.setGravity(Gravity.CENTER)

                val s1 = Integer.toString(i)
                val s2 = Integer.toString(j)
                val s3 = s1 + s2
                val id = Integer.parseInt(s3)
                Log.d("TAG", "-___>" + id)
                if (i == 0 && j == 0) {
                    textView.setText("0==0")
                } else if (i == 0) {
                    Log.d("TAAG", "set Column Headers")
                    textView.setText(columns[j - 1])
                } else if (j == 0) {
                    Log.d("TAAG", "Set Row Headers")
                    textView.setText(rows[i - 1])
                } else {
                    textView.setText("" + id)
                    // check id=23
                    if (id == 23) {
                        textView.setText("ID=23")

                    }
                }

                // 5) add textView to tableRow
                tableRow.addView(textView, tableRowParams)
            }

            // 6) add tableRow to tableLayout
            tableLayout.addView(tableRow, tableLayoutParams)
        }

        return tableLayout
    }
}