package com.rikkei.tra_02t0104fragmentlifecycle

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_a.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [AFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [AFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class AFragment : Fragment(), View.OnClickListener {

    val TAG = AFragment::class.java.simpleName

    lateinit var mediaPlayer: MediaPlayer
    var startTime = 0
    var finalTime = 0
    var handler: Handler = Handler()

    val timeSeek = 5000

    var isFirstRun = true

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate")
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        mediaPlayer = MediaPlayer.create(context, R.raw.ai_la_nguoi_thuong_em_quan_ap)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_a, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        ivStop.isEnabled = false
        ivForward.setOnClickListener(this)
        ivStop.setOnClickListener(this)
        ivPlay.setOnClickListener(this)
        ivRewind.setOnClickListener(this)
        btnGoTo2.setOnClickListener(this)

        if (mediaPlayer != null) {
            finalTime = mediaPlayer!!.duration
            startTime = mediaPlayer!!.currentPosition
            seekBar.max = finalTime
            tvTimeRun.text = createTime(startTime)
            tvTimeRemain.text = createTime(finalTime - startTime)
        }

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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "onSaveInstanceState")
        if (mediaPlayer != null) {
            outState!!.putInt("position", mediaPlayer!!.currentPosition)
            outState.putBoolean("isPlaying", mediaPlayer!!.isPlaying)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        Log.d(TAG, "onViewStateRestored")
        if (savedInstanceState != null){
            val currentPosition = savedInstanceState.getInt("position")
            savedInstanceState.getBoolean("isPlaying")
            if (mediaPlayer != null){
                mediaPlayer!!.seekTo(currentPosition)
                mediaPlayer!!.start()

            }
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
            ivPlay.isEnabled = false
            ivStop.isEnabled = true
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
        Log.d(TAG, "onDestroyView")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
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
                listener?.goToFragmentB()
//                startActivity(Intent(applicationContext, Main2Activity::class.java))
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
        Toast.makeText(context, "Playing song", Toast.LENGTH_SHORT).show()
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
        Toast.makeText(context, "Pausing song", Toast.LENGTH_SHORT).show()
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



    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach")
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach")
        listener = null
        if (mediaPlayer != null) {
            mediaPlayer!!.release()
        }
        if (updateSongTime != null) {
            handler.removeCallbacks(updateSongTime)
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
//        fun onFragmentInteraction(uri: Uri)
        fun goToFragmentB();
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
