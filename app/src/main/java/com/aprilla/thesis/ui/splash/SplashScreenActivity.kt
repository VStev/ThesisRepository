package com.aprilla.thesis.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aprilla.thesis.MainActivity
import com.aprilla.thesis.ui.help.HelpActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashScreenActivity : AppCompatActivity() {

    private val splashViewModel: SplashScreenViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val status = splashViewModel.getRunStatus()
        status.observe(this) {
            status.removeObservers(this)
            if (it) {
                splashViewModel.setRunStatus(false)
                startActivity(Intent(this, HelpActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}