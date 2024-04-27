package com.example.rccar

import android.bluetooth.BluetoothSocket
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class ReceiveThread(val bSocket: BluetoothSocket) : Thread() {
    var inStream: InputStream? = null
    var outStream: OutputStream? = null

    init {
        try {
            inStream = bSocket.inputStream
        }catch (e: IOException){
            Log.d("MyLog", e.toString())
        }
        try {
            outStream = bSocket.outputStream
        }catch (e: IOException){
            Log.d("MyLog", e.toString())
        }
    }

    override fun run() {
        val buf = ByteArray(8)
        while (true){
            try {
                val size = inStream?.read(buf)
                val message = String(buf, 0, size!!)
                Log.d("MyLog", "message: $message")
            } catch (e: IOException){
                break
                //bSocket.close()
            }
        }
    }

    fun sendMessage(byteArray: ByteArray){
        try {
            outStream?.write(byteArray)
            //Log.d("MyLog", byteArray.toString())
        } catch (e: IOException){
            Log.d("MyLog", e.toString())
        }
    }


}