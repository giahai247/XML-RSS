package com.giahai.xml

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.IOException
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URI
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

     var url: String = "https://vnexpress.net/rss/tin-moi-nhat.rss"

    lateinit var tvTitle : TextView
    lateinit var recycleView : RecyclerView
    lateinit var getDataAdapter: Adapter
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var arrayNews: MutableList<News>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        val loadingTask = LoadingTask()
        loadingTask.execute(url)

        tvTitle.movementMethod = LinkMovementMethod.getInstance()
        tvTitle.text = Html.fromHtml(resources.getString(R.string.string_with_link))

        imageView.setOnClickListener {
                var intent =  Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://vnexpress.net/"));
                startActivity(intent);
        }
    }

    private fun initViews() {
        tvTitle = findViewById(R.id.tvTitle)
        recycleView = findViewById(R.id.recyclerview)
        linearLayoutManager = LinearLayoutManager(this@MainActivity)
    }

    private fun setRecycleView(news: List<News>) {
        getDataAdapter = Adapter(this@MainActivity, news)
        recycleView.layoutManager = linearLayoutManager
        recycleView.setHasFixedSize(true)
        recycleView.adapter = getDataAdapter
    }

    internal inner class LoadingTask : AsyncTask<String, Long, List<News>>() {
        override  fun doInBackground(vararg strings: String): List<News> {

            arrayNews = ArrayList()

            try {
                val url = java.net.URL(strings[0])
                val httpURLConnection = url.openConnection() as HttpURLConnection
                val inputStream = httpURLConnection.inputStream

                val xmlPullParserFactory = XmlPullParserFactory.newInstance()
                xmlPullParserFactory.isNamespaceAware = false

                val xmlPullParser = xmlPullParserFactory.newPullParser()
                xmlPullParser.setInput(inputStream, "utf-8")

                var evenType = xmlPullParser.eventType
                var news: News? = null

                var text = ""

                while (evenType != XmlPullParser.END_DOCUMENT) {
                    val name = xmlPullParser.name
                    when (evenType) {
                        XmlPullParser.START_TAG -> if (name.equals("item",  ignoreCase = true)) {
                            news = News("","","")
                        }
                        XmlPullParser.TEXT -> text = xmlPullParser.text
                        XmlPullParser.END_TAG ->
                            if (news != null && name.equals("title", ignoreCase = true)) {
                                news.title = text
                            } else if (news != null && name.equals("description", ignoreCase = true)) {
                                news.description = text
                            } else if (news != null && name.equals("link", ignoreCase = true)) {
                                news.link = text
                            } else if (name.equals("item", ignoreCase = true)) {
                                if (news != null) {
                                    arrayNews?.add(news)
                                }
                            }
                    }
                    evenType = xmlPullParser.next()
                }
            } catch (e: MalformedURLException) {
                e.printStackTrace()
                Log.e("Exception 1", e.message)
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("Exception 2", e.message)
            } catch (e: XmlPullParserException) {
                e.printStackTrace()
                Log.e("Exception 3", e.message)
            }
            return arrayNews as ArrayList<News>
        }

        override fun onPostExecute(news: List<News>) {
            super.onPostExecute(news)
            setRecycleView(news)
        }
    }

}
