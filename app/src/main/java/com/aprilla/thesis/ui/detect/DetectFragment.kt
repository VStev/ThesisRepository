package com.aprilla.thesis.ui.detect

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aprilla.thesis.databinding.FragmentDetectBinding

class DetectFragment : Fragment() {

    private var _binding: FragmentDetectBinding? = null

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
                Toast.makeText(context, "Input valid, $query", Toast.LENGTH_SHORT).show()
                binding.newsTitle.text?.clear()
            }else{
                Toast.makeText(context, "Input Invalid", Toast.LENGTH_SHORT).show()
            }
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