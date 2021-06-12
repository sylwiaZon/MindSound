package com.example.mindsound.spotify;

import android.util.Log;

import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.types.Track;

public class SpotifyService {

    private static SpotifyService instance;

    public static final String CLIENT_ID = "f2414b499d0646ef9306ebfe0aa5c511";

    public static final String REDIRECT_URI = "https://mindwave.com/callback/";

    private final String examplePlaylist = "45cDCNOuzGklEGOGFfXkZ2";

    private SpotifyAppRemote mSpotifyAppRemote;

    public static SpotifyService getInstance() {
        if (instance == null) {
            instance = new SpotifyService();
        }
        return instance;
    }

    public SpotifyAppRemote getmSpotifyAppRemote() {
        return mSpotifyAppRemote;
    }

    public void setmSpotifyAppRemote(SpotifyAppRemote mSpotifyAppRemote) {
        this.mSpotifyAppRemote = mSpotifyAppRemote;
    }

    public void playPlaylist(){
            // Play a playlist
            getmSpotifyAppRemote().getPlayerApi().play("spotify:playlist:" + examplePlaylist);

            // Subscribe to PlayerState
            mSpotifyAppRemote.getPlayerApi()
                    .subscribeToPlayerState()
                    .setEventCallback(playerState -> {
                        final Track track = playerState.track;
                        if (track != null) {
                            Log.d("TITLE", track.name + " by " + track.artist.name);
                            Log.d("SONG", Long.toString(playerState.playbackPosition));
                        }
                    });
    }

    public void nextSongInPlaylist() {
        getmSpotifyAppRemote().getPlayerApi().skipNext();
    }

    public void previousSongInPlaylist() {
        getmSpotifyAppRemote().getPlayerApi().skipPrevious();
    }

    public void stopSongInPlaylist() {
        getmSpotifyAppRemote().getPlayerApi().pause();
    }

    public void resumeSongInPlaylist() {
        getmSpotifyAppRemote().getPlayerApi().resume();
    }
}
