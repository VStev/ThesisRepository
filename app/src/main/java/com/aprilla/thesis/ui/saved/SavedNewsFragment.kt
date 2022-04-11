package com.aprilla.thesis.ui.saved

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprilla.thesis.adapter.SavedAdapter
import com.aprilla.thesis.databinding.FragmentSavednewsBinding
import com.aprilla.thesis.models.ItemsRSS
import com.aprilla.thesis.repository.Status
import com.aprilla.thesis.ui.details.DetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SavedNewsFragment : Fragment() {

    private var _binding: FragmentSavednewsBinding? = null
    private val binding get() = _binding!!
    private val savedViewModel: SavedNewsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavednewsBinding.inflate(inflater, container, false)
        setLayouts()
        return binding.root
    }

    private fun setLayouts() {
        binding.shimmerLayout.visibility = View.VISIBLE
        val data = savedViewModel.fetchItems()
        val rv = binding.rvNews
        val adapter = SavedAdapter()
        adapter.setOnItemClickCallback(object : SavedAdapter.OnItemClickCallback {
            override fun onItemClicked(article: ItemsRSS?) {
                if (article != null) {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.NEWS_URL, article.link)
                    startActivity(intent)
                }
            }

            override fun onItemSave(article: ItemsRSS?, position: Int) {
                if (article != null) {
//                                if (article.favourite) homeViewModel.saveItem(article) else homeViewModel.deleteItem(
//                                    article
//                                )
//                    savedViewModel.setFavourite(article.link, article.favourite)
                    savedViewModel.deleteRss(article)
                    adapter.notifyItemRemoved(position)
                }
            }

        })
//        val listFav = ArrayList<ItemsRSS>()
//        homeViewModel.fetchSaved().observe(viewLifecycleOwner) {
//            adapter.setSavedData(listFav)
//        }
        data.observe(viewLifecycleOwner) { content ->
            when (content.status) {
                Status.SUCCESS -> {
                    Toast.makeText(context, "Loaded", Toast.LENGTH_SHORT).show()
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
                    data.removeObservers(viewLifecycleOwner)
                }
                Status.ERROR -> {
                    Toast.makeText(
                        context,
                        "Gagal memuat data, ${content.message}",
                        Toast.LENGTH_LONG
                    ).show()
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