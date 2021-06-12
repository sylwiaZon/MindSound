package com.example.mindsound.ui.home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.example.mindsound.R;
import com.example.mindsound.spotify.PlaylistService;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    private SharedPreferences msharedPreferences;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        msharedPreferences = getActivity().getSharedPreferences("SPOTIFY", 0);
        Log.d("TOKEN", msharedPreferences.getString("token", ""));
        Log.d("USERID", msharedPreferences.getString("userid", ""));
        return root;
    }
}