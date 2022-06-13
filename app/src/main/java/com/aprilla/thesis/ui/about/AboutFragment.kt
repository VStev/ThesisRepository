package com.aprilla.thesis.ui.about

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aprilla.thesis.BuildConfig
import com.aprilla.thesis.R
import com.aprilla.thesis.databinding.FragmentAboutBinding
import com.aprilla.thesis.databinding.FragmentSavednewsBinding

class AboutFragment : Fragment() {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appver.text = "App Ver ${BuildConfig.VERSION_NAME}"
    }
}