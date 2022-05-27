package com.aprilla.thesis

import android.content.res.Resources
import android.view.Gravity
import android.view.KeyEvent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.DrawerMatchers.isClosed
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.contrib.NavigationViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.aprilla.thesis.utilities.EspressoIdlingResource
import com.aprilla.thesis.utilities.MyViewAction
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MainInstrumentedTest {
    @get:Rule
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
    }

    @Test
    fun testMainPage() {
        onView(withId(R.id.rv_news)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_news)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                10
            )
        )
    }

    //EITHER RUN AS IS WHEN OFFLINE, OR, USE ROOTED DEVICE THEN UNCOMMENT ALL
    @Test
    fun testMainPageNoInternetThenRetry() {
//        Runtime.getRuntime().exec("\"svc wifi disable\"") //ONLY UNCOMMENT WHEN ROOTED
        onView(withId(R.id.rv_news)).check(matches(not(isDisplayed()))) //ALWAYS RUN THIS
        onView(withId(R.id.not_found)).check(matches(isDisplayed())) //ALWAYS RUN THIS
//        Runtime.getRuntime().exec("\"svc wifi enable\"") //ONLY UNCOMMENT WHEN ROOTED
//        onView(withId(R.id.reload)).perform(click()) //UNCOMMENT WHEN RUNTIME BASH COMMAND IS UNCOMMENTED
//        onView(withId(R.id.rv_news)).check(matches(isDisplayed())) //UNCOMMENT WHEN RUNTIME BASH COMMAND IS UNCOMMENTED
//        onView(withId(R.id.not_found)).check(matches(not(isDisplayed()))) //UNCOMMENT WHEN RUNTIME BASH COMMAND IS UNCOMMENTED
//        onView(withId(R.id.rv_news)).perform(
//            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
//                10
//            )
//        ) //UNCOMMENT WHEN RUNTIME BASH COMMAND IS UNCOMMENTED
    }

    @Test
    fun testMenuOpened() {
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT)))
            .perform(DrawerActions.open())
        onView(withId(R.id.drawer_layout))
            .check(matches(isOpen(Gravity.LEFT)))
    }

    @Test
    fun testSaveThenCheckExist() {
        onView(withId(R.id.rv_news)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_news))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    MyViewAction.clickChildViewWithId(R.id.button_save)
                )
            )
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT)))
            .perform(DrawerActions.open())
        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_saved))
        onView(withId(R.id.rv_news)).check(matches(isDisplayed()))
        onView(withId(R.id.not_found)).check(matches(not(isDisplayed())))
    }

    @Test
    fun deleteThenCheckEmpty() {
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT)))
            .perform(DrawerActions.open())
        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_saved))
        onView(withId(R.id.rv_news)).check(matches(isDisplayed()))
        onView(withId(R.id.not_found)).check(matches(not(isDisplayed())))
        onView(withId(R.id.rv_news))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0, MyViewAction.clickChildViewWithId(R.id.button_delete)
                )
            )
        onView(withId(R.id.not_found)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_news)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testRead() {
        onView(withId(R.id.rv_news)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_news)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                10
            )
        )
        onView(withId(R.id.rv_news)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                10, click()
            )
        )
        onView(withId(R.id.webview_news)).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchFound() {
        onView(withId(R.id.app_bar_search)).perform(click())
        onView(
            withId(
                Resources.getSystem().getIdentifier(
                    "search_src_text",
                    "id", "android"
                )
            )
        ).perform(typeText("tokopedia"))
        onView(withId(R.id.app_bar_search)).perform(pressKey(KeyEvent.KEYCODE_ENTER))
        onView(withId(R.id.rv_news)).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchNotFound() {
        onView(withId(R.id.app_bar_search)).perform(click())
        onView(
            withId(
                Resources.getSystem().getIdentifier(
                    "search_src_text",
                    "id", "android"
                )
            )
        ).perform(typeText("black desert"))
        onView(withId(R.id.app_bar_search)).perform(pressKey(KeyEvent.KEYCODE_ENTER))
        onView(withId(R.id.not_found)).check(matches(isDisplayed()))
    }

    @Test
    fun testOpenPredictNoText() {
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT)))
            .perform(DrawerActions.open())
        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_detect))
        onView(withId(R.id.news_title)).check(matches(withText("")))
        onView(withId(R.id.text_header_predict)).check(matches(not(isDisplayed())))
    }

    @Test
    fun testPredictInvalidTitle() {
        onView(withId(R.id.drawer_layout))
            .check(matches(isClosed(Gravity.LEFT)))
            .perform(DrawerActions.open())
        onView(withId(R.id.nav_view))
            .perform(NavigationViewActions.navigateTo(R.id.nav_detect))
        onView(withId(R.id.news_title)).check(matches(withText("")))
        onView(withId(R.id.news_title)).perform(typeText("black desert"))
        onView(withId(R.id.button_predict)).perform(click())
        onView(withId(R.id.text_header_predict)).check(matches(not(isDisplayed())))
        onView(withId(R.id.text_error)).check(matches(isDisplayed()))
    }

    @Test
    fun openPredictFromPopUp() {
        onView(withId(R.id.rv_news)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_news))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    1,
                    MyViewAction.clickChildViewWithId(R.id.button_menu)
                )
            )
        onView(withId(R.id.news_title)).check(matches(not(withText(""))))
        onView(withId(R.id.text_header_predict)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_news)).check(matches(isDisplayed()))
    }

    @After
    fun teardown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
    }

}