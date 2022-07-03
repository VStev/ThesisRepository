package com.aprilla.thesis.ui.help

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.aprilla.thesis.R
import com.aprilla.thesis.adapter.HelpAdapter
import com.aprilla.thesis.databinding.FragmentHelpBinding
import com.aprilla.thesis.databinding.FragmentHomeBinding

class HelpFragment : Fragment() {

    private var _binding: FragmentHelpBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHelpBinding.inflate(inflater, container, false)
        setLayout()
        return binding.root
    }

    private fun setLayout() {
        val messages = resources.obtainTypedArray(R.array.help_message)
        val images = resources.obtainTypedArray(R.array.drawables)
        val adapter = HelpAdapter()
        adapter.setData(messages, images)
        with (binding.rvHelp){
            this.adapter = adapter
            layoutManager = LinearLayoutManager(context)
        }
    }
}