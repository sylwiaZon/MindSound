package com.example.mindsound.ui.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.mindsound.R;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

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