package com.loki.ripoti.presentation.news.views

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.loki.ripoti.R
import com.loki.ripoti.data.remote.response.NewsItem
import com.loki.ripoti.databinding.NewsItemLayoutBinding

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    private var newsList = mutableListOf<NewsItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NewsItemLayoutBinding.inflate(inflater, parent, false)
        return NewsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = newsList[position]
        holder.bind(newsItem)
    }

    override fun getItemCount(): Int = newsList.size

    fun setNewsList(newsList: List<NewsItem>) {
        this.newsList = newsList.toMutableList()
        notifyDataSetChanged()
    }

    inner class NewsViewHolder(val binding: NewsItemLayoutBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(newsItem: NewsItem) {
            binding.apply {
                newsTitleTxt.text = newsItem.title
                newsDescTxt.text = newsItem.brief_description

                Glide.with(binding.root).load(newsItem.image_url).into(newsImgIv)

                readmoreTxt.setOnClickListener {

                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse(newsItem.news_url)
                    }

                    if (intent.resolveActivity(itemView.context.packageManager) != null) {
                        itemView.context.startActivity(intent)
                    }
                }
            }
        }
    }
}