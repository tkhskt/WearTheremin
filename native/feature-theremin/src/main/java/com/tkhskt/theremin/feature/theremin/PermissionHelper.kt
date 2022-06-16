package com.tkhskt.theremin.feature.theremin

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.ContextCompat

val Context.bluetoothPermissionGranted: Boolean
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_CONNECT,
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH_ADVERTISE,
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.BLUETOOTH,
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

fun ActivityResultLauncher<Array<String>>.requestBluetoothPermissions() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        launch(
            arrayOf(
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_ADVERTISE,
            )
        )
    } else {
        launch(arrayOf(Manifest.permission.BLUETOOTH))
    }
}

sealed class PermissionRequestState {
    object NotRequested : PermissionRequestState()
    object Granted : PermissionRequestState()
    object Denied : PermissionRequestState()
}
