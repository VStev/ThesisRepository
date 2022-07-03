package com.aprilla.thesis.ui.help

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.aprilla.thesis.MainActivity
import com.aprilla.thesis.R
import com.aprilla.thesis.databinding.ActivityHelpBinding

class HelpActivity : AppCompatActivity() {

    private var state = 0
    private lateinit var binding: ActivityHelpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHelpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setListeners()
    }

    private fun setListeners() {
        val messages = resources.obtainTypedArray(R.array.help_message)
        val images = resources.obtainTypedArray(R.array.drawables)
        binding.buttonNext.setOnClickListener {
            state += 1
            if (state == messages.length()) {
                messages.recycle()
                images.recycle()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }else{
                if (state == 1) binding.buttonPrevious.visibility = View.VISIBLE
                binding.helpImage.setImageResource(images.getResourceId(state, 0))
                binding.helpMessage.text = messages.getString(state)
            }
        }
        binding.buttonPrevious.setOnClickListener {
            state -= 1
            if (state == 0) binding.buttonPrevious.visibility = View.GONE
            binding.helpImage.setImageResource(images.getResourceId(state, 0))
            binding.helpMessage.text = messages.getString(state)
        }
    }


}