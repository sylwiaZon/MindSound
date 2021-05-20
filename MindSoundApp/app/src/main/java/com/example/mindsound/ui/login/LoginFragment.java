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
import android.widget.ImageView;
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

import java.util.Set;

public class LoginFragment extends Fragment {

    private LoginViewModel loginViewModel;
    private BluetoothAdapter mBluetoothAdapter;
    private BTDeviceListAdapter deviceListApapter = null;

    private BluetoothDevice mBluetoothDevice;
    private ListView list_select;
    private Dialog selectDialog;
    private String address = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        loginViewModel =
                new ViewModelProvider(this).get(LoginViewModel.class);
        View root = inflater.inflate(R.layout.fragment_log_in, container, false);
        final ImageView select_device = root.findViewById(R.id.button);
        final TextView connection = root.findViewById(R.id.connection);
        deviceListApapter = new BTDeviceListAdapter(getActivity());

        loginViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                connection.setText(s);
            }
        });

        select_device.setOnClickListener(arg0 -> scanDevice());

        try {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
                Toast.makeText(
                        getActivity(),
                        "Please enable your Bluetooth and re-run this program !",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Main", "error:" + e.getMessage());
        }
        return root;
    }

    public void scanDevice(){

        if(mBluetoothAdapter.isDiscovering()){
            mBluetoothAdapter.cancelDiscovery();
        }

        setUpDeviceListView();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, filter);

        mBluetoothAdapter.startDiscovery();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                deviceListApapter.addDevice(device);
                deviceListApapter.notifyDataSetChanged();

            }
        }
    };

    private void setUpDeviceListView(){

        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_select_device, null);
        list_select = (ListView) view.findViewById(R.id.list_select);
        selectDialog = new Dialog(getActivity());
        selectDialog.setContentView(view);

        deviceListApapter = new BTDeviceListAdapter(getActivity());
        list_select.setAdapter(deviceListApapter);
        list_select.setOnItemClickListener(selectDeviceItemClickListener);

        selectDialog.setOnCancelListener(new DialogInterface.OnCancelListener(){

            @Override
            public void onCancel(DialogInterface arg0) {
                getActivity().unregisterReceiver(mReceiver);
            }

        });

        selectDialog.show();

        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        for(BluetoothDevice device: pairedDevices){
            deviceListApapter.addDevice(device);
        }
        deviceListApapter.notifyDataSetChanged();
    }

    private AdapterView.OnItemClickListener selectDeviceItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
            getActivity().unregisterReceiver(mReceiver);

            mBluetoothDevice = deviceListApapter.getDevice(arg2);
            selectDialog.dismiss();
            selectDialog = null;

            address = mBluetoothDevice.getAddress().toString();
            ((MyApplication) getActivity().getApplication()).setBluetoothDevice(mBluetoothDevice);

            BluetoothDevice remoteDevice = mBluetoothAdapter.getRemoteDevice(mBluetoothDevice.getAddress().toString());
            loginViewModel.setmText(remoteDevice.getName());
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    };
}