package com.example.mindsound.spotify;

import android.graphics.Bitmap;
import android.util.Log;

import com.example.mindsound.spotify.models.Playlist;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.protocol.client.CallResult;
import com.spotify.protocol.types.Track;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class SpotifyService {

    private static SpotifyService instance;

    public static final String CLIENT_ID = "f2414b499d0646ef9306ebfe0aa5c511";

    public static final String REDIRECT_URI = "https://mindwave.com/callback/";

    public static final int REQUEST_CODE = 1337;

    private CallResult<Bitmap> albumImage;

    private String artist;

    private String song;

    private String duration;

    private HashMap<PlaylistTitle, String> moodPlaylists = new HashMap<>();

    private SpotifyAppRemote mSpotifyAppRemote;

    private PlaylistTitle current;

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

    public void setCurrent(PlaylistTitle current){
        this.current = current;
    }

    public PlaylistTitle getCurrent(){
        return current;
    }

    public interface PlaylistListener {
        void trackPlayed(Track track);
    }

    public void playPlaylist(PlaylistListener listener){
        current = PlaylistTitle.HAPPY;
        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:" + moodPlaylists.get(current));
        mSpotifyAppRemote.getPlayerApi()
                .subscribeToPlayerState()
                .setEventCallback(playerState -> {
                    final Track track = playerState.track;
                    if (track != null) {
                        Log.d("TITLE", track.name + " by " + track.artist.name);
                        setTrackInfo(track);
                        listener.trackPlayed(track);
                    }
                });
    }

    public void changePlaylist(PlaylistTitle title){
        current = title;
        Log.d("PLAYLIST CHANGED",  moodPlaylists.get(title));

        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:" + moodPlaylists.get(title));
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

    public boolean createPlaylistsBasedOnMood(ArrayList<Playlist> playlists){
        PlaylistTitle[] titles = PlaylistTitle.values();
        for (PlaylistTitle title : titles) {
            for(Playlist playlist : playlists){
                if (playlist.getName().equals(title.name())){
                    moodPlaylists.put(title, playlist.getId());
                }
            }
        }
        return titles.length == moodPlaylists.size();
    }
}
