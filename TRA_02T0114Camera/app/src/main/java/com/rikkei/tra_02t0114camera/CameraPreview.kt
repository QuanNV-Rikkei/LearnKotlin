package com.rikkei.tra_02t0114camera


import android.content.Context
import android.hardware.Camera
import android.hardware.Camera.Size
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import java.io.IOException
import android.R.layout


@Suppress("DEPRECATION")
class CameraPreview : ViewGroup, SurfaceHolder.Callback {

    private val TAG = CameraPreview::class.java.simpleName
    private val mSurfaceView: SurfaceView
    private val mHolder: SurfaceHolder
    private var mPreviewSize: Size? = null
    private var mSupportedPreviewSizes: List<Size>? = null

    private var mCamera: Camera? = null

    constructor(context: Context, surfaceView: SurfaceView) : super(context) {
        mSurfaceView = surfaceView

        mHolder = mSurfaceView.holder.apply {
            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            addCallback(this@CameraPreview)
            // deprecated setting, but required on Android versions prior to 3.0
            setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        }


    }

    public fun setCamera(camera: Camera?) {
        mCamera = camera
        if (mCamera != null) {
            mSupportedPreviewSizes = mCamera!!.parameters.supportedPreviewSizes
            requestLayout()

            val params: Camera.Parameters = mCamera!!.parameters
            val focusModes = params.supportedFocusModes
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                params.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
                mCamera!!.parameters = params
            }

        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // We purposely disregard child measurements because act as a
        // wrapper to a SurfaceView that centers the camera preview instead
        // of stretching it.
        val width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        val height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
        setMeasuredDimension(width, height);

        if (mSupportedPreviewSizes != null) {
            mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
        }
    }

    private fun getOptimalPreviewSize(sizes: List<Size>?, w: Int, h: Int): Size? {
        val ASPECT_TOLERANCE = 0.1
        val targetRatio = w.toDouble() / h
        if (sizes == null) return null

        var optimalSize: Size? = null
        var minDiff = java.lang.Double.MAX_VALUE

// Try to find an size match aspect ratio and size
        for (size in sizes) {
            val ratio = (size.width / size.height).toDouble()
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE) continue
            if (Math.abs(size.height - h) < minDiff) {
                optimalSize = size
                minDiff = Math.abs(size.height - h).toDouble()
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = java.lang.Double.MAX_VALUE
            for (size in sizes) {
                if (Math.abs(size.height - h) < minDiff) {
                    optimalSize = size
                    minDiff = Math.abs(size.height - h).toDouble()
                }
            }
        }
        return optimalSize
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (changed && childCount > 0) {
            val child = getChildAt(0)

            val width = r - l
            val height = b - t

            var previewWidth = width
            var previewHeight = height
            if (mPreviewSize != null) {
                previewWidth = mPreviewSize!!.width
                previewHeight = mPreviewSize!!.height
            }

            // Center the child SurfaceView within the parent.
            if (width * previewHeight > height * previewWidth) {
                val scaledChildWidth = previewWidth * height / previewHeight
                child.layout(
                    (width - scaledChildWidth) / 2, 0,
                    (width + scaledChildWidth) / 2, height
                )
            } else {
                val scaledChildHeight = previewHeight * width / previewWidth
                child.layout(
                    0, (height - scaledChildHeight) / 2,
                    width, (height + scaledChildHeight) / 2
                )
            }
        }
    }


    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, w: Int, h: Int) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        if (mHolder.surface == null || mCamera == null) {
            // preview surface does not exist
            return
        }

        val parameters = mCamera!!.parameters
        if (mPreviewSize != null) {
            parameters.setPreviewSize(mPreviewSize!!.width, mPreviewSize!!.height)
            requestLayout()
        }
        // stop preview before making changes
        try {
            mCamera!!.stopPreview()
        } catch (e: Exception) {
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        mCamera!!.apply {
            try {
                setPreviewDisplay(mHolder)
                startPreview()
            } catch (e: Exception) {
                Log.d(TAG, "Error starting camera preview: ${e.message}")
            }
        }
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        // Surface will be destroyed when we return, so stop the preview.
        if (mCamera != null) {
            // Call stopPreview() to stop updating the preview surface.
            mCamera!!.stopPreview();
        }
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            if (mCamera != null) {
                mCamera!!.setPreviewDisplay(holder)
                mCamera!!.startPreview()
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error starting camera preview: ${e.message}")
        }
    }
}