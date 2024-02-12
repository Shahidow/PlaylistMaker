package com.my.playlistmaker.presentation.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.my.playlistmaker.databinding.ActivitySettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private val vm by viewModel<SettingsViewModel>()
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.themeSwitcher.isChecked = vm.themeState
        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            vm.themeSwitcher(checked)
        }

        binding.backFromSettings.setOnClickListener {
            finish()
        }

        binding.share.setOnClickListener {
            vm.share()
        }

        binding.support.setOnClickListener {
            vm.support()
        }

        binding.terms.setOnClickListener {
            vm.terms()
        }
    }
}