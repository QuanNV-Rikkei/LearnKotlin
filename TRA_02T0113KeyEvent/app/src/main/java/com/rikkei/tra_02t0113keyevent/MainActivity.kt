package com.rikkei.tra_02t0113keyevent

import android.os.Bundle
import android.view.KeyEvent
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    var i = 0
    var j = 0
    val listData = arrayOf(
        arrayOf(KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2 , KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_4, KeyEvent.KEYCODE_5,
            KeyEvent.KEYCODE_6, KeyEvent.KEYCODE_7, KeyEvent.KEYCODE_8, KeyEvent.KEYCODE_9, KeyEvent.KEYCODE_0),
        arrayOf(KeyEvent.KEYCODE_Q, KeyEvent.KEYCODE_W, KeyEvent.KEYCODE_E, KeyEvent.KEYCODE_R, KeyEvent.KEYCODE_T,
            KeyEvent.KEYCODE_Y, KeyEvent.KEYCODE_U, KeyEvent.KEYCODE_I, KeyEvent.KEYCODE_O, KeyEvent.KEYCODE_P)
        /*arrayOf(KeyEvent.KEYCODE_A, KeyEvent.KEYCODE_S, KeyEvent.KEYCODE_D, KeyEvent.KEYCODE_F, KeyEvent.KEYCODE_G,
            KeyEvent.KEYCODE_H, KeyEvent.KEYCODE_J, KeyEvent.KEYCODE_K, KeyEvent.KEYCODE_L)*/

    )
    var keyCurrent : Int = 8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        /*fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }*/
    }

    /*private fun getKetEventCode(row: Int, column: Int) : Int {
        return listData[row][column]
    }

    private fun handleEventUpAndDown() {
        if (i == 0) i = 1 else i = 0
        getKetEventCode(i, j)
    }

    private fun handleEventLeft() {
        if (j == 0 && i == 0) {
            return
        } else if(j == 0 && i == 1) {
            i = 0
            j = 9
        } else {
            j--
        }
        getKetEventCode(i, j)
    }

    private fun handleEventRight() {
        if (j == 9 && i == 1) {
            i = 0
            j = 0
        } else if (j == 9 && i ==0){
            i = 1
            j = 0
        } else {
            j++
        }
        getKetEventCode(i, j)
    }*/


    override fun dispatchKeyEvent(event: KeyEvent?): Boolean {
        if (event != null) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_1 -> {
                    handleKeyInput(event, tv1, "1")
                }
                KeyEvent.KEYCODE_2 -> {
                    handleKeyInput(event, tv2, "2")
                }
                KeyEvent.KEYCODE_3 -> {
                    handleKeyInput(event, tv3, "3")
                }
                KeyEvent.KEYCODE_4 -> {
                    handleKeyInput(event, tv4, "4")
                }
                KeyEvent.KEYCODE_5 -> {
                    handleKeyInput(event, tv5, "5")
                }
                KeyEvent.KEYCODE_6 -> {
                    handleKeyInput(event, tv6, "6")
                }
                KeyEvent.KEYCODE_7 -> {
                    handleKeyInput(event, tv7, "7")
                }
                KeyEvent.KEYCODE_8 -> {
                    handleKeyInput(event, tv8, "8")
                }
                KeyEvent.KEYCODE_9 -> {
                    handleKeyInput(event, tv9, "9")
                }
                KeyEvent.KEYCODE_0 -> {
                    handleKeyInput(event, tv0, "0")
                }
                /*-----------------------------*/
                KeyEvent.KEYCODE_Q -> {
                    handleKeyInput(event, tvQ, "Q")
                }
                KeyEvent.KEYCODE_W -> {
                    handleKeyInput(event, tvW, "W")
                }
                KeyEvent.KEYCODE_E -> {
                    handleKeyInput(event, tvE, "E")
                }
                KeyEvent.KEYCODE_R -> {
                    handleKeyInput(event, tvR, "R")
                }
                KeyEvent.KEYCODE_T -> {
                    handleKeyInput(event, tvT, "T")
                }
                KeyEvent.KEYCODE_Y -> {
                    handleKeyInput(event, tvY, "Y")
                }
                KeyEvent.KEYCODE_U -> {
                    handleKeyInput(event, tvU, "U")
                }
                KeyEvent.KEYCODE_I -> {
                    handleKeyInput(event, tvI, "I")
                }
                KeyEvent.KEYCODE_O -> {
                    handleKeyInput(event, tvO, "O")
                }
                KeyEvent.KEYCODE_P -> {
                    handleKeyInput(event, tvP, "P")
                }
                /*-----------------------------*/
                KeyEvent.KEYCODE_A -> {
                    handleKeyInput(event, tvA, "A")
                }
                KeyEvent.KEYCODE_S -> {
                    handleKeyInput(event, tvS, "S")
                }
                KeyEvent.KEYCODE_D -> {
                    handleKeyInput(event, tvD, "D")
                }
                KeyEvent.KEYCODE_F -> {
                    handleKeyInput(event, tvF, "F")
                }
                KeyEvent.KEYCODE_G -> {
                    handleKeyInput(event, tvG, "G")
                }
                KeyEvent.KEYCODE_H -> {
                    handleKeyInput(event, tvH, "H")
                }
                KeyEvent.KEYCODE_J -> {
                    handleKeyInput(event, tvJ, "J")
                }
                KeyEvent.KEYCODE_K -> {
                    handleKeyInput(event, tvK, "K")
                }
                KeyEvent.KEYCODE_L -> {
                    handleKeyInput(event, tvL, "L")
                }
                /*KeyEvent.KEYCODE_P -> {
                    ( > ) không biết ký tự nào :)
                }*/

                /*-----------------------------*/
                KeyEvent.KEYCODE_Z -> {
                    handleKeyInput(event, tvZ, "Z")
                }
                KeyEvent.KEYCODE_X -> {
                    handleKeyInput(event, tvX, "X")
                }
                KeyEvent.KEYCODE_C -> {
                    handleKeyInput(event, tvC, "C")
                }
                KeyEvent.KEYCODE_V -> {
                    handleKeyInput(event, tvV, "V")
                }
                KeyEvent.KEYCODE_B -> {
                    handleKeyInput(event, tvB, "B")
                }
                KeyEvent.KEYCODE_N -> {
                    handleKeyInput(event, tvN, "N")
                }
                KeyEvent.KEYCODE_M -> {
                    handleKeyInput(event, tvM, "M")
                }

                /*KeyEvent.KEYCODE_K -> {
                   ( < ) không biết ký tự nào :)
                }*/
                KeyEvent.KEYCODE_DEL -> {
                    handleKeyDel(event)
                }
                KeyEvent.KEYCODE_DPAD_UP -> {
//                    handleEventUpAndDown()
                }
                KeyEvent.KEYCODE_DPAD_DOWN -> {
//                    handleEventUpAndDown()
                }
                KeyEvent.KEYCODE_DPAD_LEFT -> {
//                    handleEventLeft()
                }
                KeyEvent.KEYCODE_DPAD_RIGHT -> {
//                    handleEventRight()
                }
                KeyEvent.KEYCODE_ENTER -> {

                }
            }
        }
        return super.dispatchKeyEvent(event)
    }

    private fun handleKeyInput(event: KeyEvent, tvInput: TextView, strInput: String): Boolean {

        if (event.action == KeyEvent.ACTION_DOWN) {
            tvInput.setBackgroundResource(R.color.green_light)
        } else if (event.action == KeyEvent.ACTION_UP) {
//            tvInput.setBackgroundResource(R.color.white)
            tvContent.setText(tvContent.text.toString() + strInput)
        }

        return true
    }

    private fun handleKeyDel(event: KeyEvent) :Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            tvDel.setBackgroundResource(R.color.green_light)
        } else if (event.action == KeyEvent.ACTION_UP) {
//            tvDel.setBackgroundResource(R.color.white)
            var strContent = tvContent.text.toString()
            strContent = strContent.substring(0, strContent.length - 1)
            tvContent.setText(strContent)
        }
        return true
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
