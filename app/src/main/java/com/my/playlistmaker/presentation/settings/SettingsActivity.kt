package com.my.playlistmaker.presentation.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.my.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this, SettingsViewModelFactory(applicationContext))[SettingsViewModel::class.java]

        binding.themeSwitcher.isChecked = viewModel.themeState
        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            viewModel.themeSwitcher(checked)
        }

        binding.backFromSettings.setOnClickListener {
            finish()
        }

        binding.share.setOnClickListener {
            viewModel.share()
        }

        binding.support.setOnClickListener {
            viewModel.support()
        }

        binding.terms.setOnClickListener {
            viewModel.terms()
        }
    }
}