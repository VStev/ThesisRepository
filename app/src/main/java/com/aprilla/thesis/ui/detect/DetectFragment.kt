package com.aprilla.thesis.ui.detect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
        val galleryViewModel =
            ViewModelProvider(this).get(DetectViewModel::class.java)

        _binding = FragmentDetectBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGallery
        galleryViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    private fun validation(query: String): Boolean{
        val trimmed = query.toString().trim()
        val size = trimmed.split("\\s").size
        return size > 5
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}