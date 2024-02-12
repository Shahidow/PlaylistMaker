package com.my.playlistmaker.data

interface Player {
    fun playbackControl()
    fun onCreate(trackURL:String)
    fun onDestroy()
    fun play()
    fun pause()

}