package com.my.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Settings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val back = findViewById<Button>(R.id.backFromSettings)
        val share = findViewById<Button>(R.id.share)
        val support = findViewById<Button>(R.id.support)
        val terms= findViewById<Button>(R.id.terms)

        back.setOnClickListener{
            val backIntent = Intent(this, MainActivity::class.java)
            startActivity(backIntent)
        }

        share.setOnClickListener{
            val message = getString(R.string.developerUrl)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }

        support.setOnClickListener{
            val supportIntent = Intent(Intent.ACTION_SENDTO)
            val message = getString(R.string.supportMsg)
            val subject = getString(R.string.supportSubject)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            supportIntent.putExtra(Intent.EXTRA_EMAIL, "shahidow@mail.ru")
            supportIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(supportIntent)
        }

        terms.setOnClickListener{
            val url = getString(R.string.practicumOfferUrl)
            val shareIntent = Intent(Intent.ACTION_VIEW)
            shareIntent.data = Uri.parse(url)
            startActivity(shareIntent)
        }
    }
}