package com.tkhskt.theremin.domain.audio.repository

interface AudioRepository {
    suspend fun initialize()
    suspend fun sendParameter(parameter: ThereminParameter)
}

class AudioRepositoryImpl(
    private val bluetoothClient: BluetoothClient,
) : AudioRepository {

    override suspend fun initialize() {
        bluetoothClient.prepare()
    }

    override suspend fun sendParameter(parameter: ThereminParameter) {
        bluetoothClient.notify("${parameter.frequency},${parameter.volume}")
    }
}
