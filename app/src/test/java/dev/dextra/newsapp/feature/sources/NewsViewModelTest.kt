package dev.dextra.newsapp.feature.sources

import dev.dextra.newsapp.TestConstants
import dev.dextra.newsapp.api.model.ArticlesResponse
import dev.dextra.newsapp.api.model.Source
import dev.dextra.newsapp.base.BaseTest
import dev.dextra.newsapp.base.NetworkState
import dev.dextra.newsapp.base.TestSuite
import dev.dextra.newsapp.feature.news.NewsViewModel
import dev.dextra.newsapp.utils.JsonUtils
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.koin.test.get

class NewsViewModelTest : BaseTest(){

    private val emptyResponse = ArticlesResponse(ArrayList(), "ok", 20)
    private lateinit var viewModel: NewsViewModel

    @Before
    fun setupTest() {
        viewModel = TestSuite.get()
    }

    @Test
    fun testGetNews() {
        viewModel.loadNews()

        assert(viewModel.articles.value?.size == SUCCESS_SIZE)
        assertEquals(NetworkState.SUCCESS, viewModel.networkState.value)

        viewModel.onCleared()

        assert(viewModel.getDisposables().isEmpty())
    }

    @Test
    fun testEmptyNews() {
        TestSuite.mock(TestConstants.newsURL).body(JsonUtils.toJson(emptyResponse)).apply()

        viewModel.loadNews()

        assert(viewModel.articles.value?.size == EMPTY_SIZE)
        assertEquals(NetworkState.EMPTY, viewModel.networkState.value)
    }

    @Test
    fun testErrorNews() {
        TestSuite.mock(TestConstants.newsURL).throwConnectionError().apply()

        viewModel.loadNews()

        assert(viewModel.articles.value == null)
        assertEquals(NetworkState.ERROR, viewModel.networkState.value)
    }

    companion object{
        const val SUCCESS_SIZE = 11
        const val EMPTY_SIZE = 0
    }
}