package com.tkhskt.theremin

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothGattServer
import android.bluetooth.BluetoothGattServerCallback
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.content.Context
import android.os.ParcelUuid
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.delay
import java.util.UUID
import kotlin.experimental.and

@SuppressLint("MissingPermission")
class ThereminBluetoothManager(
    private val context: Context,
    private val bleManager: BluetoothManager,
    private val bleAdapter: BluetoothAdapter,
) {

    companion object {
        private const val UUID_LIFF_VALUE_SIZE: Int = 500
        private const val UUID_LIFF_SERVICE_STR: String = "a9d158bb-9007-4fe3-b5d2-d3696a3eb067"

        private val UUID_LIFF_PSDI_SERVICE: UUID =
            UUID.fromString("e625601e-9e55-4597-a598-76018a0d293d")
        private val UUID_LIFF_PSDI: UUID = UUID.fromString("26e2b12b-85f0-4f3f-9fdd-91d114270e6e")


        private val UUID_LIFF_SERVICE: UUID = UUID.fromString(UUID_LIFF_SERVICE_STR)
        private val UUID_LIFF_NOTIFY: UUID = UUID.fromString("52dc2803-7e98-4fc2-908a-66161b5959b0")
        private val UUID_LIFF_WRITE = UUID.fromString("52dc2801-7e98-4fc2-908a-66161b5959b0")
        private val UUID_LIFF_DESC: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }

    /*
   BLUETOOTH
    */
    private val mPsdiCharacteristic: BluetoothGattCharacteristic = BluetoothGattCharacteristic(
        UUID_LIFF_PSDI,
        BluetoothGattCharacteristic.PROPERTY_READ,
        BluetoothGattCharacteristic.PERMISSION_READ
    )
    private val mNotifyCharacteristic: BluetoothGattCharacteristic = BluetoothGattCharacteristic(
        UUID_LIFF_NOTIFY,
        BluetoothGattCharacteristic.PROPERTY_NOTIFY,
        BluetoothGattCharacteristic.PERMISSION_READ
    )
    private val btPsdiService: BluetoothGattService =
        BluetoothGattService(UUID_LIFF_PSDI_SERVICE, BluetoothGattService.SERVICE_TYPE_PRIMARY)
    private val btGattService: BluetoothGattService =
        BluetoothGattService(UUID_LIFF_SERVICE, BluetoothGattService.SERVICE_TYPE_PRIMARY)

    private val mBtGattServer: BluetoothGattServer by lazy {
        bleManager.openGattServer(context, mGattServerCallback)
    }
    private var connectedDevice: BluetoothDevice? = null


    suspend fun prepareBle() {
        val btAdvertiser = bleAdapter.bluetoothLeAdvertiser
        if (btAdvertiser == null) {
            Toast.makeText(context, "BLE Peripheralモードが使用できません。", Toast.LENGTH_SHORT).show()
            return
        }

        btPsdiService.addCharacteristic(mPsdiCharacteristic)

        mBtGattServer.addService(btPsdiService)
        delay(200)

        btGattService.addCharacteristic(mNotifyCharacteristic)
        val dataDescriptor = BluetoothGattDescriptor(
            UUID_LIFF_DESC,
            BluetoothGattDescriptor.PERMISSION_WRITE or BluetoothGattDescriptor.PERMISSION_READ
        )
        mNotifyCharacteristic.addDescriptor(dataDescriptor)
        mBtGattServer.addService(btGattService)
        delay(200)
        startBleAdvertising(btAdvertiser)
    }

    private fun startBleAdvertising(advertiser: BluetoothLeAdvertiser) {
        val dataBuilder = AdvertiseData.Builder().apply {
            setIncludeTxPowerLevel(true)
            addServiceUuid(ParcelUuid.fromString(UUID_LIFF_SERVICE_STR))
        }
        val settingsBuilder = AdvertiseSettings.Builder().apply {
            setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_BALANCED)
            setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_MEDIUM)
            setTimeout(0)
            setConnectable(true)
        }
        val respBuilder = AdvertiseData.Builder().apply {
            setIncludeDeviceName(true)
        }
        advertiser.startAdvertising(
            settingsBuilder.build(),
            dataBuilder.build(),
            respBuilder.build(),
            object : AdvertiseCallback() {
                override fun onStartSuccess(settingsInEffect: AdvertiseSettings) {
                    Log.d("bleperi", "onStartSuccess")
                }

                override fun onStartFailure(errorCode: Int) {
                    Log.d("bleperi", "onStartFailure")
                }
            })
    }

    private val mGattServerCallback: BluetoothGattServerCallback =
        object : BluetoothGattServerCallback() {
            private val psdiValue = ByteArray(8)
            private val notifyDescValue = ByteArray(2)
            private val charValue = ByteArray(UUID_LIFF_VALUE_SIZE) /* max 512 */
            override fun onMtuChanged(device: BluetoothDevice, mtu: Int) {
                Log.d("bleperi", "onMtuChanged($mtu)")
            }

            override fun onConnectionStateChange(
                device: BluetoothDevice,
                status: Int,
                newState: Int
            ) {
                Log.d("bleperi", "onConnectionStateChange")
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    connectedDevice = device
                    Log.d("bleperi", "STATE_CONNECTED:$device")
                } else {
                    Log.d("bleperi", "Unknown STATE:$newState")
                }
            }

            override fun onCharacteristicReadRequest(
                device: BluetoothDevice,
                requestId: Int,
                offset: Int,
                characteristic: BluetoothGattCharacteristic
            ) {
//                Log.d("bleperi", "onCharacteristicReadRequest")
//                if (characteristic.uuid.compareTo(UUID_LIFF_PSDI) == 0) {
//                    mBtGattServer!!.sendResponse(
//                        device,
//                        requestId,
//                        BluetoothGatt.GATT_SUCCESS,
//                        offset,
//                        psdiValue
//                    )
//                } else if (characteristic.uuid.compareTo(UUID_LIFF_READ) == 0) {
//                    handler.sendUIMessage(MSG_ID_OBJ_BASE, 1, R.id.txt_access, "Read")
//                    handler.sendUIMessage(
//                        MSG_ID_OBJ_BASE,
//                        1,
//                        R.id.txt_offset,
//                        Integer.toString(offset)
//                    )
//                    handler.sendUIMessage(MSG_ID_OBJ_BASE, 1, R.id.txt_length, "")
//                    handler.sendUIMessage(MSG_ID_OBJ_BASE, 1, R.id.txt_value, "")
//                    if (offset > charValue.size) {
//                        mBtGattServer!!.sendResponse(
//                            device,
//                            requestId,
//                            BluetoothGatt.GATT_FAILURE,
//                            offset,
//                            null
//                        )
//                    } else {
//                        val value = ByteArray(charValue.size - offset)
//                        System.arraycopy(charValue, offset, value, 0, value.size)
//                        mBtGattServer!!.sendResponse(
//                            device,
//                            requestId,
//                            BluetoothGatt.GATT_SUCCESS,
//                            offset,
//                            value
//                        )
//                    }
//                } else {
//                    mBtGattServer!!.sendResponse(
//                        device,
//                        requestId,
//                        BluetoothGatt.GATT_FAILURE,
//                        offset,
//                        null
//                    )
//                }
            }

            override fun onCharacteristicWriteRequest(
                device: BluetoothDevice,
                requestId: Int,
                characteristic: BluetoothGattCharacteristic,
                preparedWrite: Boolean,
                responseNeeded: Boolean,
                offset: Int,
                value: ByteArray
            ) {
                if (characteristic.uuid.compareTo(UUID_LIFF_WRITE) == 0) {
                    if (offset < charValue.size) {
                        var len: Int = value.size
                        if (offset + len > charValue.size) len = charValue.size - offset
                        System.arraycopy(value, 0, charValue, offset, len)
                        mBtGattServer.sendResponse(
                            device,
                            requestId,
                            BluetoothGatt.GATT_SUCCESS,
                            offset,
                            null
                        )
                    } else {
                        mBtGattServer.sendResponse(
                            device,
                            requestId,
                            BluetoothGatt.GATT_FAILURE,
                            offset,
                            null
                        )
                    }
                    if (notifyDescValue[0] and 0x01.toByte() != 0x00.toByte()) {
                        if (offset == 0 && value[0] == 0xff.toByte()) {
                            mNotifyCharacteristic.value = charValue
                            mBtGattServer.notifyCharacteristicChanged(
                                connectedDevice,
                                mNotifyCharacteristic,
                                false
                            )
                            // notified
                        }
                    }
                } else {
                    mBtGattServer.sendResponse(
                        device,
                        requestId,
                        BluetoothGatt.GATT_FAILURE,
                        offset,
                        null
                    )
                }
            }

            override fun onDescriptorReadRequest(
                device: BluetoothDevice,
                requestId: Int,
                offset: Int,
                descriptor: BluetoothGattDescriptor
            ) {
                Log.d("bleperi", "onDescriptorReadRequest");

                if (descriptor.uuid.compareTo(UUID_LIFF_DESC) == 0) {
                    mBtGattServer.sendResponse(
                        device,
                        requestId,
                        BluetoothGatt.GATT_SUCCESS,
                        offset,
                        notifyDescValue
                    )
                }
            }

            override fun onDescriptorWriteRequest(
                device: BluetoothDevice,
                requestId: Int,
                descriptor: BluetoothGattDescriptor,
                preparedWrite: Boolean,
                responseNeeded: Boolean,
                offset: Int,
                value: ByteArray
            ) {

            }
        }

}