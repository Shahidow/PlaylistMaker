package com.my.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.my.playlistmaker.R
import com.my.playlistmaker.data.sharing.SharingRepository

class SharingRepositoryImpl(private val context: Context): SharingRepository {

    override fun share() {
        val message = context.getString(R.string.developerUrl)
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, message)
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(shareIntent)
    }

    override fun support() {
        Intent(Intent.ACTION_SENDTO).apply {
            val message = context.getString(R.string.supportMsg)
            val subject = context.getString(R.string.supportSubject)
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("shahidow@mail.ru"))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, message)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(this)
        }
    }

    override fun terms() {
        val url = context.getString(R.string.practicumOfferUrl)
        val termsIntent = Intent(Intent.ACTION_VIEW)
        termsIntent.data = Uri.parse(url)
        termsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(termsIntent)
    }
}