package dev.dextra.newsapp.feature.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dev.dextra.newsapp.api.model.Article
import dev.dextra.newsapp.api.model.Source
import dev.dextra.newsapp.api.repository.NewsRepository
import dev.dextra.newsapp.base.BaseViewModel
import dev.dextra.newsapp.base.NetworkState

class NewsViewModel(private val newsRepository: NewsRepository) : BaseViewModel() {

    private val _articles = MutableLiveData<List<Article>>()
    val articles: LiveData<List<Article>> get() = _articles

    private val _networkState = MutableLiveData<NetworkState>()
    val networkState: LiveData<NetworkState> get() = _networkState

    private var source: Source? = null

    fun configureSource(source: Source) {
        this.source = source
    }

    fun loadNews() {
        loadNews(FIRST_PAGE, TOTAL_PAGES)
    }

    fun loadNews(page: Int, totalPage: Int) {
        _networkState.postValue(NetworkState.RUNNING)
        addDisposable(
            newsRepository.getEverything(source?.id, page, totalPage).subscribe({
                _articles.postValue(it.articles)
                if (it.articles.isEmpty()) {
                    _networkState.postValue(NetworkState.EMPTY)
                } else {
                    _networkState.postValue(NetworkState.SUCCESS)
                }
            },
                {
                    _networkState.postValue(NetworkState.ERROR)
                }
            ))
    }

    companion object{
        const val FIRST_PAGE = 1
        const val TOTAL_PAGES = 20
    }
}
