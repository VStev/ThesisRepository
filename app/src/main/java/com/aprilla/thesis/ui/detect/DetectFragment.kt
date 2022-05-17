package com.aprilla.thesis.ui.detect

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.aprilla.thesis.R
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.aprilla.thesis.databinding.FragmentDetectBinding
import com.aprilla.thesis.repository.Status

class DetectFragment : Fragment() {

    private var title = "none"
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
                        binding.loadingbar.visibility = View.VISIBLE
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
        binding.newsTitle.setText(title)
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
                    binding.loadingbar.visibility = View.VISIBLE
                    detect.removeObservers(viewLifecycleOwner)
                }
                else -> {
                }
            }
        }
    }

    private fun showFeed(category: String){
        val feed = detectViewModel.getFeedFromCategory(category)
        feed.observe(viewLifecycleOwner){

        }
    }

    private fun setupResultView(category: String){
        binding.textCategoryPredicted.apply{
            text = getString(R.string.hasil, category)
            visibility = View.VISIBLE
        }
        binding.cardRecommendation.visibility = View.VISIBLE
        binding.textHeaderPredict.visibility = View.VISIBLE
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
}