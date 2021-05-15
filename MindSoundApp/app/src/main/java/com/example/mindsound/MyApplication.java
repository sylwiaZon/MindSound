package com.example.mindsound;

import android.app.Application;
import android.bluetooth.BluetoothDevice;

public class MyApplication extends Application {
    private BluetoothDevice bluetoothDevice;

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }
}
