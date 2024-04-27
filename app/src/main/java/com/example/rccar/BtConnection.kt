package com.example.rccar

import android.bluetooth.BluetoothAdapter
import android.util.Log
import java.nio.ByteBuffer

class BtConnection(val adapter: BluetoothAdapter) {
    lateinit var cThread: ConnectThread
    fun connect(mac: String){
        if(adapter.isEnabled && mac.isNotEmpty()){
            val device = adapter.getRemoteDevice(mac)
            device.let {
                cThread = ConnectThread(it)
                cThread.start()
            }
        }
    }

    fun sendMessage(message: Int){
        val byteIntMessage: ByteArray = ByteArray(1)//ByteBuffer.allocate(4).putInt(message).array()
        byteIntMessage[0] = message.toByte()
        //byteIntMessage[1] = (message shr 8 and 0xFF).toByte()
        cThread.rThread.sendMessage(byteIntMessage)
        Log.d("MyLog", message.toString())
    }

    fun sendMessage(message: String){
        cThread.rThread.sendMessage(message.toByteArray())
        Log.d("MyLog", message)
    }
}