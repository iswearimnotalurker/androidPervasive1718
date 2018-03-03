package com.crioprecipitati.androidpervasive1718

import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import com.crioprecipitati.androidpervasive1718.networking.webSockets.TestTaskWSAdapter
import com.crioprecipitati.androidpervasive1718.utils.WSTaskServer
import com.crioprecipitati.androidpervasive1718.viewPresenter.member.taskMonitoring.TaskMonitoringActivity

//TODO vedere se sostituire gli sleep con degli idling

@RunWith(AndroidJUnit4::class)
class TaskMonitoringTest {

    @Rule
    @JvmField
    val activity = ActivityTestRule<TaskMonitoringActivity>(TaskMonitoringActivity::class.java)
    companion object {

        val leaderCF1 = "CFLeader1"
        val leaderCF2 = "CFLeader2"
        val patientCF1 = "CFPatient1"
        val patientCF2 = "CFPatient2"

    }

    fun createTask(){
        WSTaskServer()
        TestTaskWSAdapter.initWS()
        TestTaskWSAdapter.sendAddTaskMessage()
    }

    @Test
    fun closeTask(){
        Thread.sleep(2500)
        createTask()
        /*onView(withId(R.id.etUsername)).perform(clearText()).perform(typeText(leaderCF1)).perform(closeSoftKeyboard())
        onView(withId(R.id.btnRequestOpenSessions)).perform(click())*/
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