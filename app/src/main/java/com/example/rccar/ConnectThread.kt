package com.example.rccar

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.util.Log
import android.widget.Toast
import java.io.IOException
import java.util.UUID

class ConnectThread(private val device: BluetoothDevice) : Thread() {
    val uuid = "00001101-0000-1000-8000-00805f9b34fb"  //00001101-0000-8000-00805F9B34FB
    var mSocket: BluetoothSocket? = null
    lateinit var rThread: ReceiveThread

    init {
        try {
            mSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid))
        }catch (e: IOException){
            Log.d("MyLog", e.toString())
        }catch (e: SecurityException){
            Log.d("MyLog", e.toString())
        }
    }

    override fun run() {
        try {
            Log.d("MyLog", "connecting...")
            mSocket?.connect()
            Log.d("MyLog", "connected")
            rThread = ReceiveThread(mSocket!!)
            rThread.start()
            //Toast.makeText(this, "connected to HC-06", Toast.LENGTH_SHORT).show()
        } catch (e: SecurityException) {
            //Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
            Log.d("MyLog", e.toString())
        } catch(e2: IOException){
            Log.d("MyLog", e2.toString())
        }
    }

    fun closeConnection() {
        try {
            mSocket?.close()
        } catch (e: SecurityException){
            Log.d("MyLog", e.toString())
        }
    }
}