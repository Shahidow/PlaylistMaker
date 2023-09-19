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

        val search = findViewById<Button>(R.id.search)
        val library = findViewById<Button>(R.id.library)
        val settings = findViewById<Button>(R.id.settings)

        search.setOnClickListener{
            val searchIntent = Intent(this, Search::class.java)
            startActivity(searchIntent)
        }

        val libraryClickListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                val libraryIntent = Intent(this@MainActivity, Library::class.java)
                startActivity(libraryIntent)
            }
        }
        library.setOnClickListener(libraryClickListener)

        settings.setOnClickListener{
            val settingsIntent = Intent(this, Settings::class.java)
            startActivity(settingsIntent)
        }
    }

}