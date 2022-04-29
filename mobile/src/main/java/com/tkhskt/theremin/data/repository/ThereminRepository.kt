package com.tkhskt.theremin.data.repository

import com.tkhskt.theremin.data.source.BluetoothClient
import com.tkhskt.theremin.data.model.ThereminParameter

interface ThereminRepository {
    suspend fun initialize()
    suspend fun sendParameter(parameter: ThereminParameter)
}

class ThereminRepositoryImpl(
    private val bluetoothClient: BluetoothClient,
) : ThereminRepository {

    override suspend fun initialize() {
        bluetoothClient.prepare()
    }

    override suspend fun sendParameter(parameter: ThereminParameter) {
        bluetoothClient.notify("${parameter.gravity},${parameter.distance}")
    }
}
