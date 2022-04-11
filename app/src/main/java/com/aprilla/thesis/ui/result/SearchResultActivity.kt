package com.aprilla.thesis.ui.result

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprilla.thesis.R
import com.aprilla.thesis.adapter.FeedAdapter
import com.aprilla.thesis.databinding.ActivitySearchResultBinding
import com.aprilla.thesis.models.ItemsRSS
import com.aprilla.thesis.repository.Status
import com.aprilla.thesis.ui.details.DetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.URLEncoder


class SearchResultActivity : AppCompatActivity() {

    private val searchViewModel: SearchViewModel by viewModel()
    private lateinit var binding: ActivitySearchResultBinding
    private var keyword = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        supportActionBar?.title = "Hasil Pencarian"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        keyword = intent.getStringExtra(KEYWORD).toString()
        setLayouts()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        val searchManager = this.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(this.componentName))
        searchView.queryHint = resources.getString(R.string.hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val keyword = URLEncoder.encode(query, "utf-8")
                val intent = Intent(baseContext, SearchResultActivity::class.java)
                intent.putExtra(KEYWORD, keyword)
                startActivity(intent)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setLayouts() {
        Log.d("TAG", "setLayouts: $keyword")
        binding.shimmerLayout.visibility = View.VISIBLE
        val data = searchViewModel.fetchItems()
        val searched = searchViewModel.fetchSearched(keyword)
        val rv = binding.rvNews
        val adapter = FeedAdapter()
        adapter.setOnItemClickCallback(object : FeedAdapter.OnItemClickCallback {
            override fun onItemClicked(article: ItemsRSS?) {
                if (article != null) {
                    val intent = Intent(baseContext, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.NEWS_URL, article.link)
                    startActivity(intent)
                }
            }

            override fun onItemSave(article: ItemsRSS?, position: Int) {
                if (article != null) {
                    searchViewModel.setFavourite(article.link, article.favourite)
                    adapter.notifyItemChanged(position)
                }
            }

        })
        data.observe(this) { content ->
            when (content.status) {
                Status.SUCCESS -> {
                    searched.observe(this){ result ->
                        when (result.status){
                            Status.SUCCESS -> {
                                Toast.makeText(this, "Loaded", Toast.LENGTH_SHORT).show()
                                binding.shimmerLayout.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                                content.data?.let { adapter.setData(it) }
                                with(rv) {
                                    visibility = View.VISIBLE
                                    setAdapter(adapter)
                                    layoutManager = LinearLayoutManager(context)
                                }
                                searched.removeObservers(this)
                            }
                            Status.ERROR -> {
                                binding.shimmerLayout.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                                binding.notFound.visibility = View.VISIBLE
                                binding.rvNews.visibility = View.GONE
                            }
                            else -> {
                            }
                        }
                    }
                    data.removeObservers(this)
                }
                Status.ERROR -> {
                    searched.observe(this){ result ->
                        when (result.status){
                            Status.SUCCESS -> {
                                Toast.makeText(this, "Loaded", Toast.LENGTH_SHORT).show()
                                binding.shimmerLayout.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                                content.data?.let { adapter.setData(it) }
                                with(rv) {
                                    visibility = View.VISIBLE
                                    setAdapter(adapter)
                                    layoutManager = LinearLayoutManager(context)
                                }
                                searched.removeObservers(this)
                            }
                            Status.ERROR -> {
                                binding.shimmerLayout.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                                binding.notFound.visibility = View.VISIBLE
                                binding.rvNews.visibility = View.GONE
                            }
                            else -> {
                            }
                        }
                    }
                    data.removeObservers(this)
                }
                else -> {
                }
            }
        }
    }

    companion object {
        const val KEYWORD = "keyword"
    }
}