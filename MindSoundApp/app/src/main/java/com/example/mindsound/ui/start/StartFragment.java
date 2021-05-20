package com.example.mindsound.ui.start;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mindsound.R;
import com.example.mindsound.ui.home.HomeViewModel;

public class StartFragment extends Fragment {
    private StartViewModel startViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        startViewModel =
                new ViewModelProvider(this).get(StartViewModel.class);
        View root = inflater.inflate(R.layout.fragment_start, container, false);
        final TextView textView = root.findViewById(R.id.text_start);
        startViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
