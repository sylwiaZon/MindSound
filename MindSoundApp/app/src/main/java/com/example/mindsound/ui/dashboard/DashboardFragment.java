package com.example.mindsound.ui.dashboard;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mindsound.MainActivity;
import com.example.mindsound.MyApplication;
import com.example.mindsound.R;
import com.example.mindsound.ui.login.LoginViewModel;
import com.example.mindsound.ui.player.PlayerActivity;

import java.util.Set;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        final LinearLayout startButton = root.findViewById(R.id.lets_start_button);

        startButton.setOnClickListener(el -> {
            Intent intent = new Intent(getActivity(), PlayerActivity.class);
            getActivity().startActivity(intent);
        });
        return root;
    }
}