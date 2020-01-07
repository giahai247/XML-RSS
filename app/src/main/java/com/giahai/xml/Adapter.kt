package com.giahai.xml

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.giahai.xml.Adapter.NewsHolder


class Adapter (var context: Context, var news: List<News>) : RecyclerView.Adapter<NewsHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): NewsHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_news, viewGroup, false)
        return NewsHolder(v)
    }

    override fun getItemCount(): Int {
        return if (news == null) 0 else news.size
    }

    override fun onBindViewHolder(newsHolder: NewsHolder, i: Int) {
        newsHolder.tvTitle.text = news[i].title
        newsHolder.tvDescription.text = news[i].description
        newsHolder.itemView.setOnClickListener {
            val intent = Intent(context, WebActivity::class.java)
            intent.putExtra("link", news[i].link)
            context.startActivity(intent)
        }
    }

    class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle : TextView
        val tvDescription : TextView
        init {
            tvTitle = itemView.findViewById(R.id.tvTitle)
            tvDescription = itemView.findViewById(R.id.tvDescription)

        }
    }

}