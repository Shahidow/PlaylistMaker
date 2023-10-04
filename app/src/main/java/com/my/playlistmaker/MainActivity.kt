package com.my.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)

        val searchButton = findViewById<Button>(R.id.search)
        val libraryButton = findViewById<Button>(R.id.library)
        val settingsButton = findViewById<Button>(R.id.settings)

        searchButton.setOnClickListener{
            val searchIntent = Intent(this, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        val libraryClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val libraryIntent = Intent(this@MainActivity, LibraryActivity::class.java)
                startActivity(libraryIntent)
            }
        }
        libraryButton.setOnClickListener(libraryClickListener)

        settingsButton.setOnClickListener{
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }

}