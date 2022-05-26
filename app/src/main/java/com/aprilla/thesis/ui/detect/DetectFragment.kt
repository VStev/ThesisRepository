package com.aprilla.thesis.ui.detect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
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

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
        Log.d("TAG", "onResume: $title")
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
        Log.d("TAG", "detect: $query")
        binding.loadingbar.visibility = View.VISIBLE
        if (validation(query)){
            val detect = detectViewModel.predictCategory(query)
            detect.observe(viewLifecycleOwner){ result ->
                when (result.status){
                    Status.SUCCESS -> {
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
        Log.d("TAG", "autoDetect: $query")
        val detect = detectViewModel.predictCategory(query)
        detect.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {
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
//                val popup = PopupMenu(context, cView)
//                val inflater: MenuInflater = popup.menuInflater
//                inflater.inflate(R.menu.popup_menu, popup.menu)
//                popup.setOnMenuItemClickListener { item ->
//                    when (item.itemId) {
//                        R.id.predict_this -> {
//                            if (article != null) {
//                                title = article.title
//                            }
//                            binding.newsTitle.text?.clear()
//                            binding.newsTitle.setText(title)
//                            autoDetect(title)
//                            true
//                        } //redirect to fragment detect with title
//                        else -> false
//                    }
//                }
//                popup.show()
                if (article != null) {
                    title = article.title
                }
                binding.newsTitle.text?.clear()
                binding.newsTitle.setText(title)
                autoDetect(title)
            }
        })

        saved.observe(viewLifecycleOwner) { content ->
            when (content.status) {
                Status.SUCCESS -> {
                    content.data?.let { adapter.setSavedData(it) }
                    data.observe(viewLifecycleOwner) { data ->
                        when (data.status) {
                            Status.SUCCESS -> {
                                data.data?.let { adapter.setData(it) }
                                with(rv) {
                                    visibility = View.VISIBLE
                                    setAdapter(adapter)
                                    layoutManager = LinearLayoutManager(context)
                                }
                            }
                            Status.ERROR -> {
//                                binding.notFound.visibility = View.VISIBLE
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
                                data.data?.let { adapter.setData(it) }
                                with(rv) {
                                    visibility = View.VISIBLE
                                    setAdapter(adapter)
                                    layoutManager = LinearLayoutManager(context)
                                }
                            }
                            Status.ERROR -> {
//                                binding.notFound.visibility = View.VISIBLE
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
        binding.textCategoryPredicted.apply{
            text = getString(R.string.hasil, category)
            visibility = View.VISIBLE
        }
//        binding.cardRecommendation.visibility = View.VISIBLE
        binding.textHeaderPredict.apply{
            text = getString(R.string.hasil_rekomendasi_feed_berita_berdasarkan_prediksi_kategori, category)
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