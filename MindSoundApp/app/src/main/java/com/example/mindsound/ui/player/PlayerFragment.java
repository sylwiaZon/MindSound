package com.example.mindsound.ui.player;

import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mindsound.R;
import com.example.mindsound.spotify.PlaylistTitle;
import com.example.mindsound.spotify.SpotifyService;
import com.github.pwittchen.neurosky.library.NeuroSky;
import com.github.pwittchen.neurosky.library.listener.ExtendedDeviceMessageListener;
import com.github.pwittchen.neurosky.library.message.enums.BrainWave;
import com.github.pwittchen.neurosky.library.message.enums.Signal;
import com.github.pwittchen.neurosky.library.message.enums.State;
import com.google.android.material.slider.Slider;
import com.spotify.protocol.types.Track;

import java.math.BigInteger;
import java.util.Locale;
import java.util.Set;

import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

public class PlayerFragment extends Fragment {

    private TextView moodText;
    private TextView attentionLevelText;
    private Slider moodLevel;
    private TextView attentionLevel;
    private ImageView cover;
    private TextView authorText;
    private TextView songNameText;
    private TextView currentTime;
    private TextView wholeTime;
    private ImageView previousSongButton;
    private ImageView nextSongButton;
    private ImageView playSongButton;
    private ImageView pauseSongButton;

    private PlayerViewModel playerViewModel;

    private final SpotifyService spotifyService = SpotifyService.getInstance();

    private BigInteger attentionMeasures;
    private int attentionMeasuresCount;
    private int measuredAttentionLevel;

    public static PlayerFragment newInstance() {
        return new PlayerFragment();
    }
    private NeuroSky neuroSky;
    SharedPreferences sharedPref;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_player, container, false);
        playerViewModel =
                new ViewModelProvider(this).get(PlayerViewModel.class);
        ButterKnife.bind(getActivity());
        spotifyService.playPlaylist(new SpotifyService.PlaylistListener() {
            @Override
            public void trackPlayed(Track track) {
                songNameText.setText(track.name);
            }
        });
        neuroSky = createNeuroSky();
        sharedPref = getActivity().getSharedPreferences("settings", MODE_PRIVATE);
        moodText = root.findViewById(R.id.mood_text_player);
        moodLevel = root.findViewById(R.id.mood_level_player);
        moodLevel.setStepSize(1.0f/6.0f);
        moodLevel.setValueFrom(0);
        moodLevel.setValueTo(1);
        moodLevel.setValue(1);
        attentionLevelText = root.findViewById(R.id.state_text_player);
        attentionLevel = root.findViewById(R.id.state_level_player);
        cover = root.findViewById(R.id.song_cover_player);
        authorText = root.findViewById(R.id.author_name_player);
        songNameText = root.findViewById(R.id.song_name_player);
        currentTime = root.findViewById(R.id.current_time_player);
        wholeTime = root.findViewById(R.id.whole_time_player);
        previousSongButton = root.findViewById(R.id.previus_player);
        previousSongButton.setOnClickListener(el -> {
            spotifyService.previousSongInPlaylist();
            attentionMeasuresCount = 0;
            attentionMeasures= BigInteger.valueOf(0);
        });
        nextSongButton = root.findViewById(R.id.next_player);
        nextSongButton.setOnClickListener(el -> {
            //zmieniamy piosenkę
            changeSong();
            attentionMeasuresCount = 0;
            attentionMeasures= BigInteger.valueOf(0);
        });
        playSongButton = root.findViewById(R.id.play_player);
        playSongButton.setOnClickListener(el -> {
            // pause song
            spotifyService.resumeSongInPlaylist();
            pauseSongButton.setVisibility(View.VISIBLE);
            playSongButton.setVisibility(View.INVISIBLE);
        });
        pauseSongButton = root.findViewById(R.id.pause_player);
        pauseSongButton.setOnClickListener(el -> {
            // play song
            spotifyService.stopSongInPlaylist();
            playSongButton.setVisibility(View.VISIBLE);
            pauseSongButton.setVisibility(View.INVISIBLE);
        });
        playSongButton.setVisibility(View.INVISIBLE);
        pauseSongButton.setVisibility(View.VISIBLE);
        neuroSky.connect();
        neuroSky.startMonitoring();
        attentionMeasures = BigInteger.valueOf(0);
        attentionMeasuresCount = 0;
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        spotifyService.stopSongInPlaylist();
    }

    @Override
    public void onResume() {
        super.onResume();
        spotifyService.resumeSongInPlaylist();
    }

    @NonNull private NeuroSky createNeuroSky() {
        return new NeuroSky(new ExtendedDeviceMessageListener() {
            @Override public void onStateChange(State state) {
                handleStateChange(state);
            }

            @Override public void onSignalChange(Signal signal) {
                handleSignalChange(signal);
            }

            @Override public void onBrainWavesChange(Set<BrainWave> brainWaves) {
                handleBrainWavesChange(brainWaves);
            }
        });
    }

    private void handleStateChange(final State state) {
        if (neuroSky != null && state.equals(State.CONNECTED)) {
            neuroSky.startMonitoring();
        }
    }

    private void handleSignalChange(final Signal signal) {
        switch (signal) {
            case ATTENTION:
                attentionLevel.setText(getFormattedMessage(signal));
                attentionMeasures = attentionMeasures.add(BigInteger.valueOf(signal.getValue()));
                attentionMeasuresCount++;
                break;
            case BLINK:
                // następna piosenka
                String isBlinkEnabled = sharedPref.getString(getString(R.string.blink_detection_preference), "On");
                if(isBlinkEnabled.equals("On") && signal.getValue() > 80) {
                    changeSong();
                }
                Log.d("MindWave", String.format("blink: %d", signal.getValue()));
                break;
        }
    }

    private void handleBrainWavesChange(final Set<BrainWave> brainWaves) {
        for (BrainWave brainWave : brainWaves) {
            Log.d("MindWave", String.format("%s: %d", brainWave.toString(), brainWave.getValue()));
        }
    }

    private String getFormattedMessage(Signal signal) {
        return String.format(Locale.getDefault(),"%d", signal.getValue());
    }

    private void changeSong() {
        PlaylistTitle title;
        String matchMood = sharedPref.getString(getString(R.string.match_mood_preference), "Yes");
        measuredAttentionLevel = attentionMeasures.divide(BigInteger.valueOf(attentionMeasuresCount)).intValue() / 100;

        float moodLevelValue = matchMood.equals("Yes")
                ?  moodLevel.getValue()
                : 1 - moodLevel.getValue();
        if (moodLevelValue < 0.5) {
            title = PlaylistTitle.SAD;
        } else {
            title = PlaylistTitle.HAPPY;
        }

        measuredAttentionLevel = matchMood.equals("Yes")
                ?  measuredAttentionLevel
                : 1 - measuredAttentionLevel;
        if (measuredAttentionLevel > 0.5 && moodLevelValue < 0.5) {
            title = PlaylistTitle.SAD_CONCENTRATION;
        }
        else if(measuredAttentionLevel > 0.5 && moodLevelValue > 0.5){
            title = PlaylistTitle.HAPPY_CONCENTRATION;
        }
        if (spotifyService.getCurrent() == title){
            spotifyService.nextSongInPlaylist();
        }
        Log.d("!!!!!!!!!!!!!!!!!!!!!!!", title.toString());
        spotifyService.changePlaylist(title);
    }
}