package com.aprilla.thesis.ui.result

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprilla.thesis.MainActivity
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
        keyword = intent.getStringExtra(KEYWORD).toString()
        binding = ActivitySearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Hasil Pencarian"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
                val intent = Intent(baseContext, SearchResultActivity::class.java)
                intent.putExtra(KEYWORD, query)
                startActivity(intent)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    private fun setLayouts() {
        binding.shimmerLayout.visibility = View.VISIBLE
        val data = searchViewModel.fetchItems()
        val link = URLEncoder.encode(keyword, "utf-8")
        val searched = searchViewModel.fetchSearched(link)
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
                    if (article.favourite) {
                        searchViewModel.saveItem(article)
                        Toast.makeText(applicationContext, "Berita berhasil disimpan. Anda dapat melihat berita tersimpan pada menu berita tersimpan", Toast.LENGTH_SHORT).show()
                    } else {
                        searchViewModel.deleteItem(article)
                        Toast.makeText(applicationContext, "Berita berhasil dihapus.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onMenuClicked(article: ItemsRSS?, cView: View) {
                val intent = Intent(applicationContext, MainActivity::class.java)
                with (intent){
                    putExtra("param", "detect")
                    putExtra("title", article?.title)
                }
                startActivity(intent)
            }

            override fun onShareNews(article: ItemsRSS?) {
                val sendText = "Baca berita ${article?.title} sekarang di ${article?.link}"
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, sendText)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        })
        data.observe(this) { content ->
            when (content.status) {
                Status.SUCCESS -> {
                    content.data?.let { adapter.setSavedData(it) }
                    searched.observe(this){ result ->
                        when (result.status){
                            Status.SUCCESS -> {
                                binding.shimmerLayout.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                                result.data?.let { adapter.setData(it) }
                                with(rv) {
                                    setAdapter(adapter)
                                    visibility = View.VISIBLE
                                    layoutManager = LinearLayoutManager(context)
                                }
                            }
                            Status.ERROR -> {
                                binding.shimmerLayout.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                                binding.notFound.visibility = View.VISIBLE
                                binding.errorMessage.text = getString(R.string.tidak_ditemukan, keyword)
                                binding.rvNews.visibility = View.GONE
                            }
                            else -> {
                            }
                        }
                    }
                    data.removeObservers(this)
                }
                Status.ERROR -> {
                    content.data?.let { adapter.setSavedData(it) }
                    searched.observe(this){ result ->
                        when (result.status){
                            Status.SUCCESS -> {
                                binding.shimmerLayout.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                                result.data?.let { adapter.setData(it) }
                                with(rv) {
                                    setAdapter(adapter)
                                    visibility = View.VISIBLE
                                    layoutManager = LinearLayoutManager(context)
                                }
                            }
                            Status.ERROR -> {
                                binding.shimmerLayout.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                                binding.notFound.visibility = View.VISIBLE
                                binding.errorMessage.text = getString(R.string.tidak_ditemukan, keyword)
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