package com.example.jaderabbittrade.news

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.jaderabbittrade.R

class NewsAdapter(context: Context, news: MutableList<News>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>()
{
    private var _context: Context
    private var _news: MutableList<News>

    init
    {
        _context = context
        _news = news
    }

    var onItemLongClickListener : ((News) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder
    {
        return NewsViewHolder(LayoutInflater.from(_context).inflate(R.layout.view_news, parent, false))
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int)
    {
        val currentNews = _news[position]

        holder.newsDateTimeTextView.text = _context.getString(R.string.news_datetime_and_author,currentNews.dateTimeInString + "\nBy " + currentNews.author)
        holder.newsTitleTextView.text = currentNews.title

        holder.itemView.setOnClickListener()
        {
            this.onItemLongClickListener?.invoke(currentNews)
        }
    }

    override fun getItemCount(): Int
    {
        return _news.size
    }

    inner class NewsViewHolder(newsView: View): RecyclerView.ViewHolder(newsView)
    {
        var newsDateTimeTextView: TextView
        var newsTitleTextView: TextView

        init
        {
            newsDateTimeTextView = newsView.findViewById(R.id.news_date_time)
            newsTitleTextView = newsView.findViewById(R.id.news_title)
        }
    }
}