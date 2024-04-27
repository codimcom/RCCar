package com.example.rccar

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException
import kotlin.math.abs

class MainActivity : AppCompatActivity(){//},OnSeekBarChangeListener {

    private var speedView: TextView? = null
    private var turnView: TextView? = null
    //private var seekbar: SeekBar? = null
    //private var seekbar
    private var btAdapter: BluetoothAdapter? = null
    lateinit var btConnection: BtConnection
    private var mac: String = "None"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonConnect: Button = findViewById(R.id.buttonConnect)
        val send512: Button = findViewById(R.id.send512)
        //val bSendA: Button = findViewById(R.id.bSendA)
        val turnBar: SeekBar = findViewById(R.id.seekbar)
        val speedBar: SeekBar = findViewById(R.id.seekBar2)
        var turn = 0
        var oldTurn = 0
        var speed = 0
        var oldSpeed = 0
        //var message = 0

        speedView = findViewById(R.id.speedView)
        turnView = findViewById(R.id.TurnView)

        buttonConnect.setOnClickListener {
            Log.d("MyLog", "button clicked")
            try {
                init()
                Log.d("MyLog", "initialized, $mac")
                btConnection.connect(mac)
            }catch (e: IOException){
                Log.d("MyLog", e.toString())
            }
        }
        send512.setOnClickListener { btConnection.sendMessage(400) }
        turnBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                turnView!!.text = ((progress - 50)/10).toString()
                //seekbarStatusView!!.text = "Tracking Touch"
                //btConnection.sendMessage(progress)
                turn = (progress - 50)/10
                //if(abs(turn) == 1){turn = 0}
                if (turn != oldTurn){ sendMessage(turn, speed) }
                oldTurn = turn
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //seekbarStatusView!!.text = "Started Tracking Touch"
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                //seekbarStatusView!!.text = "Stopped Tracking Touch"
                seekBar!!.setProgress(50, true)
            }

        })
        speedBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                speedView!!.text = ((progress - 50)/10).toString()
                speed = (progress - 50)/10
                //if(abs(speed) == 1){speed = 0}
                if (speed != oldSpeed){ sendMessage(turn, speed) }
                oldSpeed = speed
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                //
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                seekBar!!.setProgress(50, true)
            }

        })

    }

    private fun sendMessage(turn: Int, speed: Int){
        var message = 0
        Log.d("MyLog", "${turn}, ${speed}")
        if (turn >= 0 && speed >= 0){ message = turn*10 + speed } // 3 2 => 32
        else if (turn <= 0 && speed <= 0){ message = 100 - (turn*10 + speed) } // -3 -2 => 132
        else if (turn >= 0 && speed <= 0){ message = 200 + turn*10 - speed } // 3 -2 => 232
        else if (turn <= 0 && speed >= 0){
            var tem = -turn*10 + speed
            if (tem < 44){ message = 55 + tem} // -3 2 => 87
            else {message = 155 + tem-44} // -5 2 => 161
        } // -3 2 => cringe
        btConnection.sendMessage(message)
    }

    private fun init(){
        val btManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        btAdapter = btManager.adapter
        getPairedDevices()
        val btAdapterCopy = btAdapter
        btConnection = BtConnection(btAdapterCopy!!)
    }

    private fun getPairedDevices(){
        //Log.d("MyLog", "In fun getPairedDevices")
        try {
            val pairedDevices: Set<BluetoothDevice>? = btAdapter?.bondedDevices
            pairedDevices?.forEach {
                //Log.d("MyLog", "Name: ${it.name}")
                if (it.name == "HC-06"){
                    mac = it.address
                    Log.d("MyLog", mac)
                }
            }
        } catch (e: SecurityException) {
            Log.d("MyLog", e.toString())
        }
    }
}