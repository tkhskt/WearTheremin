package com.tkhskt.theremin.feature.theremin.ui

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

class HandTracker(
    private val activity: Activity,
) : DefaultLifecycleObserver {

    var onChangeDistanceListener: (Float) -> Unit = {}

    private var hands: Hands? = null

    private var cameraInput: CameraInput? = null

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        init()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        cameraInput?.close()
        hands?.close()
        cameraInput = null
        hands = null
    }

    fun startTracking() {
        stopCurrentPipeline()
        hands = Hands(
            activity,
            HandsOptions.builder()
                .setStaticImageMode(false)
                .setMaxNumHands(2)
                .setRunOnGpu(RUN_ON_GPU)
                .build()
        ).apply {
            setErrorListener { message: String, _: RuntimeException? ->
                Timber.e("MediaPipe Hands error:$message")
            }
            setResultListener { handsResult: HandsResult ->
                if (handsResult.multiHandLandmarks().isEmpty()) return@setResultListener
                val wristLandmark =
                    handsResult.multiHandLandmarks()[0].landmarkList[HandLandmark.WRIST]
                onChangeDistanceListener.invoke(wristLandmark.y)
            }
        }
        cameraInput = CameraInput(activity).apply {
            setNewFrameListener { textureFrame: TextureFrame? ->
                hands?.send(textureFrame)
            }
        }
        startCamera(activity)
    }

    private fun init() {
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
        val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val windowMetrics = windowManager.currentWindowMetrics
        val windowInsets = windowMetrics.windowInsets

        val insets = windowInsets.getInsetsIgnoringVisibility(
            WindowInsets.Type.navigationBars() or WindowInsets.Type.displayCutout()
        )
        val insetsWidth = insets.right + insets.left
        val insetsHeight = insets.top + insets.bottom

        val bounds = windowMetrics.bounds
        val width = bounds.width() - insetsWidth
        val height = bounds.height() - insetsHeight

        val hands = hands ?: return
        cameraInput?.start(
            activity,
            hands.glContext,
            CameraInput.CameraFacing.FRONT,
            width,
            height,
        )
    }

    companion object {
        private const val RUN_ON_GPU = true
    }
}
