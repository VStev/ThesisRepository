package com.aprilla.thesis.ui.home

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprilla.thesis.R
import com.aprilla.thesis.adapter.FeedAdapter
import com.aprilla.thesis.databinding.FragmentHomeBinding
import com.aprilla.thesis.models.ItemsRSS
import com.aprilla.thesis.repository.Status
import com.aprilla.thesis.ui.details.DetailActivity
import com.aprilla.thesis.ui.detect.DetectFragment
import com.aprilla.thesis.ui.result.SearchResultActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.net.URLEncoder


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                    if (article.favourite) {
                        homeViewModel.saveItem(article)
                    } else {
                        homeViewModel.deleteItem(article)
                    }
                }
            }

            override fun onMenuClicked(article: ItemsRSS?, cView: View) {
                val popup = PopupMenu(context, cView)
                val inflater: MenuInflater = popup.menuInflater
                inflater.inflate(R.menu.popup_menu, popup.menu)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.predict_this -> {
                            val bundle = Bundle()
                            bundle.putString("title", article?.title)
//                            view?.let { Navigation.findNavController(it).navigate(R.id.action_nav_home_to_nav_detect, bundle) }
                            findNavController().navigate(R.id.action_nav_home_to_nav_detect, bundle)
                            true
                        } //redirect to fragment detect with title
                        else -> false
                    }
                }
                popup.show()
            }
        })

        saved.observe(viewLifecycleOwner) { content ->
            when (content.status) {
                Status.SUCCESS -> {
                    content.data?.let { adapter.setSavedData(it) }
                    data.observe(viewLifecycleOwner) { data ->
                        when (data.status) {
                            Status.SUCCESS -> {
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
                    data.observe(viewLifecycleOwner) { data ->
                        when (data.status) {
                            Status.SUCCESS -> {
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
                    saved.removeObservers(viewLifecycleOwner)
                }
                else -> {
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}