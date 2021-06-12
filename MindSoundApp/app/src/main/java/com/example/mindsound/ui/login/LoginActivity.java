package com.example.mindsound.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.mindsound.MainActivity;
import com.example.mindsound.R;
import com.example.mindsound.spotify.PlaylistService;
import com.example.mindsound.spotify.SpotifyService;
import com.example.mindsound.spotify.UserService;
import com.example.mindsound.spotify.models.SpotifyUser;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationResponse;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences.Editor editor;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        queue = Volley.newRequestQueue(this);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == SpotifyService.REQUEST_CODE) {
            AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                case TOKEN:
                    editor = getSharedPreferences("SPOTIFY", 0).edit();
                    editor.putString("token", response.getAccessToken());
                    editor.apply();
                    waitForUserInfo();
                    waitForPlaylistInfo();
                    break;
                case ERROR:
                    Toast.makeText(
                            this,
                            "Log in failed! Check if you have Spotify app on your device.",
                            Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(
                            this,
                            "Unknown error.",
                            Toast.LENGTH_LONG).show();
            }
        }
    }

    private void waitForUserInfo() {
        UserService userService = new UserService(queue, this.getSharedPreferences("SPOTIFY", 0));
        userService.get(() -> {
            SpotifyUser user = userService.getUser();
            editor = getSharedPreferences("SPOTIFY", 0).edit();
            editor.putString("userid", user.id);
            Log.d("STARTING", "GOT USER INFORMATION");
            editor.commit();
        });
    }

    private void waitForPlaylistInfo() {
        PlaylistService playlistService = new PlaylistService(queue, this.getSharedPreferences("SPOTIFY", 0));
        playlistService.get(() -> {
            Log.d("STARTING", "GOT PLAYLISTS INFORMATION");
            boolean createdPlaylists = SpotifyService.getInstance().createPlaylistsBasedOnMood(playlistService.getPlaylists());
            if (createdPlaylists){
                Log.d("PLAYLISTS!", "Not enough playlists");
            }
            Intent intentMain = new Intent(this, MainActivity.class);
            startActivity(intentMain);
        });
    }
}