package com.tkhskt.theremin

import android.app.Activity
import android.content.Context
import android.view.WindowInsets
import android.view.WindowManager
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.mediapipe.framework.TextureFrame
import com.google.mediapipe.solutioncore.CameraInput
import com.google.mediapipe.solutions.hands.HandLandmark
import com.google.mediapipe.solutions.hands.Hands
import com.google.mediapipe.solutions.hands.HandsOptions
import com.google.mediapipe.solutions.hands.HandsResult
import timber.log.Timber

class HandDetector : DefaultLifecycleObserver {

    private var hands: Hands? = null

    private var cameraInput: CameraInput? = null

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        cameraInput = null
        hands = null
    }

    fun init(activity: Activity) {
        cameraInput = CameraInput(activity).apply {
            setNewFrameListener { textureFrame: TextureFrame? ->
                hands?.send(textureFrame)
            }
        }
        startCamera(activity)
    }

    fun setupStreamingModePipeline(activity: Activity) {
        stopCurrentPipeline()
        // Initializes a new MediaPipe Hands solution instance in the streaming mode.
        hands = Hands(
            activity,
            HandsOptions.builder()
                .setStaticImageMode(false)
                .setMaxNumHands(2)
                .setRunOnGpu(RUN_ON_GPU)
                .build()
        ).apply {
            setErrorListener { message: String, _: RuntimeException? ->
                Timber.e(TAG, "MediaPipe Hands error:$message")
            }
            setResultListener { handsResult: HandsResult ->
                logWristLandmark(handsResult)
            }
        }
        cameraInput = CameraInput(activity).apply {
            setNewFrameListener { textureFrame: TextureFrame? ->
                hands?.send(textureFrame)
            }
        }
        startCamera(activity)
    }

    private fun stopCurrentPipeline() {
        cameraInput?.run {
            setNewFrameListener(null)
            close()
        }
        hands?.close()
    }

    private fun startCamera(activity: Activity) {
        val wm = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val windowMetrics = wm.currentWindowMetrics
        val windowInsets: WindowInsets = windowMetrics.windowInsets

        val insets = windowInsets.getInsetsIgnoringVisibility(
            WindowInsets.Type.navigationBars() or WindowInsets.Type.displayCutout()
        )
        val insetsWidth = insets.right + insets.left
        val insetsHeight = insets.top + insets.bottom

        val b = windowMetrics.bounds
        val width = b.width() - insetsWidth
        val height = b.height() - insetsHeight

        val hands = hands ?: return
        cameraInput?.start(
            activity,
            hands.glContext,
            CameraInput.CameraFacing.FRONT,
            width,
            height
        )
    }

    private fun logWristLandmark(result: HandsResult) {
        if (result.multiHandLandmarks().isEmpty()) return
        val wristLandmark = result.multiHandLandmarks()[0].landmarkList[HandLandmark.WRIST]
        Timber.i(
            TAG, String.format(
                "MediaPipe Hand wrist world coordinates (in meters with the origin at the hand's"
                        + " approximate geometric center): x=%f m, y=%f m, z=%f m",
                wristLandmark.x, wristLandmark.y, wristLandmark.z
            )
        )
    }

    companion object {
        private const val TAG = "MainViewModel"
        private const val RUN_ON_GPU = true
    }
}
