package dev.dextra.newsapp.base

import android.widget.TextView
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import dev.dextra.newsapp.api.model.enums.Category
import dev.dextra.newsapp.api.model.enums.Country
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers.*

fun clickOn(@IdRes viewId: Int): ViewInteraction = onView(withId(viewId)).perform(click())

fun clickOnCountryPopupItem(country: Country): ViewInteraction =
    onData(CoreMatchers.equalTo(country)).inRoot(RootMatchers.isPlatformPopup()).perform(click())

fun checkIfViewIsDisplayed(@IdRes viewId: Int): ViewInteraction =
    onView(withId(viewId)).check(matches(isDisplayed()))

fun checkIfViewNotDisplayed(@IdRes viewId: Int): ViewInteraction =
    onView(withId(viewId)).check(matches(not(isDisplayed())))

fun checkMatchText(text: String): ViewInteraction =
    onView(withChild(withText(text))).check(matches(isDisplayed()))

fun clickOnCategoryPopupItem(category: Category): ViewInteraction =
    onData(CoreMatchers.equalTo(category)).inRoot(RootMatchers.isPlatformPopup()).perform(click())

fun matchToolbarText(@IdRes toolbarId: Int, titleId: String): ViewInteraction =
    onView(allOf(instanceOf(TextView::class.java), withParent(withId(toolbarId))))
        .check(matches(withText(titleId)))

fun <VH : RecyclerView.ViewHolder> clickOnItemByPosition(@IdRes viewId: Int, position: Int)
        : ViewInteraction = onView(withId(viewId))
        .perform(actionOnItemAtPosition<VH>(position, click()))