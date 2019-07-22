package com.rikkei.tra_02t0114camera

import android.Manifest
import android.content.Context
import android.hardware.Camera
import android.os.Bundle
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
import android.util.Log
import android.view.*
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;

import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.RuntimeException
import android.os.Environment.getExternalStorageDirectory
import android.os.AsyncTask
import android.os.Environment
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private val REQUSET_PERMISSION = 101

    private lateinit var cameraPreview: CameraPreview
    private var camera: Camera? = null

    private var cameraFront = false

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

        cameraPreview = CameraPreview(this, surfaceView)
        cameraPreview.layoutParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        layout.addView(cameraPreview)
        cameraPreview.keepScreenOn = true

        btnCaptureImage.setOnClickListener(View.OnClickListener {
            if (camera != null) {
                camera!!.takePicture(null, null, mPicture)
            }
        })

        btnSwitchCamera.setOnClickListener(View.OnClickListener {
            val cameraNumber = Camera.getNumberOfCameras()
            if (cameraNumber > 1) {
//                releaseCamera()
                if (camera != null) {
                    camera!!.stopPreview()
                    cameraPreview.setCamera(null)
                    camera!!.release()
//                    camera = null
                }
                chooseCamera()
            }
        })


        /*setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/
    }

    /** Check if this device has a camera */
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
                camera = Camera.open()
                camera!!.setDisplayOrientation(90);
                cameraPreview.setCamera(camera)
                camera!!.startPreview()
            } catch (ex: RuntimeException) {
                Toast.makeText(this, "Không thể mở camera", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun releaseCamera() {
        if (camera != null) {
            camera!!.stopPreview()
            cameraPreview.setCamera(null)
            camera!!.release()
            camera = null
        }
    }

    override fun onPause() {
        super.onPause()
        releaseCamera()
    }

    private fun resetCam() {
        camera!!.stopPreview()
        camera!!.startPreview()
//        cameraPreview.setCamera(camera)
    }

    fun chooseCamera() {
        //if the camera preview is the front
        if (cameraFront) {
            val cameraId = findBackFacingCamera()
            if (cameraId >= 0) {
                //open the backFacingCamera
                //set a picture callback
                //refresh the preview

                camera = Camera.open(cameraId)
                camera!!.setDisplayOrientation(90)
                cameraPreview.setCamera(camera)
                camera!!.startPreview()
//                mPicture = getPictureCallback()
            }
        } else {
            val cameraId = findFrontFacingCamera()
            if (cameraId >= 0) {
                //open the backFacingCamera
                //set a picture callback
                //refresh the preview
                camera = Camera.open(cameraId)
                camera!!.setDisplayOrientation(90)
                cameraPreview.setCamera(camera)
                camera!!.startPreview()
//                mPicture = getPictureCallback()
            }
        }
    }

    private fun findFrontFacingCamera(): Int {

        var cameraId = -1
        // Search for the front facing camera
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
        //Search for the back facing camera
        //get the number of cameras
        val numberOfCameras = Camera.getNumberOfCameras()
        //for every camera check
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
            resetCam()
        }
    }

    private fun refreshGallery(file: File) {
        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
        mediaScanIntent.data = Uri.fromFile(file)
        sendBroadcast(mediaScanIntent)
    }

    private inner class SaveImageTask : AsyncTask<ByteArray, Void, Void>() {

        override fun doInBackground(vararg data: ByteArray): Void? {
            var outStream: FileOutputStream? = null

            // Write to SD Card
            try {
                val sdCard = getExternalStorageDirectory()
                val dir = File(sdCard.getAbsolutePath() + "/camtest")
                dir.mkdirs()

                val fileName = String.format("%d.jpg", System.currentTimeMillis())
                val outFile = File(dir, fileName)

                outStream = FileOutputStream(outFile)
                outStream!!.write(data[0])
                outStream.flush()
                outStream.close()

                Log.d(TAG, "onPictureTaken - wrote bytes: " + data.size + " to " + outFile.getAbsolutePath())

                refreshGallery(outFile)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
            }
            return null
        }

    }


    /*override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }*/
}
