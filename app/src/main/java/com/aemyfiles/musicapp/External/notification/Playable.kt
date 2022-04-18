package com.aemyfiles.musicapp.External.notification

interface Playable {
    fun onTrackPrevious()
    fun onTrackPlay(isPlayNewSong: Boolean)
    fun onTrackPause()
    fun onTrackNext()
    fun onStopService()
}