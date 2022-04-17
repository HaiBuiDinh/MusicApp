package com.aemyfiles.musicapp.External.notification

interface Playable {
    fun onTrackPrevious()
    fun onTrackPlay()
    fun onTrackPause()
    fun onTrackNext()
}