package com.crioprecipitati.androidpervasive1718

import android.support.v7.widget.RecyclerView
import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.ViewAssertion
import android.view.View
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat


class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {

    override fun check(view : View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }
        val recyclerView = view as RecyclerView
        assertThat(recyclerView.adapter.itemCount, `is`(expectedCount))
    }
}