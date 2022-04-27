package com.tkhskt.theremin.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.gms.wearable.Wearable
import com.tkhskt.theremin.MainActivity
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber

// Not Used
class SensorService : Service() {

    private val sensorManager = applicationContext.getSystemService(SENSOR_SERVICE) as SensorManager

    private val messageClient by lazy { Wearable.getMessageClient(applicationContext) }
    private val nodeClient by lazy { Wearable.getNodeClient(applicationContext) }

    private var scope = CoroutineScope(Dispatchers.IO + Job())


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        scope = CoroutineScope(Dispatchers.IO + Job())
        setupNotification()
        startSensor()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun stopService(name: Intent?): Boolean {
        scope.cancel()
        return super.stopService(name)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
        stopSelf()
    }

    private fun setupNotification() {
        val openIntent = Intent(this, MainActivity::class.java).let {
            PendingIntent.getActivity(this, 0, it, 0)
        }
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("send axis data")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(openIntent)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }


    private fun startSensor() {
        val sensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        scope.launch {
            sensorEventFlow(sensor, sensorManager).collect {
                sendAxis(it.values[0])
            }
        }
    }

    private suspend fun sendAxis(x: Float) {
        withContext(Dispatchers.IO) {
            try {
                val nodes = nodeClient.connectedNodes.await()
                nodes.map { node ->
                    async {
                        messageClient.sendMessage(
                            node.id,
                            AXIS_PATH,
                            x.toString().toByteArray()
                        )
                            .await()
                    }
                }.awaitAll()

                Timber.d(TAG, "Starting activity requests sent successfully")
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (exception: Exception) {
                Timber.e(TAG, exception)
            }
        }
    }

    private fun sensorEventFlow(
        sensor: Sensor,
        manager: SensorManager,
    ): Flow<SensorEvent> {
        return channelFlow {
            val listener = object : SensorEventListener {
                override fun onSensorChanged(event: SensorEvent?) {
                    if (event?.sensor?.type == sensor.type) {
                        launch { send(event) }
                    }
                }

                override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
                    // no-op
                }
            }
            try {
                manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
                awaitClose()
            } finally {
                manager.unregisterListener(listener, sensor)
            }
        }
    }


    companion object {
        const val CHANNEL_ID = "sensor"
        private const val NOTIFICATION_ID = 9999
        private const val TAG = "SensorService"
        private const val AXIS_PATH = "/axis"
    }
}
