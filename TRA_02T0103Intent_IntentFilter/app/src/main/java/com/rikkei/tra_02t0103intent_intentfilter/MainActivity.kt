package com.rikkei.tra_02t0103intent_intentfilter

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.provider.MediaStore
import android.text.TextUtils
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.MediaController
import android.widget.Toast
import androidx.annotation.RequiresApi

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import java.util.jar.Manifest

const val VIDEO_CAPTURE = 100
const val REQUEST_PERMISSION = 101
const val REQUEST_RESULT = 102

class MainActivity : AppCompatActivity() {

    val listPermission = arrayOf(
        android.Manifest.permission.CAMERA,
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    )


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        handleIntent()

        fab.setOnClickListener { view ->
            if (checkSelfPermission(listPermission[0]) != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(listPermission[1]) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(listPermission, REQUEST_PERMISSION)
            } else {
                captureVideo()
            }
        }

        btnNext.setOnClickListener({ view ->
            //            startActivity(Intent(this, BActivity::class.java))
            val intent = Intent(this, BActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
            startActivityForResult(intent, REQUEST_RESULT)
        })
    }

    private fun handleIntent() {
        if (intent != null && intent.action == Intent.ACTION_SEND) {
            when {
                intent.type.startsWith("image/") -> {
                    handleSendImage(intent)
                }
                intent.type.startsWith("video/") -> {
                    handleSendVideo(intent)
                }
                else -> {

                }
            }
        }
    }

    private fun handleSendImage(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            // Update UI to reflect image being shared
            ivItem.setImageURI(intent.getParcelableExtra(Intent.EXTRA_STREAM));
        }
    }

    private fun handleSendVideo(intent: Intent) {
        (intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri)?.let {
            // Update UI to reflect image being shared
            videoView.setVideoURI(intent.getParcelableExtra(Intent.EXTRA_STREAM))
            videoView.setMediaController(MediaController(this))
            videoView.requestFocus()
            videoView.start()
        }
    }


    private fun captureVideo() {
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {

            /*val mediaFile = File(Environment.getExternalStorageDirectory().absolutePath + "/test_video.mp4")
            val uriVideo = Uri.fromFile(mediaFile)*/


            val intent = Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10)
            intent.putExtra(
                MediaStore.EXTRA_OUTPUT,
                Environment.getExternalStorageDirectory().absolutePath + "/test_video.mp4"
            );
            startActivityForResult(intent, VIDEO_CAPTURE);

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            var isGranted = true
            if (grantResults != null && !grantResults.isEmpty()) {
                for (item in grantResults) {
                    if (PackageManager.PERMISSION_DENIED == item) {
                        isGranted = false
                        break
                    }
                }
            }
            if (isGranted) {
                captureVideo()
            } else {
                Toast.makeText(this, "You must grant permission! ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (VIDEO_CAPTURE == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Video saved to: " + data?.data, Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Video recording cancelled", Toast.LENGTH_SHORT).show()
            }
        } else if (REQUEST_RESULT == requestCode) {
            if (resultCode == Activity.RESULT_OK) {
                val str = data?.getStringExtra("data")
                if (!TextUtils.isEmpty(str)) {
                    tvResult.setText(str)
                }
            }
        } else {
            Toast.makeText(this, "Failed to record video", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
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
    }
}
