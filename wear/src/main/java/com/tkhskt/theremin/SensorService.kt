package com.tkhskt.theremin

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder

class SensorService : Service() {

    override fun onBind(intent: Intent?): IBinder? {

        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        val sensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                // no-op
            }

            @SuppressLint("SetTextI18n")
            override fun onSensorChanged(event: SensorEvent?) {
                event ?: return
                val x = event.values[0]
                val y = event.values[1]
                val z = event.values[2]
            }
        }, sensor, SensorManager.SENSOR_DELAY_NORMAL)

        return super.onStartCommand(intent, flags, startId)
    }

//    private fun sendAxis(x: Float, y: Float, z: Float) {
//        lifecycleScope.launch {
//            try {
//                val nodes = nodeClient.connectedNodes.await()
//                nodes.map { node ->
//                    async {
//                        messageClient.sendMessage(node.id, MainActivity.AXIS_PATH, x.toString().toByteArray())
//                            .await()
//                    }
//                }.awaitAll()
//
//                Log.d(MainActivity.TAG, "Starting activity requests sent successfully")
//            } catch (cancellationException: CancellationException) {
//                throw cancellationException
//            } catch (exception: Exception) {
//                Log.d(MainActivity.TAG, "Starting activity failed: $exception")
//            }
//        }
//    }

}