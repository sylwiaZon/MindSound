package com.example.mindsound.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mindsound.R;

import static android.content.Context.MODE_PRIVATE;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        final TextView blinkText = root.findViewById(R.id.blink_detection_text);
        final TextView matchMoodText = root.findViewById(R.id.match_mood_text);
        final LinearLayout blinkButton = root.findViewById(R.id.blink_detection_button);
        final LinearLayout matchMoodButton = root.findViewById(R.id.match_mood_button);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        blinkText.setText("On");
        editor.putString(getString(R.string.blink_detection_preference), "On");
        blinkButton.setOnClickListener(n -> {
            if (blinkText.getText() == "On") {
                blinkText.setText("Off");
                editor.putString(getString(R.string.blink_detection_preference), "Off");
                editor.commit();
            }
            else {
                blinkText.setText("On");
                editor.putString(getString(R.string.blink_detection_preference), "On");
                editor.commit();
            }
        });
        matchMoodButton.setOnClickListener(n -> {
            if (matchMoodText.getText() == "Yes") {
                matchMoodText.setText("No");
                editor.putString(getString(R.string.match_mood_preference), "No");
                editor.commit();
            }
            else {
                matchMoodText.setText("Yes");
                editor.putString(getString(R.string.match_mood_preference), "Yes");
                editor.commit();
            }
        });
        return root;
    }
}