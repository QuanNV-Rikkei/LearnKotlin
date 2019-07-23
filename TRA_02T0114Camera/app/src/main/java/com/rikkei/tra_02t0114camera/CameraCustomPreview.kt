package com.rikkei.tra_02t0114camera

import android.content.Context
import android.hardware.Camera
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException

class CameraCustomPreview : SurfaceView, SurfaceHolder.Callback {

    private val TAG = CameraCustomPreview::class.java.simpleName

    private val mHolder: SurfaceHolder
    private var mCamera: Camera? = null

    constructor(context: Context) : super(context) {
//        mCamera = camera
//        setCamera(camera)
        mHolder = holder.apply {
            holder.addCallback(this@CameraCustomPreview)
            holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        }
    }

    fun refreshCamera(camera: Camera?) {
        if (mHolder.surface == null || camera == null) {
            // preview surface does not exist
            return
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
//        setCamera(camera!!)
        try {
            mCamera!!.setPreviewDisplay(mHolder)
            mCamera!!.startPreview()
        } catch (e: Exception) {
            Log.d(TAG, "Error starting camera preview: " + e.message)
        }

    }

    fun setCamera(camera: Camera?, width: Int, height: Int) {
        mCamera = camera
        if (mCamera != null) {
            val params: Camera.Parameters = mCamera!!.parameters
            val focusModes = params.supportedFocusModes
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                params.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
                mCamera!!.parameters = params
            }

            if (width > 0 && height > 0) {
                params.setPreviewSize(width, height)
                mCamera!!.parameters = params
            }
        }

        try {
            mCamera!!.setPreviewDisplay(mHolder)
            mCamera!!.startPreview()
        } catch (e: Exception) {
            Log.d(TAG, "Error starting camera preview: " + e.message)
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, p1: Int, p2: Int, p3: Int) {
        refreshCamera(mCamera)
    }

    override fun surfaceDestroyed(p0: SurfaceHolder?) {
        // Surface will be destroyed when we return, so stop the preview.
        if (mCamera != null) {
            // Call stopPreview() to stop updating the preview surface.
            mCamera!!.release()
        }
    }

    override fun surfaceCreated(p0: SurfaceHolder?) {
        try {
            // create the surface and start camera preview
            if (mCamera == null) {
                mCamera!!.setPreviewDisplay(holder)
                mCamera!!.startPreview()
            }
        } catch (e: IOException) {
            Log.d(TAG, "Error setting camera preview: " + e.message)
        }

    }
}