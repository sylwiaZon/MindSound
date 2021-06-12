package com.example.mindsound.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mindsound.MainActivity;
import com.example.mindsound.R;
import com.example.mindsound.spotify.SpotifyService;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;

    private final SpotifyService spotifyService = SpotifyService.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);
        View root = inflater.inflate(R.layout.fragment_log_in, container, false);
        final ImageView loginWithSpotify = root.findViewById(R.id.log_in_with_spotify_button);

        loginWithSpotify.setOnClickListener(n -> {
            loginWithSpotify();
        });
        return root;
    }

    private void loginWithSpotify() {
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(SpotifyService.CLIENT_ID)
                        .setRedirectUri(SpotifyService.REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.connect(getActivity(), connectionParams,
                new Connector.ConnectionListener() {

                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        spotifyService.setmSpotifyAppRemote(spotifyAppRemote);
                        Log.d("LOGIN", "Connected!");
                        Intent intentMain = new Intent(getActivity(), MainActivity.class);
                        startActivity(intentMain);
                    }

                    public void onFailure(Throwable throwable) {
                        Log.e("MyActivity", throwable.getMessage(), throwable);
                        Toast.makeText(
                                getActivity(),
                                "Log in failed! Try again.",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}