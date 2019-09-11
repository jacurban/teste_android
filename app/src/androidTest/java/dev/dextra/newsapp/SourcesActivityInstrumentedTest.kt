package dev.dextra.newsapp

import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import dev.dextra.newsapp.api.model.Source
import dev.dextra.newsapp.api.model.SourceResponse
import dev.dextra.newsapp.api.model.enums.Category
import dev.dextra.newsapp.api.model.enums.Country
import dev.dextra.newsapp.base.BaseInstrumentedTest
import dev.dextra.newsapp.base.FileUtils
import dev.dextra.newsapp.base.TestSuite
import dev.dextra.newsapp.base.mock.endpoint.ResponseHandler
import dev.dextra.newsapp.feature.sources.SourcesActivity
import dev.dextra.newsapp.utils.JsonUtils
import okhttp3.Request
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SourcesActivityInstrumentedTest : BaseInstrumentedTest() {

    val emptyResponse = SourceResponse(ArrayList(), "ok")
    val brazilResponse = SourceResponse(
        listOf(
            Source(
                "cat",
                "BR",
                "Test Brazil Description",
                "1234",
                "PT",
                "Test Brazil",
                "http://www.google.com.br"
            )
        ), "ok"
    )

    @get:Rule
    val activityRule = ActivityTestRule(SourcesActivity::class.java, false, false)

    @Before
    fun setup_test() {
        //we need to lauch the activity here so the MockedEndpointService is set
        activityRule.launchActivity(null)
        Intents.init()
    }

    private fun setupCountrySelectorWithStatesTest() {
        //dynamic mock, BR = customResponse, US = empty response and CA = error response, everything else is the default json
        TestSuite.mock(TestConstants.sourcesURL).body(object : ResponseHandler {
            override fun getResponse(request: Request, path: String): String {
                val jsonData = FileUtils.readJson(path.substring(1) + ".json") ?: ""
                return request.url().queryParameter("country")?.let {
                    when (it) {
                        Country.BR.name.toLowerCase() -> {
                            JsonUtils.toJson(brazilResponse)
                        }
                        Country.US.name.toLowerCase() -> {
                            JsonUtils.toJson(emptyResponse)
                        }
                        Country.CA.name.toLowerCase() -> {
                            throw RuntimeException()
                        }
                        else -> {
                            jsonData
                        }
                    }
                } ?: jsonData
            }
        }).apply()

        waitLoading()
    }

    @Test
    fun test_successful_state() {
        setupCountrySelectorWithStatesTest()

        //select Brazil in the country list
        SourcesRobot().apply {
            clickOnCountry()
            clickOnBrazil()

            waitLoading()

            //check if the Sources list is displayed with the correct item and the empty and error states are hidden
            checkSourceListIsDisplayed()
            checkEmptyStateNotDisplayed()
            checkErrorStateNotDisplayed()
            checkBrazilTextWithDisplayed()
        }
    }

    @Test
    fun test_empty_state() {
        setupCountrySelectorWithStatesTest()

        //select United States in the country list
        SourcesRobot().apply {
            clickOnCountry()
            clickOnUnitedStates()

            waitLoading()

            //check if the empty state is displayed with the correct item and the source list and error state are hidden
            checkEmptyStateIsDisplayed()
            checkSourceListNotDisplayed()
            checkErrorStateNotDisplayed()
        }
    }

    @Test
    fun test_error_state() {
        setupCountrySelectorWithStatesTest()

        //select Canada in the country list
        SourcesRobot().apply {
            clickOnCountry()
            clickOnCanada()

            waitLoading()

            //check if the error state is displayed with the correct item and the source list and empty state are hidden
            checkErrorStateIsDisplayed()
            checkEmptyStateNotDisplayed()
            checkSourceListNotDisplayed()

            //clear the mocks to use just the json files
            TestSuite.clearEndpointMocks()

            //retry in the error state
            clickOnRetryErrorState()

            waitLoading()

            //check if the Sources list is displayed and the empty and error states are hidden
            checkSourceListIsDisplayed()
            checkErrorStateNotDisplayed()
            checkEmptyStateNotDisplayed()
        }
    }

    @Test
    fun test_category_selector_with_states() {
        //dynamic mock, if any category besides ALL is selected, show a custom response
        TestSuite.mock(TestConstants.sourcesURL).body(object : ResponseHandler {
            override fun getResponse(request: Request, path: String): String {
                val jsonData = FileUtils.readJson(path.substring(1) + ".json")!!
                return request.url().queryParameter("category")?.let {
                    if (it == Category.BUSINESS.name.toLowerCase()) JsonUtils.toJson(brazilResponse) else jsonData
                } ?: jsonData
            }
        }).apply()

        waitLoading()

        //select the Business category
        SourcesRobot().apply {
            clickOnCategory()
            clickOnBusiness()

            waitLoading()

            //check if the Sources list is displayed with the correct item and the empty and error states are hidden
            checkSourceListIsDisplayed()
            checkErrorStateNotDisplayed()
            checkEmptyStateNotDisplayed()
            checkBrazilTextWithDisplayed()
        }
    }

    @After
    fun clearTest() {
        Intents.release()
    }
}