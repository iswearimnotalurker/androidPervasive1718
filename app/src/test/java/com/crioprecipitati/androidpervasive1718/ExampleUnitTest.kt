package com.crioprecipitati.androidpervasive1718

import org.junit.runner.RunWith
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.contrib.RecyclerViewActions
import android.support.test.espresso.action.ViewActions.click
import android.support.test.espresso.action.ViewActions.typeText
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.espresso.matcher.ViewMatchers.withText
import android.support.test.espresso.matcher.ViewMatchers.hasDescendant
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.runner.AndroidJUnit4
import android.support.test.rule.ActivityTestRule
import com.crioprecipitati.androidpervasive1718.viewPresenter.login.LoginActivity
import org.junit.Rule
import org.junit.Test

@RunWith(AndroidJUnit4::class)
class ApplicationTest {
    @Rule
    @JvmField
    val activity = ActivityTestRule<LoginActivity>(LoginActivity::class.java)


    @Test
    fun startNewSession(){
        val stringToBeTyped = "CFLeader"
        onView(withId(R.id.etUsername)).perform(typeText(stringToBeTyped))
        onView(withId(R.id.btnCreateNewSession)).perform(click())
        onView(withId(R.id.rbMember)).perform(click())
        onView(withId(R.id.btnRequestOpenSessions)).perform(click())
        onView(withId(R.id.rvSessionList))
                .check(matches(hasDescendant(withText(stringToBeTyped))))
    }

}