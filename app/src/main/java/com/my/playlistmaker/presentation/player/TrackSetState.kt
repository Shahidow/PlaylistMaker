package com.my.playlistmaker.presentation.player

sealed class TrackSetState(val result:Boolean, val playlistName:String) {
    class Negative(playlistName:String): TrackSetState(false, playlistName)
    class Success(playlistName:String): TrackSetState(true, playlistName)
}
