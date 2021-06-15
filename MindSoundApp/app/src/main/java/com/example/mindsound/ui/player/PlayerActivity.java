package com.example.mindsound.ui.player;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mindsound.R;


public class PlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, PlayerFragment.newInstance())
                    .commitNow();
        }
    }
}