package dev.dextra.newsapp.feature.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dev.dextra.newsapp.R
import dev.dextra.newsapp.api.model.Article
import kotlinx.android.synthetic.main.item_article.view.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ArticleListAdapter(private val listener: ArticleListAdapterListener) :
    RecyclerView.Adapter<ArticleListAdapter.ViewHolder>() {

    private var dataset: ArrayList<Article> = ArrayList()
    private val dateFormat =
        SimpleDateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT)
    private val parseFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val article = dataset[position]

        holder.itemView.article_name.text = article.title
        holder.itemView.article_description.text = article.description
        holder.itemView.article_author.text = article.author
        holder.itemView.article_date.text =
            dateFormat.format(parseFormat.parse(article.publishedAt))
        holder.itemView.setOnClickListener { listener.onClick(article) }
    }

    class ViewHolder(item: View) : RecyclerView.ViewHolder(item)

    fun add(articles: List<Article>) {
        dataset.addAll(articles)
    }

    fun clear() {
        dataset.clear()
    }

    interface ArticleListAdapterListener {
        fun onClick(article: Article)
    }
}


