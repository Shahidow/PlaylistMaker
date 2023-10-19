package com.my.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val back = findViewById<Button>(R.id.backFromSettings)
        val share = findViewById<Button>(R.id.share)
        val support = findViewById<Button>(R.id.support)
        val terms= findViewById<Button>(R.id.terms)

        back.setOnClickListener{
            finish()
        }

        share.setOnClickListener{
            val message = getString(R.string.developerUrl)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }

        support.setOnClickListener{
            Intent(Intent.ACTION_SENDTO).apply {
                val message = getString(R.string.supportMsg)
                val subject = getString(R.string.supportSubject)
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf("shahidow@mail.ru"))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, message)
                startActivity(this)
            }
        }

        terms.setOnClickListener{
            val url = getString(R.string.practicumOfferUrl)
            val termsIntent = Intent(Intent.ACTION_VIEW)
            termsIntent.data = Uri.parse(url)
            startActivity(termsIntent)
        }
    }
}