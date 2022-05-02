package com.aprilla.thesis.ui.detect

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.aprilla.thesis.databinding.FragmentDetectBinding
import com.aprilla.thesis.repository.Status

class DetectFragment : Fragment() {

    private var _binding: FragmentDetectBinding? = null
    private val detectViewModel: DetectViewModel by viewModel()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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

    private fun setListeners(){
        binding.buttonPredict.setOnClickListener {
            val query = binding.newsTitle.text.toString()
            if (validation(query)){
                val detect = detectViewModel.predictCategory(query)
                detect.observe(viewLifecycleOwner){ result ->
                    when (result.status){
                        Status.SUCCESS -> {
                            Toast.makeText(context, "Hasilnya adalah ${result.data}", Toast.LENGTH_LONG).show()
                            detect.removeObservers(viewLifecycleOwner)
                        }
                        Status.ERROR -> {
                            Toast.makeText(context, "Gagal melakukan koneksi ke server", Toast.LENGTH_LONG).show()
                            detect.removeObservers(viewLifecycleOwner)
                        }
                        else -> {

                        }
                    }
                }
            }else{
                binding.textError.visibility = View.VISIBLE
            }
        }
    }

    private fun showFeed(category: String){
        val feed = detectViewModel.getFeedFromCategory(category)
        feed.observe(viewLifecycleOwner){

        }
    }

    private fun validation(query: String): Boolean{
        val trimmed = query.trim()
        val size = trimmed.split("\\s+".toRegex()).size
        Log.d("TAG", "validation: $size")
        return size > 5
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}