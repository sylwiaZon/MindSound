package com.example.mindsound.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mindsound.R;
import com.example.mindsound.spotify.SpotifyService;
import com.example.mindsound.ui.player.PlayerActivity;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final LinearLayout startButton = root.findViewById(R.id.lets_start_button);
        startButton.setOnClickListener(el -> getSpotifyAppRemote());
        return root;
    }

    private void getSpotifyAppRemote(){
        ConnectionParams connectionParams =
                new ConnectionParams.Builder(SpotifyService.CLIENT_ID)
                        .setRedirectUri(SpotifyService.REDIRECT_URI)
                        .showAuthView(true)
                        .build();

        SpotifyAppRemote.disconnect(SpotifyService.getInstance().getmSpotifyAppRemote());
        SpotifyAppRemote.connect(getActivity(), connectionParams,
                new Connector.ConnectionListener() {
                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
                        SpotifyService.getInstance().setmSpotifyAppRemote(spotifyAppRemote);
                        Log.d("SPOTIFY PLAYER", "Connected!");
                        Intent intent = new Intent(getActivity(), PlayerActivity.class);
                        getActivity().startActivity(intent);
                    }

                    public void onFailure(Throwable throwable) {
                        Log.d("SPOTIFY PLAYER", throwable.getMessage(), throwable);
                    }
                });
    }
}