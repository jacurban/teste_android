package dev.dextra.newsapp.feature.news

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.dextra.newsapp.R
import dev.dextra.newsapp.api.model.Article
import dev.dextra.newsapp.api.model.Source
import dev.dextra.newsapp.components.LoadPageScrollListener
import dev.dextra.newsapp.feature.news.adapter.ArticleListAdapter
import kotlinx.android.synthetic.main.activity_news.*
import org.koin.android.ext.android.inject

class NewsActivity : AppCompatActivity(), ArticleListAdapter.ArticleListAdapterListener {

    private val newsViewModel: NewsViewModel by inject()
    private var viewAdapter: ArticleListAdapter = ArticleListAdapter(this)
    private var viewManager: RecyclerView.LayoutManager = GridLayoutManager(this, 1)
    private var loading: Dialog? = null

    private val loadPageScrollListener = object :
        LoadPageScrollListener.LoadPageScrollLoadMoreListener {
        override fun onLoadMore(
            currentPage: Int,
            totalItemCount: Int,
            recyclerView: RecyclerView
        ) {
            showLoading()
            newsViewModel.loadNews(currentPage, totalItemCount)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_news)

        (intent?.extras?.getSerializable(NEWS_ACTIVITY_SOURCE) as Source).let { source ->
            title = source.name
            newsViewModel.configureSource(source)
            setupList()
            observeEvents()
            newsViewModel.loadNews()
        }
        super.onCreate(savedInstanceState)
    }

    private fun observeEvents() {
        newsViewModel.articles.observe(this, Observer {
            viewAdapter.apply {
                add(it)
                notifyDataSetChanged()
            }
            hideLoading()
        })
    }

    override fun onClick(article: Article) {
        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(article.url)
        startActivity(i)
    }

    private fun showLoading() {
        if (loading == null) {
            loading = Dialog(this)
            loading?.apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                window?.setBackgroundDrawableResource(android.R.color.transparent)
                setContentView(R.layout.dialog_loading)
            }
        }
        loading?.show()
    }

    private fun hideLoading() = loading?.dismiss()

    private fun setupList() {
        news_list.apply {
            layoutManager = viewManager
            adapter = viewAdapter
            addOnScrollListener(LoadPageScrollListener(loadPageScrollListener))
        }
    }

    companion object {
        const val NEWS_ACTIVITY_SOURCE = "NEWS_ACTIVITY_SOURCE"
    }
}
