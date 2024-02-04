package com.my.playlistmaker.data.api.impl

import com.my.playlistmaker.Track
import com.my.playlistmaker.data.api.Response

class TracksSearchResponse(val resultCount: Int,
                           val expression: String,
                           val results: List<Track>) : Response()