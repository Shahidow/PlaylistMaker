package com.my.playlistmaker.presentation.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.my.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var vm: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vm =
            ViewModelProvider(this, SettingsViewModelFactory(applicationContext))[SettingsViewModel::class.java]

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