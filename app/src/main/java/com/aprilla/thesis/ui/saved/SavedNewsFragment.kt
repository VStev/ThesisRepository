package com.aprilla.thesis.ui.saved

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprilla.thesis.adapter.SavedAdapter
import com.aprilla.thesis.databinding.FragmentSavednewsBinding
import com.aprilla.thesis.models.ItemsRSS
import com.aprilla.thesis.repository.Status
import com.aprilla.thesis.ui.details.DetailActivity
import com.aprilla.thesis.utilities.GroupingFilter
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

    override fun onStart() {
        super.onStart()
        binding.spinnerCategory.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                val cat = binding.spinnerCategory.selectedItem.toString()
                when{
                    (cat == "News") -> {
                        savedViewModel.filter(GroupingFilter.CAT_NEWS)
                    }
                    (cat == "Ekonomi") -> {
                        savedViewModel.filter(GroupingFilter.CAT_ECO)
                    }
                    (cat == "Politik") -> {
                        savedViewModel.filter(GroupingFilter.CAT_POL)
                    }
                    (cat == "Kesehatan") -> {
                        savedViewModel.filter(GroupingFilter.CAT_HEALTH)
                    }
                    (cat == "Olahraga") -> {
                        savedViewModel.filter(GroupingFilter.CAT_SPORT)
                    }
                    (cat == "Lainnya") -> {
                        savedViewModel.filter(GroupingFilter.CAT_OTHER)
                    }
                    else -> {
                        savedViewModel.filter(GroupingFilter.CAT_ALL)
                    }
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                return
            }
        }
    }

    private fun setLayouts() {
        binding.shimmerLayout.visibility = View.VISIBLE
        binding.layoutNews.visibility = View.GONE
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
                    savedViewModel.deleteRss(article)
                    if (adapter.updateData(position)) {
                        binding.notFound.visibility = View.VISIBLE
                        binding.layoutNews.visibility = View.GONE
                    }
                }
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
        data.observe(viewLifecycleOwner) { content ->
            when (content.status) {
                Status.SUCCESS -> {
                    binding.shimmerLayout.apply {
                        stopShimmer()
                        visibility = View.GONE
                    }
                    content.data?.let { adapter.setData(it) }
                    with(rv) {
                        setAdapter(adapter)
                        layoutManager = LinearLayoutManager(context)
                    }
                    binding.layoutNews.visibility = View.VISIBLE
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}