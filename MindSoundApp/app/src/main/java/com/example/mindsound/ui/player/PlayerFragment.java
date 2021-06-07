package com.example.mindsound.ui.player;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mindsound.R;

public class PlayerFragment extends Fragment {

    private TextView moodText;
    private TextView appreciationText;
    private ImageView moodIcon;
    private ImageView appreciationIcon;
    private ImageView cover;
    private TextView authorText;
    private TextView songNameText;
    private TextView currentTime;
    private TextView wholeTime;
    private ImageView previousSongButton;
    private ImageView nextSongButton;
    private ImageView playSongButton;
    private ImageView pauseSongButton;

    private PlayerViewModel mViewModel;

    public static PlayerFragment newInstance() {
        return new PlayerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_player, container, false);

        moodText = root.findViewById(R.id.mood_text_player);
        moodIcon = root.findViewById(R.id.mood_icon_player);
        appreciationText = root.findViewById(R.id.appreciation_text_player);
        appreciationIcon = root.findViewById(R.id.appreciation_level_player);
        cover = root.findViewById(R.id.song_cover_player);
        authorText = root.findViewById(R.id.author_name_player);
        songNameText = root.findViewById(R.id.song_name_player);
        currentTime = root.findViewById(R.id.current_time_player);
        wholeTime = root.findViewById(R.id.whole_time_player);
        previousSongButton = root.findViewById(R.id.previus_player);
        nextSongButton = root.findViewById(R.id.next_player);
        playSongButton = root.findViewById(R.id.play_player);
        pauseSongButton = root.findViewById(R.id.pause_player);

        return root;
    }

}