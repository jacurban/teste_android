package dev.dextra.newsapp

import dev.dextra.newsapp.base.clickOnItemByPosition
import dev.dextra.newsapp.feature.news.adapter.ArticleListAdapter

class NewsRobot {
    fun clickOnArticle() = clickOnItemByPosition<ArticleListAdapter.ViewHolder>(R.id.news_list, 0)
}