package com.aprilla.thesis.ui.detect

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprilla.thesis.R
import com.aprilla.thesis.adapter.DetectAdapter
import com.aprilla.thesis.databinding.FragmentDetectBinding
import com.aprilla.thesis.models.ItemsRSS
import com.aprilla.thesis.repository.Status
import com.aprilla.thesis.ui.details.DetailActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetectFragment : Fragment() {

    private var _binding: FragmentDetectBinding? = null
    private val detectViewModel: DetectViewModel by viewModel()
    private val binding get() = _binding!!
    private var cResult = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString("title", "none")?.let {
            title = it
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }

    override fun onResume() {
        super.onResume()
        if (title != "none") {
            binding.newsTitle.setText(title)
            findNavController(this).clearBackStack(R.id.action_nav_home_to_nav_detect)
            autoDetect(title)
        }else{
            binding.newsTitle.text?.clear()
        }
    }

    private fun setListeners(){
        if (title != "none") {
            binding.newsTitle.setText(title)
            autoDetect(title)
        }
        binding.buttonPredict.setOnClickListener {
            detect()
        }
    }

    private fun detect(){
        val query = binding.newsTitle.text.toString()
        binding.loadingbar.visibility = View.VISIBLE
        binding.textError.visibility = View.GONE
        binding.textHeaderPredict.visibility = View.GONE
        binding.textPredicted.visibility = View.GONE
        binding.rvNews.visibility = View.GONE
        if (validation(query)){
            val detect = detectViewModel.predictCategory(query)
            detect.observe(viewLifecycleOwner){ result ->
                when (result.status){
                    Status.SUCCESS -> {
                        cResult = result.data.toString()
                        result.data?.let { it1 -> setupResultView(it1) }
                        binding.loadingbar.visibility = View.GONE
                        detect.removeObservers(viewLifecycleOwner)
                    }
                    Status.ERROR -> {
                        Toast.makeText(context, "Gagal melakukan koneksi ke server", Toast.LENGTH_LONG).show()
                        binding.loadingbar.visibility = View.GONE
                        detect.removeObservers(viewLifecycleOwner)
                    }
                    else -> {
                    }
                }
            }
        }else{
            binding.textError.visibility = View.VISIBLE
            binding.loadingbar.visibility = View.GONE
        }
    }

    private fun autoDetect(query: String){
        binding.loadingbar.visibility = View.VISIBLE
        val detect = detectViewModel.predictCategory(query)
        detect.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    cResult = result.data.toString()
                    result.data?.let { it1 -> setupResultView(it1) }
                    binding.loadingbar.visibility = View.GONE
                    detect.removeObservers(viewLifecycleOwner)
                }
                Status.ERROR -> {
                    Toast.makeText(context, "Gagal melakukan koneksi ke server", Toast.LENGTH_LONG)
                        .show()
                    binding.loadingbar.visibility = View.GONE
                    detect.removeObservers(viewLifecycleOwner)
                }
                else -> {
                }
            }
        }
    }

    private fun showFeed(category: String){
        val saved = detectViewModel.fetchItems()
        val data = detectViewModel.getFeedFromCategory(category)
        val rv = binding.rvNews
        val adapter = DetectAdapter()
        adapter.setOnItemClickCallback(object : DetectAdapter.OnItemClickCallback {
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
                        detectViewModel.saveItem(article)
                    } else {
                        detectViewModel.deleteItem(article)
                    }
                }
            }

            override fun onMenuClicked(article: ItemsRSS?, cView: View) {
                if (article != null) {
                    title = article.title
                }
                binding.newsTitle.text?.clear()
                binding.newsTitle.setText(title)
                autoDetect(title)
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

        saved.observe(viewLifecycleOwner) { content ->
            when (content.status) {
                Status.SUCCESS -> {
                    content.data?.let { adapter.setSavedData(it) }
                    data.observe(viewLifecycleOwner) { data ->
                        when (data.status) {
                            Status.SUCCESS -> {
                                data.data?.let { adapter.setData(it, cResult) }
                                with(rv) {
                                    visibility = View.VISIBLE
                                    setAdapter(adapter)
                                    layoutManager = LinearLayoutManager(context)
                                }
                            }
                            Status.ERROR -> {
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
                                data.data?.let { adapter.setData(it, cResult) }
                                with(rv) {
                                    visibility = View.VISIBLE
                                    setAdapter(adapter)
                                    layoutManager = LinearLayoutManager(context)
                                }
                            }
                            Status.ERROR -> {
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

    private fun setupResultView(category: String){
        binding.textPredicted.apply{
            text = getString(R.string.hasil_prediksi_adalah_kategori_s, category)
            visibility = View.VISIBLE
        }
        binding.textHeaderPredict.apply{
            visibility = View.VISIBLE
        }
        showFeed(category)
    }

    private fun validation(query: String): Boolean{
        val trimmed = query.trim()
        val size = trimmed.split("\\s+".toRegex()).size
        return size > 5
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        var title: String = "none"
    }
}