package com.aprilla.thesis.ui.home

import android.animation.ObjectAnimator
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.AdapterView
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
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
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()
    private val handler = Handler()

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
        setClickListener()
        setLayouts()
        return binding.root
    }

    private fun setClickListener() {
        binding.reload.setOnClickListener {
            binding.notFound.visibility = View.GONE
            setLayouts()
        }
        binding.refreshPrompt.setOnClickListener {
            setLayouts()
            binding.refreshPrompt.visibility = View.GONE
            binding.refreshPrompt.alpha = 0f
            triggerReload()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main, menu)
        val searchManager = context?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        searchView.queryHint = resources.getString(R.string.hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                val intent = Intent(activity, SearchResultActivity::class.java)
                intent.putExtra(SearchResultActivity.KEYWORD, query)
                startActivity(intent)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun setLayouts() {
        binding.shimmerLayout.apply {
            startShimmer()
            visibility = View.VISIBLE
        }
        binding.layoutNews.visibility = View.GONE
        val saved = homeViewModel.fetchSaved()
        val data = homeViewModel.fetchItems()
        val rv = binding.rvNews
        val adapter = FeedAdapter()
        binding.spinnerCategory.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val cat = binding.spinnerCategory.selectedItem.toString()
                adapter.filter(cat)
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                return
            }
        }
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
                        Toast.makeText(context, "Berita berhasil disimpan. Anda dapat melihat berita tersimpan pada menu berita tersimpan", Toast.LENGTH_SHORT).show()
                    } else {
                        homeViewModel.deleteItem(article)
                        Toast.makeText(context, "Berita berhasil dihapus.", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onMenuClicked(article: ItemsRSS?, cView: View) {
                val bundle = Bundle()
                bundle.putString("title", article?.title)
                DetectFragment.title = article?.title?:"none"
                findNavController().navigate(R.id.action_nav_home_to_nav_detect, bundle)
            }

            override fun onShareNews(article: ItemsRSS?) {
                val sendText = "Saya menemukan berita ${article?.title} dengan kategori ${article?.category}. Baca sekarang di ${article?.link}"
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, sendText)
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        })

        saved.observe(viewLifecycleOwner) { content ->
            binding.status.text = "Memuat berita"
            when (content.status) {
                Status.SUCCESS -> {
                    content.data?.let { adapter.setSavedData(it) }
                    data.observe(viewLifecycleOwner) { data ->
                        when (data.status) {
                            Status.SUCCESS -> {
                                data.data?.let { adapter.setData(it) }
                                val titles = ArrayList<String>()
                                data.data?.forEach {
                                    titles.add(it.title)
                                }
                                binding.status.text = "Melakukan klasifikasi berita"
                                homeViewModel.batchPredict(titles).observe(viewLifecycleOwner){ predicted ->
                                    when (predicted.status){
                                        Status.SUCCESS -> {
                                            predicted.data?.let { adapter.setCategories(it) }
                                            binding.shimmerLayout.apply {
                                                stopShimmer()
                                                visibility = View.GONE
                                            }
                                            with(rv) {
                                                setAdapter(adapter)
                                                layoutManager = LinearLayoutManager(context)
                                            }
                                            binding.layoutNews.visibility = View.VISIBLE
                                            triggerReload()
                                        }
                                        Status.ERROR -> {
                                            binding.shimmerLayout.apply {
                                                stopShimmer()
                                                visibility = View.GONE
                                            }
                                            binding.notFound.visibility = View.VISIBLE
                                            binding.layoutNews.visibility = View.GONE
                                        }
                                        else -> {
                                        }
                                    }
                                }
                            }
                            Status.ERROR -> {
                                binding.shimmerLayout.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                                binding.notFound.visibility = View.VISIBLE
                                binding.layoutNews.visibility = View.GONE
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
                                data.data?.let { adapter.setData(it) }
                                val titles = ArrayList<String>()
                                data.data?.forEach {
                                    titles.add(it.title)
                                }
                                binding.status.text = "Melakukan klasifikasi berita"
                                homeViewModel.batchPredict(titles).observe(viewLifecycleOwner){ predicteds ->
                                    when (predicteds.status){
                                        Status.SUCCESS -> {
                                            predicteds.data?.let { adapter.setCategories(it) }
                                            binding.shimmerLayout.apply {
                                                stopShimmer()
                                                visibility = View.GONE
                                            }
                                            with(rv) {
                                                setAdapter(adapter)
                                                layoutManager = LinearLayoutManager(context)
                                            }
                                            binding.layoutNews.visibility = View.VISIBLE
                                            triggerReload()
                                        }
                                        Status.ERROR -> {
                                            binding.shimmerLayout.apply {
                                                stopShimmer()
                                                visibility = View.GONE
                                            }
                                            binding.notFound.visibility = View.VISIBLE
                                            binding.layoutNews.visibility = View.GONE
                                        }
                                        else -> {
                                        }
                                    }
                                }
                            }
                            Status.ERROR -> {
                                binding.shimmerLayout.apply {
                                    stopShimmer()
                                    visibility = View.GONE
                                }
                                binding.notFound.visibility = View.VISIBLE
                                binding.layoutNews.visibility = View.GONE
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

    private fun triggerReload(){
        val run = Runnable{
            binding.refreshPrompt.visibility = View.VISIBLE
            playAnimation()
        }
        handler.postDelayed(run, 300000) //300000
    }

    private fun playAnimation(){
        ObjectAnimator.ofFloat(binding.refreshPrompt, View.ALPHA, 1f).setDuration(500).start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        handler.removeCallbacksAndMessages(null)
    }
}