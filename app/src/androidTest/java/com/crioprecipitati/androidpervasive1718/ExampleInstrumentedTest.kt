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

@RunWith(AndroidJUnit4::class)
class ApplicationTest {
    @Rule
    @JvmField
    val activity = ActivityTestRule<LoginActivity>(LoginActivity::class.java)


    @Test
    fun startNewSession(){
        val stringToBeTyped = "CFLeader"
        onView(withId(R.id.rbLeader)).perform(click())
        onView(withId(R.id.etUsername)).perform(clearText()).perform(typeText(stringToBeTyped)).perform(closeSoftKeyboard())
        onView(withId(R.id.btnCreateNewSession)).perform(click())
        onView(withId(R.id.rbMember)).perform(click())
        onView(withId(R.id.btnRequestOpenSessions)).perform(click())
        Thread.sleep(4000)
        onView(withId(R.id.rvSessionList))
            .check(matches(hasDescendant(withText(containsString(stringToBeTyped)))))
    }

}