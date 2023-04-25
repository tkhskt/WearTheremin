package com.tkhskt.theremin.infra.bluetooth

import android.annotation.SuppressLint
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
import kotlinx.coroutines.delay
import timber.log.Timber
import java.util.UUID
import kotlin.experimental.and

@SuppressLint("MissingPermission")
class BluetoothClient(
    private val context: Context,
    private val bleManager: BluetoothManager,
) {

    private var connectedDevice: BluetoothDevice? = null

    private var gattSucceeded = false

    private val psdiCharacteristic: BluetoothGattCharacteristic = BluetoothGattCharacteristic(
        UUID_LIFF_PSDI,
        BluetoothGattCharacteristic.PROPERTY_READ,
        BluetoothGattCharacteristic.PERMISSION_READ,
    )
    private val notifyCharacteristic: BluetoothGattCharacteristic = BluetoothGattCharacteristic(
        UUID_LIFF_NOTIFY,
        BluetoothGattCharacteristic.PROPERTY_NOTIFY,
        BluetoothGattCharacteristic.PERMISSION_READ,
    )
    private val btPsdiService: BluetoothGattService =
        BluetoothGattService(UUID_LIFF_PSDI_SERVICE, BluetoothGattService.SERVICE_TYPE_PRIMARY)
    private val btGattService: BluetoothGattService =
        BluetoothGattService(UUID_LIFF_SERVICE, BluetoothGattService.SERVICE_TYPE_PRIMARY)

    private val btGattServer: BluetoothGattServer by lazy {
        bleManager.openGattServer(context, gattServerCallback)
    }

    private val gattServerCallback: BluetoothGattServerCallback =
        object : BluetoothGattServerCallback() {
            private val notifyDescValue = ByteArray(2)
            private val charValue = ByteArray(UUID_LIFF_VALUE_SIZE) /* max 512 */
            override fun onMtuChanged(device: BluetoothDevice, mtu: Int) {
                Timber.d("onMtuChanged($mtu)")
            }

            override fun onConnectionStateChange(
                device: BluetoothDevice,
                status: Int,
                newState: Int,
            ) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    connectedDevice = device
                    Timber.d("STATE_CONNECTED:$device")
                } else {
                    Timber.d("Unknown STATE:$newState")
                }
            }

            override fun onCharacteristicReadRequest(
                device: BluetoothDevice,
                requestId: Int,
                offset: Int,
                characteristic: BluetoothGattCharacteristic,
            ) {
                Timber.d("onCharacteristicReadRequest")
            }

            override fun onCharacteristicWriteRequest(
                device: BluetoothDevice,
                requestId: Int,
                characteristic: BluetoothGattCharacteristic,
                preparedWrite: Boolean,
                responseNeeded: Boolean,
                offset: Int,
                value: ByteArray,
            ) {
                if (characteristic.uuid.compareTo(UUID_LIFF_WRITE) == 0) {
                    if (offset < charValue.size) {
                        var len: Int = value.size
                        if (offset + len > charValue.size) len = charValue.size - offset
                        System.arraycopy(value, 0, charValue, offset, len)
                        btGattServer.sendResponse(
                            device,
                            requestId,
                            BluetoothGatt.GATT_SUCCESS,
                            offset,
                            null,
                        )
                    } else {
                        btGattServer.sendResponse(
                            device,
                            requestId,
                            BluetoothGatt.GATT_FAILURE,
                            offset,
                            null,
                        )
                    }
                    if (notifyDescValue[0] and 0x01.toByte() != 0x00.toByte()) {
                        if (offset == 0 && value[0] == 0xff.toByte()) {
                            notifyCharacteristic.value = "start".toByteArray()
                            btGattServer.notifyCharacteristicChanged(
                                connectedDevice,
                                notifyCharacteristic,
                                false,
                            )
                        }
                    }
                } else {
                    btGattServer.sendResponse(
                        device,
                        requestId,
                        BluetoothGatt.GATT_FAILURE,
                        offset,
                        null,
                    )
                }
            }

            override fun onDescriptorReadRequest(
                device: BluetoothDevice,
                requestId: Int,
                offset: Int,
                descriptor: BluetoothGattDescriptor,
            ) {
                Timber.d("onDescriptorReadRequest")

                if (descriptor.uuid.compareTo(UUID_LIFF_DESC) == 0) {
                    btGattServer.sendResponse(
                        device,
                        requestId,
                        BluetoothGatt.GATT_SUCCESS,
                        offset,
                        notifyDescValue,
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
                value: ByteArray,
            ) {
                if (descriptor.uuid.compareTo(UUID_LIFF_DESC) == 0) {
                    notifyDescValue[0] = value[0]
                    notifyDescValue[1] = value[1]
                    btGattServer.sendResponse(
                        device,
                        requestId,
                        BluetoothGatt.GATT_SUCCESS,
                        offset,
                        null,
                    )
                    gattSucceeded = true
                    btGattServer.notifyCharacteristicChanged(
                        connectedDevice,
                        notifyCharacteristic,
                        false,
                    )
                }
            }
        }

    suspend fun prepare() {
        val btAdvertiser = bleManager.adapter.bluetoothLeAdvertiser
        if (btAdvertiser == null) {
            Timber.w("Cannot use BLE Peripheral mode")
            return
        }

        btPsdiService.addCharacteristic(psdiCharacteristic)

        btGattServer.addService(btPsdiService)
        delay(200)

        btGattService.addCharacteristic(notifyCharacteristic)
        val dataDescriptor = BluetoothGattDescriptor(
            UUID_LIFF_DESC,
            BluetoothGattDescriptor.PERMISSION_WRITE or BluetoothGattDescriptor.PERMISSION_READ
        )
        notifyCharacteristic.addDescriptor(dataDescriptor)
        btGattServer.addService(btGattService)
        delay(200)
        startBleAdvertising(btAdvertiser)
    }

    fun notify(value: String) {
        if (!gattSucceeded) return
        notifyCharacteristic.value = value.toByteArray()
        btGattServer.notifyCharacteristicChanged(
            connectedDevice,
            notifyCharacteristic,
            false,
        )
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
                    Timber.d("onStartSuccess")
                }

                override fun onStartFailure(errorCode: Int) {
                    Timber.d("onStartFailure")
                }
            }
        )
    }

    companion object {
        private const val UUID_LIFF_VALUE_SIZE: Int = 300
        private const val UUID_LIFF_SERVICE_STR: String = "a9d158bb-9007-4fe3-b5d2-d3696a3eb067"

        private val UUID_LIFF_PSDI_SERVICE: UUID =
            UUID.fromString("e625601e-9e55-4597-a598-76018a0d293d")
        private val UUID_LIFF_PSDI: UUID = UUID.fromString("26e2b12b-85f0-4f3f-9fdd-91d114270e6e")

        private val UUID_LIFF_SERVICE: UUID = UUID.fromString(UUID_LIFF_SERVICE_STR)
        private val UUID_LIFF_NOTIFY: UUID = UUID.fromString("52dc2803-7e98-4fc2-908a-66161b5959b0")
        private val UUID_LIFF_WRITE = UUID.fromString("52dc2801-7e98-4fc2-908a-66161b5959b0")
        private val UUID_LIFF_DESC: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    }
}
