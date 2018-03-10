package com.crioprecipitati.androidpervasive1718

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.action.ViewActions.*
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import com.crioprecipitati.androidpervasive1718.viewPresenter.login.LoginActivity
import org.hamcrest.core.StringContains.containsString
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.hamcrest.CoreMatchers.not
import android.support.test.InstrumentationRegistry
import android.support.test.uiautomator.UiDevice

@RunWith(AndroidJUnit4::class)
class LoginActivityTest {

    @Rule
    @JvmField
    val activity = ActivityTestRule<LoginActivity>(LoginActivity::class.java)
    companion object {
        val leaderCF1 = "CFLeader1"
        val leaderCF2 = "CFLeader2"
        val patientCF1 = "CFPatient1"
        val patientCF2 = "CFPatient2"
    }

    fun createSessions(){
        val mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
        onView(withId(R.id.rbLeader)).perform(click())
        onView(withId(R.id.etUsername)).perform(clearText()).perform(typeText(leaderCF1)).perform(closeSoftKeyboard())
        onView(withId(R.id.etPatient)).perform(clearText()).perform(typeText(patientCF1)).perform(closeSoftKeyboard())
        onView(withId(R.id.btnCreateNewSession)).perform(click())
        Thread.sleep(10000)
        mDevice.pressBack()
        onView(withId(R.id.etPatient)).perform(clearText()).perform(typeText(patientCF2)).perform(closeSoftKeyboard())
        onView(withId(R.id.btnCreateNewSession)).perform(click())
        Thread.sleep(10000)
        mDevice.pressBack()
        onView(withId(R.id.etUsername)).perform(clearText()).perform(typeText(leaderCF2)).perform(closeSoftKeyboard())
        onView(withId(R.id.etPatient)).perform(clearText()).perform(typeText(patientCF1)).perform(closeSoftKeyboard())
        onView(withId(R.id.btnCreateNewSession)).perform(click())
        Thread.sleep(10000)
        mDevice.pressBack()
        onView(withId(R.id.etPatient)).perform(clearText()).perform(typeText(patientCF2)).perform(closeSoftKeyboard())
        onView(withId(R.id.btnCreateNewSession)).perform(click())
        Thread.sleep(10000)
        mDevice.pressBack()
    }

    @Test
    fun sessionListByLeader(){
        createSessions()
        onView(withId(R.id.etUsername)).perform(clearText()).perform(typeText(leaderCF1)).perform(closeSoftKeyboard())
        onView(withId(R.id.btnRequestOpenSessions)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.rvSessionList))
                .check(matches(hasDescendant(withText(containsString(leaderCF1)))))
                .check(matches(not(hasDescendant(withText(containsString(leaderCF2))))))
                .check(RecyclerViewItemCountAssertion(2))
        onView(withId(R.id.etUsername)).perform(clearText()).perform(typeText(leaderCF2)).perform(closeSoftKeyboard())
        onView(withId(R.id.btnRequestOpenSessions)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.rvSessionList))
                .check(matches(hasDescendant(withText(containsString(leaderCF2)))))
                .check(matches(not(hasDescendant(withText(containsString(leaderCF1))))))
                .check(RecyclerViewItemCountAssertion(2))
    }

    @Test
    fun sessionListByMember(){
        onView(withId(R.id.rbMember)).perform(click())
        onView(withId(R.id.btnRequestOpenSessions)).perform(click())
        Thread.sleep(1000)
        onView(withId(R.id.rvSessionList))
            .check(matches(hasDescendant(withText(containsString(leaderCF1)))))
            .check(matches(hasDescendant(withText(containsString(leaderCF2)))))
            .check(RecyclerViewItemCountAssertion(4))

    }



}