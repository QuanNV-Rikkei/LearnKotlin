package com.rikkei.tra_02t0108dialog.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.rikkei.tra_02t0108dialog.R
import kotlinx.android.synthetic.main.layout_brightness.*

class BrightnessDialogFragment : DialogFragment() {

    companion object {
        fun newInstance() = BrightnessDialogFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_brightness, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvCancel.setOnClickListener(View.OnClickListener {
            dismiss()
        })
        tvOk.setOnClickListener(View.OnClickListener {
            dismiss()
        })
    }

    override fun onStart() {
        super.onStart()
        val window = dialog.window
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }
}