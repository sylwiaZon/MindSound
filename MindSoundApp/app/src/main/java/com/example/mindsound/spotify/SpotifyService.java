package com.example.mindsound.spotify;

import android.graphics.Bitmap;
import android.util.Log;

import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.types.Track;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SpotifyService {

    private static SpotifyService instance;

    public static final String CLIENT_ID = "f2414b499d0646ef9306ebfe0aa5c511";

    public static final String REDIRECT_URI = "https://mindwave.com/callback/";

    public static final int REQUEST_CODE = 1337;

    private final String examplePlaylist = "45cDCNOuzGklEGOGFfXkZ2";

    private CallResult<Bitmap> albumImage;

    private String artist;

    private String song;

    private String duration;

    private Map<PlaylistTitles, String> playlists;

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
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:" + examplePlaylist);
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("TITLE", track.name + " by " + track.artist.name);
                        setTrackInfo(track);
                    }
                });
    }

    private void setTrackInfo(Track track) {
        artist = track.artist.name;
        song = track.name;
        duration = String.format("%d:%d",
                TimeUnit.MILLISECONDS.toMinutes(track.duration),
                TimeUnit.MILLISECONDS.toSeconds(track.duration) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(track.duration))
        );
        albumImage = mSpotifyAppRemote.getImagesApi().getImage(track.imageUri);
    }

    public void nextSongInPlaylist() {
        mSpotifyAppRemote.getPlayerApi().skipNext();
    }

    public void previousSongInPlaylist() {
        mSpotifyAppRemote.getPlayerApi().skipPrevious();
    }

    public void stopSongInPlaylist() {
        mSpotifyAppRemote.getPlayerApi().pause();
    }

    public void resumeSongInPlaylist() {
        mSpotifyAppRemote.getPlayerApi().resume();
    }

    public void createPlaylistsBasedOnMood(){}


}
