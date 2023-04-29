package com.tkhskt.theremin.infra.audio.repositoryimpl

import com.tkhskt.theremin.domain.audio.repository.AudioParameter
import com.tkhskt.theremin.domain.audio.repository.AudioRepository
import com.tkhskt.theremin.infra.bluetooth.BluetoothClient

class AudioRepositoryImpl(
    private val bluetoothClient: BluetoothClient,
) : AudioRepository {

    override suspend fun initialize() {
        bluetoothClient.prepare()
    }

    override suspend fun sendParameter(parameter: AudioParameter) {
        bluetoothClient.notify("${parameter.frequency},${parameter.volume}")
    }
}
