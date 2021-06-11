package com.example.mindsound.spotify;

import com.spotify.android.appremote.api.SpotifyAppRemote;

public class SpotifyService {

    public static final String CLIENT_ID = "f2414b499d0646ef9306ebfe0aa5c511";

    public static final String REDIRECT_URI = "https://mindwave.com/callback/";

    private SpotifyAppRemote mSpotifyAppRemote;

    public SpotifyAppRemote getmSpotifyAppRemote() {
        return mSpotifyAppRemote;
    }

    public void setmSpotifyAppRemote(SpotifyAppRemote mSpotifyAppRemote) {
        this.mSpotifyAppRemote = mSpotifyAppRemote;
    }
}
