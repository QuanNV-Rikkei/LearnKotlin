package com.rikkei.tra_02t0101activitylifecycle

import android.content.Intent
import android.content.res.Configuration
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    val TAG = MainActivity::class.java.simpleName

    var mediaPlayer: MediaPlayer? = null
    var startTime = 0
    var finalTime = 0
    var handler: Handler = Handler()

    val timeSeek = 5000

    var isFirstRun = true


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        mediaPlayer = MediaPlayer.create(this, R.raw.ai_la_nguoi_thuong_em_quan_ap)
        ivStop.isEnabled = false
        ivForward.setOnClickListener(this)
        ivStop.setOnClickListener(this)
        ivPlay.setOnClickListener(this)
        ivRewind.setOnClickListener(this)
        btnGoTo2.setOnClickListener(this)

        finalTime = mediaPlayer!!.duration
        startTime = mediaPlayer!!.currentPosition
        seekBar.max = finalTime
        tvTimeRun.text = createTime(startTime)
        tvTimeRemain.text = createTime(finalTime - startTime)

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, fromUser: Boolean) {
                if (mediaPlayer != null && fromUser) {
                    mediaPlayer!!.seekTo(p1)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {

            }
        })
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            seekBar.setProgress(0, true)
        } else {
            seekBar.setProgress(0)
        }
    }


    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
        if (mediaPlayer != null && !mediaPlayer!!.isPlaying && !isFirstRun) {
            mediaPlayer!!.start()
            if (updateSongTime != null) {
                handler.postDelayed(updateSongTime, 1000)
            }
        }
        isFirstRun = false
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer!!.pause()
        }
        if (updateSongTime != null) {
            handler.removeCallbacks(updateSongTime)
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
        if (mediaPlayer != null && mediaPlayer!!.isPlaying) {
            mediaPlayer!!.pause()
        }
        if (updateSongTime != null) {
            handler.removeCallbacks(updateSongTime)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
        }
        if (updateSongTime != null) {
            handler.removeCallbacks(updateSongTime)
        }
    }

    override fun onClick(p0: View?) {
        val itemId = p0!!.id
        when (itemId) {
            R.id.ivForward -> {
                actionSeekForward()
            }
            R.id.ivStop -> {
                actionStop()
            }
            R.id.ivPlay -> {
                actionPlay()
            }
            R.id.ivRewind -> {
                actionSeekRewind()
            }
            R.id.btnGoTo2 -> {
                startActivity(Intent(applicationContext, Main2Activity::class.java))
            }
        }
    }

    val updateSongTime: Runnable = object : Runnable {
        override fun run() {
            Log.e(TAG, "updateSongTime")
            startTime = mediaPlayer!!.currentPosition
            finalTime = mediaPlayer!!.duration
            tvTimeRun.text = createTime(startTime)
            tvTimeRemain.text = createTime(finalTime - startTime)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                seekBar.setProgress(startTime, true)
            } else {
                seekBar.setProgress(startTime)
            }
            handler.postDelayed(this, 1000)
        }
    }

    fun createTime(time: Int): String {
        var timeLabel = ""
        var min = time / 1000 / 60
        var sec = time / 1000 % 60

        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec
        return timeLabel
    }


    private fun actionPlay() {
        Toast.makeText(applicationContext, "Playing song", Toast.LENGTH_SHORT).show()
        mediaPlayer!!.start()
        finalTime = mediaPlayer!!.duration
        startTime = mediaPlayer!!.currentPosition
        seekBar.max = finalTime
        tvTimeRun.text = createTime(startTime)
        tvTimeRemain.text = createTime(finalTime - startTime)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            seekBar.setProgress(startTime, true)
        } else {
            seekBar.setProgress(startTime)
        }
        handler.postDelayed(updateSongTime, 1000)
        ivPlay.isEnabled = false
        ivStop.isEnabled = true
    }

    private fun actionStop() {
        Toast.makeText(applicationContext, "Pausing song", Toast.LENGTH_SHORT).show()
        mediaPlayer!!.pause()
        ivStop.isEnabled = false
        ivPlay.isEnabled = true
        if (updateSongTime != null) {
            handler.removeCallbacks(updateSongTime)
        }
    }

    private fun actionSeekForward() {
        val temp = startTime
        if ((temp + timeSeek) <= finalTime) {
            startTime += timeSeek
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mediaPlayer!!.seekTo(startTime.toLong(), MediaPlayer.SEEK_NEXT_SYNC)
            } else {
                mediaPlayer!!.seekTo(startTime)
            }
        }
    }

    private fun actionSeekRewind() {
        val temp = startTime
        if ((temp - timeSeek) > 0) {
            startTime -= timeSeek
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mediaPlayer!!.seekTo(startTime.toLong(), MediaPlayer.SEEK_NEXT_SYNC)
            } else {
                mediaPlayer!!.seekTo(startTime)
            }
        }
    }
}

