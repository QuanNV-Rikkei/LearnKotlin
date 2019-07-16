package com.rikkei.tra_02t0108dialog

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.DatePicker
import com.rikkei.tra_02t0108dialog.dialog.BrightnessDialogFragment
import com.rikkei.tra_02t0108dialog.dialog.EraseUsbDialogFragment
import com.rikkei.tra_02t0108dialog.dialog.PickToppingDialogFragment
import com.rikkei.tra_02t0108dialog.dialog.TextLimitDialogFragment

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        btnPickTopping.setOnClickListener(this)
        btnBrightness.setOnClickListener(this)
        btnTextLimit.setOnClickListener(this)
        btnEraseUsb.setOnClickListener(this)
        btnDatePicker.setOnClickListener(this)
        btnTimePicker.setOnClickListener(this)
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

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        val datePickerDialog =
            DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                tvDate.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
            }, year, month, day)
        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog =
            TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { picker, selectedHour, selectedMinute ->
                tvTime.setText("" + selectedHour + ":" + selectedMinute)
            }, hour, minute, true)
        timePickerDialog.show()
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.btnPickTopping -> {
                val dialogPickTopping = PickToppingDialogFragment.newInstance()
                dialogPickTopping.show(supportFragmentManager, "PickToppingDialogFragment")
            }
            R.id.btnBrightness -> {
                val dialogBrightness = BrightnessDialogFragment.newInstance()
                dialogBrightness.show(supportFragmentManager, "BrightnessDialogFragment")
            }
            R.id.btnTextLimit -> {
                val dialogTextLimit = TextLimitDialogFragment.newInstance()
                dialogTextLimit.show(supportFragmentManager, "TextLimitDialogFragment")
            }
            R.id.btnEraseUsb -> {
                val dialogEraseUsb = EraseUsbDialogFragment.newInstance()
                dialogEraseUsb.show(supportFragmentManager, "EraseUsbDialogFragment")
            }
            R.id.btnDatePicker -> showDatePickerDialog()
            R.id.btnTimePicker -> showTimePickerDialog()
        }
    }
}
