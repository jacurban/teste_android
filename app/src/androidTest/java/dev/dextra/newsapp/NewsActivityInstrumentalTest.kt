package dev.dextra.newsapp

import android.content.Intent
import androidx.test.espresso.intent.Intents
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import dev.dextra.newsapp.api.model.Source
import dev.dextra.newsapp.base.BaseInstrumentedTest
import dev.dextra.newsapp.base.TestSuite
import dev.dextra.newsapp.feature.news.NewsActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NewsActivityInstrumentalTest : BaseInstrumentedTest() {

    private val sourceOfArticle = Source(
        "Livros",
        "BR",
        "Test ",
        "1234",
        "PT",
        "Test",
        "https://www.google.com.br"
    )

    @get:Rule
    val activityRule = ActivityTestRule(NewsActivity::class.java, false, false)

    @Before
    fun setup_test() {
        //we need to lauch the activity here so the MockedEndpointService is set
        val intent = Intent()
        intent.putExtra(NewsActivity.NEWS_ACTIVITY_SOURCE, sourceOfArticle)
        activityRule.launchActivity(intent)
        Intents.init()
    }

    private fun mockDataToTest() {
        TestSuite.mock(TestConstants.newsURL).apply()
    }

    @Test
    fun test_click_on_article(){
        mockDataToTest()
        NewsRobot().apply {
            clickOnArticle()
        }
    }
}
