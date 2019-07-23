package com.rikkei.tra_02t0114camera

import android.Manifest
import android.content.Context
import android.hardware.Camera
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.RuntimeException
import android.os.AsyncTask
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.PopupMenu
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private val REQUSET_PERMISSION = 101

    //    private lateinit var cameraPreview: CameraPreview
    private lateinit var cameraCustomPreview: CameraCustomPreview
    private var mCamera: Camera? = null
    private var mediaRecorder: MediaRecorder? = null

    private var cameraFront = false
    private var isRecording = false
    private var mSupportedPreviewSizes: List<Camera.Size>? = null
    private var indexMenu: Int? = 0

    private val MEDIA_TYPE_IMAGE = 1
    private val MEDIA_TYPE_VIDEO = 2

    private var width = 0
    private var height = 0
    private var currentType = 0
    private var strFile = ""


    private val permissions = arrayOf(
        Manifest.permission.CAMERA,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.RECORD_AUDIO
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)

        cameraCustomPreview = CameraCustomPreview(this)
        cameraCustomPreview.layoutParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        surfaceView.addView(cameraCustomPreview)
        cameraCustomPreview.keepScreenOn = true


        btnCaptureImage.setOnClickListener(View.OnClickListener {
            if (mCamera != null) {
                mCamera!!.takePicture(null, null, mPicture)
            }
        })

        btnSwitchCamera.setOnClickListener(View.OnClickListener {
            val cameraNumber = Camera.getNumberOfCameras()
            if (cameraNumber > 1) {
                releaseCamera()
                chooseCamera()
            }
        })

        btnCaptureVideo.setOnClickListener(View.OnClickListener {
            if (isRecording) {
                // stop recording and release camera
                mediaRecorder?.stop() // stop the recording
                releaseMediaRecorder() // release the MediaRecorder object
                mCamera?.lock() // take camera access back from MediaRecorder
                updateShowThumbNail(currentType, strFile)

                // inform the user that recording has stopped
                btnCaptureVideo.setImageResource(R.drawable.ic_videocam_white_24dp)
                isRecording = false
            } else {
                // initialize video camera
                if (prepareVideoRecorder()) {
                    // Camera is available and unlocked, MediaRecorder is prepared,
                    // now you can start recording
                    mediaRecorder?.start()

                    // inform the user that recording has started
                    btnCaptureVideo.setImageResource(R.drawable.ic_videocam_off_white_24dp)
                    isRecording = true
                } else {
                    // prepare didn't work, release the camera
                    releaseMediaRecorder()
                    // inform user
                }
            }
        })

        btnMenu.setOnClickListener(View.OnClickListener {
            createPopupMenu(mSupportedPreviewSizes)
        })

    }

    private fun updateShowThumbNail(currentType: Int, strFile: String) {
        if (TextUtils.isEmpty(strFile)) {
            return
        }
        if (MEDIA_TYPE_IMAGE == currentType) {
            val bitmap = BitmapFactory.decodeFile(strFile)
            val nh: Int = ((bitmap.getHeight() * (80.0 / bitmap.getWidth())).roundToInt())
            val scaled = Bitmap.createScaledBitmap(bitmap, 80, nh, true)
            ivThumbnail.setImageBitmap(scaled)
        } else if (MEDIA_TYPE_VIDEO == currentType) {
            val bitmap = ThumbnailUtils.createVideoThumbnail(strFile, MediaStore.Video.Thumbnails.MICRO_KIND)
            ivThumbnail.setImageBitmap(bitmap)
        }
    }

    private fun createPopupMenu(listSize: List<Camera.Size>?) {
        if (listSize != null && !listSize.isEmpty()) {
            val popupMenu = PopupMenu(this, btnMenu)
            for ((index, previewSize) in listSize.withIndex()) {
                popupMenu.menu.add(0, index, index, ("${previewSize.width} X ${previewSize.height}"))
            }
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { menuItem: MenuItem? ->
                indexMenu = menuItem!!.itemId
                Log.e(TAG, "indexMenu: " + indexMenu)
                val previewSize = this!!.mSupportedPreviewSizes!!.get(indexMenu!!)
                width = previewSize.width
                height = previewSize.height

                mCamera = Camera.open()
                mCamera!!.setDisplayOrientation(90)
                cameraCustomPreview.setCamera(mCamera, width, height)
                true
            })
            popupMenu.show()
        }
    }


    /** Check if this device has a mCamera */
    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, permissions[2]) == PackageManager.PERMISSION_GRANTED;
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (REQUSET_PERMISSION == requestCode) {
            var hasPermission = true
            if (grantResults != null && !grantResults.isEmpty()) {
                for (grant in grantResults) {
                    if (grant != PackageManager.PERMISSION_GRANTED) {
                        hasPermission = false
                        break
                    }
                }
                if (hasPermission) {
                    initCamera()
                }
            }
        }
    }

    private fun prepareVideoRecorder(): Boolean {
        mediaRecorder = MediaRecorder()

        mCamera?.let { camera ->
            // Step 1: Unlock and set mCamera to MediaRecorder
            camera?.unlock()

            mediaRecorder?.run {
                setCamera(camera)

                // Step 2: Set sources
                setAudioSource(MediaRecorder.AudioSource.CAMCORDER)
                setVideoSource(MediaRecorder.VideoSource.CAMERA)

                /*// Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
                setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH))*/


                // Step 5: Set the preview output
                setPreviewDisplay(cameraCustomPreview?.holder?.surface)

                // Customise your profile based on a pre-existing profile
                val profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH)
                profile.fileFormat = MediaRecorder.OutputFormat.MPEG_4
                profile.videoCodec = MediaRecorder.VideoEncoder.MPEG_4_SP
                setProfile(profile)

                // Step 4: Set output file
                setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString())


                // Step 6: Prepare configured MediaRecorder
                return try {
                    prepare()
                    true
                } catch (e: IllegalStateException) {
                    Log.d(TAG, "IllegalStateException preparing MediaRecorder: ${e.message}")
                    releaseMediaRecorder()
                    false
                } catch (e: IOException) {
                    Log.d(TAG, "IOException preparing MediaRecorder: ${e.message}")
                    releaseMediaRecorder()
                    false
                }
            }

        }
        return false
    }


    private fun releaseMediaRecorder() {
        if (mediaRecorder != null) {
            mediaRecorder!!.reset()
            mediaRecorder!!.release()
            mediaRecorder = null
        }
    }


    /** Create a File for saving an image or video */
    private fun getOutputMediaFile(type: Int): File? {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        currentType = type
        val mediaStorageDir = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "MyCameraApp"
        )
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        mediaStorageDir.apply {
            if (!exists()) {
                if (!mkdirs()) {
                    Log.d("MyCameraApp", "failed to create directory")
                    return null
                }
            }
        }

        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        var file: File?
        return when (type) {
            MEDIA_TYPE_IMAGE -> {
                file = File("${mediaStorageDir.path}${File.separator}IMG_$timeStamp.jpg")
                strFile = file.absolutePath
                file
            }
            MEDIA_TYPE_VIDEO -> {
                file = File("${mediaStorageDir.path}${File.separator}VID_$timeStamp.mp4")
                strFile = file.absolutePath
                file
            }
            else -> null
        }
    }

    override fun onResume() {
        super.onResume()
        if (!checkCameraHardware(this)) {
            return
        }
        if (checkPermission()) {
            initCamera()
        } else {
            ActivityCompat.requestPermissions(this, permissions, REQUSET_PERMISSION)
        }
    }

    private fun initCamera() {
        val numCams = Camera.getNumberOfCameras()
        if (numCams > 0) {
            try {
                mCamera = Camera.open()
                mCamera!!.setDisplayOrientation(90)
                mSupportedPreviewSizes = mCamera!!.parameters.supportedPreviewSizes

                cameraCustomPreview.setCamera(mCamera!!, 0, 0)

                if (mSupportedPreviewSizes != null) {
                    for (previewSize in mSupportedPreviewSizes!!) {
                        Log.e(TAG, "Size: " + previewSize.width + " - " + previewSize.height)
                    }
                }
            } catch (ex: RuntimeException) {
                Toast.makeText(this, "Không thể mở mCamera", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun releaseCamera() {
        if (mCamera != null) {
            mCamera!!.stopPreview()
            mCamera!!.release()
            mCamera = null
        }
    }

    override fun onPause() {
        super.onPause()
        releaseMediaRecorder()
        releaseCamera()
    }

    private fun resetCam() {
        mCamera!!.stopPreview()
        mCamera!!.startPreview()
    }

    fun chooseCamera() {
        //if the mCamera preview is the front
        try {
            if (cameraFront) {
                val cameraId = findBackFacingCamera()
                if (cameraId >= 0) {
                    mCamera = Camera.open(cameraId)
                    mCamera!!.setDisplayOrientation(90)
                    cameraCustomPreview.setCamera(mCamera!!, width, height)
                }
            } else {
                val cameraId = findFrontFacingCamera()
                if (cameraId >= 0) {
                    mCamera = Camera.open(cameraId)
                    mCamera!!.setDisplayOrientation(90)
                    cameraCustomPreview.setCamera(mCamera!!, width, height)
                }
            }
        } catch (ex: Exception) {
            Log.e(TAG, "error: " + ex.message)
        }
    }

    private fun findFrontFacingCamera(): Int {

        var cameraId = -1
        // Search for the front facing mCamera
        val numberOfCameras = Camera.getNumberOfCameras()
        for (i in 0 until numberOfCameras) {
            val info = Camera.CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                cameraId = i
                cameraFront = true
                break
            }
        }
        return cameraId

    }

    private fun findBackFacingCamera(): Int {
        var cameraId = -1
        //Search for the back facing mCamera
        //get the number of cameras
        val numberOfCameras = Camera.getNumberOfCameras()
        //for every mCamera check
        for (i in 0 until numberOfCameras) {
            val info = Camera.CameraInfo()
            Camera.getCameraInfo(i, info)
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i
                cameraFront = false
                break

            }

        }
        return cameraId
    }

    private var mPicture = Camera.PictureCallback { data, _ ->
        run {
            SaveImageTask().execute(data)
            if (!isRecording) {
                resetCam()
            }
        }
    }

    private fun refreshGallery(file: File?) {
        if (file != null) {
            val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            mediaScanIntent.data = Uri.fromFile(file)
            sendBroadcast(mediaScanIntent)
        }
    }

    private inner class SaveImageTask : AsyncTask<ByteArray, Void, Void>() {

        override fun doInBackground(vararg data: ByteArray): Void? {
            var outStream: FileOutputStream? = null

            // Write to SD Card
            try {
                /*val sdCard = getExternalStorageDirectory()
                val dir = File(sdCard.getAbsolutePath() + "/camtest")
                dir.mkdirs()

                val fileName = String.format("%d.jpg", System.currentTimeMillis())*/
                val outFile = getOutputMediaFile(MEDIA_TYPE_IMAGE)
                if (outFile != null) {
                    outStream = FileOutputStream(outFile)
                    outStream!!.write(data[0])
                    outStream.flush()
                    outStream.close()

                    Log.e(TAG, "onPictureTaken - wrote bytes: " + data.size + " to " + outFile!!.getAbsolutePath())

                    refreshGallery(outFile)
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
            }
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            updateShowThumbNail(currentType, strFile)
        }

    }
}
