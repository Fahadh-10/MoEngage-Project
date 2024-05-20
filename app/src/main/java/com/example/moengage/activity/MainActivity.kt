package com.example.moengage.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.moengage.model.Articles
import com.example.moengage.model.BaseModel
import com.example.moengage.adapter.MainListADTR
import com.example.moengage.helpers.NavigationKey
import com.example.moengage.R
import com.example.moengage.bottomSheetDialog.ViewAllDetailsBSD
import com.example.moengage.databinding.ActivityMainBinding
import com.example.moengage.helpers.API_KEY
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    private lateinit var mainListADTR: MainListADTR
    private lateinit var viewAllDetailsBSD: ViewAllDetailsBSD

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAdapter()
        setUpListeners()
        fetchApiData(API_KEY)
    }

    /**
     * Set up the RecyclerView adapter and layout manager.
     */
    private fun setAdapter() {
        binding.homeRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mainListADTR = MainListADTR()
        binding.homeRV.adapter = mainListADTR
        mainListADTR.setOnClickListeners(object : MainListADTR.OnItemClickListeners {
            override fun onItemClick(position: Int, articleList: Articles) {
                val intent = Intent(this@MainActivity, ArticleWebViewActivity::class.java)
                intent.putExtra(NavigationKey.WEB_URL, articleList.url)
                startActivity(intent)
            }

            override fun onViewMoreClick(position: Int, articleList: Articles) {
                viewAllDetailsBSD =
                    ViewAllDetailsBSD(articleList)
                viewAllDetailsBSD.show(
                    this@MainActivity.supportFragmentManager,
                    viewAllDetailsBSD.tag
                )
            }
        })

    }

    /**
     * Sets up click listener for the clickable actions.
     */
    private fun setUpListeners(){
        binding.filterIV.setOnClickListener { view ->
            showPopupMenu(view)
        }
    }

    /**
     * Handles the response from the API and updates the UI accordingly.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun handleApiResponse(response: String?) {
        val baseResponse = Gson().fromJson(response, BaseModel::class.java).articles
        if (baseResponse != null) {
            binding.progressBar.visibility = GONE
            mainListADTR.articleLists = baseResponse
            mainListADTR.notifyDataSetChanged()
        }
    }

    /**
     * Displays a popup menu for sorting the article list.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.filter_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.ascending -> {
                    mainListADTR.articleLists.sortBy { it.publishedAt }
                    mainListADTR.notifyDataSetChanged()
                    true
                }
                R.id.descending -> {
                    mainListADTR.articleLists.sortByDescending { it.publishedAt }
                    mainListADTR.notifyDataSetChanged()
                    true
                }
                else -> false
            }
        }
        popupMenu.show()
    }

    /**
     * Fetch the data from the API using coroutines for asynchronous network call.
     */
    private fun fetchApiData(apiUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            var response: String?
            var urlConnection: HttpURLConnection? = null
            try {
                val url = URL(apiUrl)
                urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.connectTimeout = 5000
                urlConnection.readTimeout = 5000
                val responseCode = urlConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = urlConnection.inputStream
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val stringBuilder = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        stringBuilder.append(line)
                    }
                    response = stringBuilder.toString()
                    reader.close()
                } else {
                    response = "Error response code: $responseCode"
                }
            } catch (e: Exception) {
                e.printStackTrace()
                response = e.message
            } finally {
                urlConnection?.disconnect()
            }
            withContext(Dispatchers.Main) {
                handleApiResponse(response)
            }
        }
    }
}