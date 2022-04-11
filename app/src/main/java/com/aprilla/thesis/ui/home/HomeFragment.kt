package com.aprilla.thesis.ui.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprilla.thesis.R
import com.aprilla.thesis.adapter.FeedAdapter
import com.aprilla.thesis.databinding.FragmentHomeBinding
import com.aprilla.thesis.models.ItemsRSS
import com.aprilla.thesis.repository.Status
import com.aprilla.thesis.ui.details.DetailActivity
import com.aprilla.thesis.ui.result.SearchResultActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.URLEncoder

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        setLayouts()
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main, menu)
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.queryHint = resources.getString(R.string.hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val keyword = URLEncoder.encode(query, "utf-8")
                val intent = Intent(activity, SearchResultActivity::class.java)
                intent.putExtra(SearchResultActivity.KEYWORD, keyword)
                startActivity(intent)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun setLayouts() {
        binding.shimmerLayout.visibility = View.VISIBLE
        val saved = homeViewModel.fetchSaved()
        val data = homeViewModel.fetchItems()
        val rv = binding.rvNews
        val adapter = FeedAdapter()
        adapter.setOnItemClickCallback(object : FeedAdapter.OnItemClickCallback {
            override fun onItemClicked(article: ItemsRSS?) {
                if (article != null) {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.NEWS_URL, article.link)
                    startActivity(intent)
                }
            }

            override fun onItemSave(article: ItemsRSS?, position: Int) {
                if (article != null) {
//                    homeViewModel.setFavourite(article.link, article.favourite)
                    if (article.favourite){
                        homeViewModel.saveItem(article)
                    }else{
                        homeViewModel.deleteItem(article)
                    }
                }
            }

        })

        saved.observe(viewLifecycleOwner) { content ->
            when (content.status) {
                Status.SUCCESS -> {
                    content.data?.let { adapter.setSavedData(it) }
                    data.observe(viewLifecycleOwner){ data ->
                        when (data.status){
                            Status.SUCCESS -> {
//                                Toast.makeText(context, "Loaded", Toast.LENGTH_SHORT).show()
                                binding.shimmerLayout.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                                data.data?.let { adapter.setData(it) }
                                with(rv) {
                                    visibility = View.VISIBLE
                                    setAdapter(adapter)
                                    layoutManager = LinearLayoutManager(context)
                                }
                            }
                            Status.ERROR -> {
                                binding.notFound.visibility = View.VISIBLE
                                binding.rvNews.visibility = View.GONE
                            }
                            else -> {
                            }
                        }
                    }
                    saved.removeObservers(viewLifecycleOwner)
                }
                Status.ERROR -> {
                    content.data?.let { adapter.setSavedData(it) }
                    data.observe(viewLifecycleOwner){ data ->
                        when (data.status){
                            Status.SUCCESS -> {
//                                Toast.makeText(context, "Loaded", Toast.LENGTH_SHORT).show()
                                binding.shimmerLayout.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                                data.data?.let { adapter.setData(it) }
                                with(rv) {
                                    visibility = View.VISIBLE
                                    setAdapter(adapter)
                                    layoutManager = LinearLayoutManager(context)
                                }
                            }
                            Status.ERROR -> {
                                binding.notFound.visibility = View.VISIBLE
                                binding.rvNews.visibility = View.GONE
                            }
                            else -> {
                            }
                        }
                    }
                    saved.removeObservers(viewLifecycleOwner)
                }
                else -> {
                }
            }
        }

//        data.observe(viewLifecycleOwner) { content ->
//            when (content.status) {
//                Status.SUCCESS -> {
//                    Toast.makeText(context, "Loaded", Toast.LENGTH_SHORT).show()
//                    binding.shimmerLayout.apply {
//                        stopShimmer()
//                        visibility = View.GONE
//                    }
//                    content.data?.let { adapter.setData(it) }
//                    with(rv) {
//                        visibility = View.VISIBLE
//                        setAdapter(adapter)
//                        layoutManager = LinearLayoutManager(context)
//                    }
//                    data.removeObservers(viewLifecycleOwner)
//                }
//                Status.ERROR -> {
//                    binding.notFound.visibility = View.VISIBLE
//                    binding.rvNews.visibility = View.GONE
//                }
//                else -> {
//                }
//            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}