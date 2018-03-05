package com.crioprecipitati.androidpervasive1718

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.crioprecipitati.androidpervasive1718.model.AugmentedTask
import com.crioprecipitati.androidpervasive1718.model.LifeParameters
import com.crioprecipitati.androidpervasive1718.model.Member
import com.crioprecipitati.androidpervasive1718.model.Status
import com.crioprecipitati.androidpervasive1718.utils.Prefs
import com.crioprecipitati.androidpervasive1718.utils.toJson
import com.crioprecipitati.androidpervasive1718.viewPresenter.member.taskMonitoring.TaskMonitoringActivity
import com.crioprecipitati.androidpervasive1718.viewPresenter.member.taskMonitoring.TaskMonitoringPresenterImpl
import model.PayloadWrapper
import model.TaskAssignment
import model.WSOperations
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*
import kotlin.collections.ArrayList

//TODO vedere se sostituire gli sleep con degli idling
/*
* This test simulate the internal web socket interaction
*
* */
@RunWith(AndroidJUnit4::class)
class TaskMonitoringTest {

    var listTaskWSAdapterMessages: ArrayList<String> = ArrayList<String>()
    var listNotifierWSAdapterMessages: ArrayList<String> = ArrayList<String>()
    var listParamametersRegistered: ArrayList<LifeParameters> = ArrayList<LifeParameters>()

    @Rule
    @JvmField
    var presenter = TaskMonitoringPresenterWithMockedWS(listTaskWSAdapterMessages,
            listNotifierWSAdapterMessages,
            listParamametersRegistered)
    val activity = ActivityTestRule<MockTaskMonitoringActivity>(MockTaskMonitoringActivity::class.java)

    companion object {
        val leaderCF1 = "CFLeader1"
        val patientCF1 = "CFPatient1"

        val taskAssigned = AugmentedTask.defaultAugmentedTask()
    }

    fun createTask(){
//        WSTaskServer()
//        TestTaskWSAdapter.initWS()
//        TestTaskWSAdapter.sendAddTaskMessage()
    }

    @Test
    fun closeTask(){
        Thread.sleep(2500)
        createTask()

        //onView(withId(R.id.etUsername)).perform(clearText()).perform(typeText(leaderCF1)).perform(closeSoftKeyboard())
        //onView(withId(R.id.btnRequestOpenSessions)).perform(click())
        Thread.sleep(10000)
        /*onView(withId(R.id.rvSessionList))
                .check(matches(hasDescendant(withText(containsString(leaderCF1)))))
                .check(matches(not(hasDescendant(withText(containsString(leaderCF2))))))
                .check(RecyclerViewItemCountAssertion(2))
        onView(withId(R.id.etUsername)).perform(clearText()).perform(typeText(leaderCF2)).perform(closeSoftKeyboard())
        onView(withId(R.id.btnRequestOpenSessions)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.rvSessionList))
                .check(matches(hasDescendant(withText(containsString(leaderCF2)))))
                .check(matches(not(hasDescendant(withText(containsString(leaderCF1))))))
                .check(RecyclerViewItemCountAssertion(2))*/
    }

    /*@Test
    fun sessionListByMember(){
        onView(withId(R.id.rbMember)).perform(click())
        onView(withId(R.id.btnRequestOpenSessions)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.rvSessionList))
                .check(matches(hasDescendant(withText(containsString(leaderCF1)))))
                .check(matches(hasDescendant(withText(containsString(leaderCF2)))))
                .check(RecyclerViewItemCountAssertion(4))

    }*/
}


class MockTaskMonitoringActivity : TaskMonitoringActivity() {
    fun setPresenter(presenter: TaskMonitoringPresenterImpl) {
        this.presenter = presenter
    }
}

class TaskMonitoringPresenterWithMockedWS(var listTaskWSAdapterMessages: ArrayList<String>,
                                          var listNotifierWSAdapterMessages: ArrayList<String>,
                                          var listParamametersRegistered: ArrayList<LifeParameters>) : TaskMonitoringPresenterImpl() {

    private val queueAssignedTask = PriorityQueue<TaskAssignment>()
    private var currentAssignedTask: TaskAssignment? = null

    override fun onTaskCompletionRequested() {
        currentAssignedTask?.run {
            this.task.task.statusId = Status.FINISHED.id
            listTaskWSAdapterMessages.add(PayloadWrapper(Prefs.sessionId, WSOperations.CHANGE_TASK_STATUS, this.toJson()).toJson())
            listNotifierWSAdapterMessages.add(PayloadWrapper(Prefs.sessionId, WSOperations.CLOSE, Member(Prefs.userCF).toJson()).toJson())
        }
    }

    private fun updateTheCurrentTask() {
        try {
            currentAssignedTask = queueAssignedTask.remove()
        } catch (ex: NoSuchElementException) {
            currentAssignedTask = null
        }
        currentAssignedTask?.run {
            view?.showNewTask(currentAssignedTask!!.task)

            listParamametersRegistered.clear()
            listParamametersRegistered.addAll(currentAssignedTask!!.task.linkedParameters)
        }
    }
}
