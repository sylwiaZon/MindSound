package com.example.mindsound.ui.login;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.example.mindsound.MainActivity;
import com.example.mindsound.MyApplication;
import com.example.mindsound.R;
import com.example.mindsound.ui.dashboard.BTDeviceListAdapter;
import com.example.mindsound.ui.dashboard.DashboardViewModel;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import java.util.Set;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;

    private static final int REQUEST_CODE = 1337;

    private static final String CLIENT_ID = "f2414b499d0646ef9306ebfe0aa5c511";

    private static final String REDIRECT_URI = "https://mindwave.com/callback/";

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

    private void loginWithSpotify(){
        AuthorizationRequest.Builder builder =
                new AuthorizationRequest.Builder(CLIENT_ID, AuthorizationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"streaming"});
        builder.setShowDialog(true);
        AuthorizationRequest request = builder.build();
        AuthorizationClient.openLoginActivity(getActivity(), REQUEST_CODE, request);
    }
}