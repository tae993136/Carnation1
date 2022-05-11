package com.example.carnation1.Client

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.util.Log
import org.json.simple.JSONObject
import org.json.simple.parser.JSONParser
import org.json.simple.parser.ParseException
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

object ServerConnection {
	private const val IP = "34.68.12.173"
	private const val PORT = 7030
	private const val TYPE = "type"
	private const val SESSION_NUMBER = "sessionNumber"
	private const val USER_NUMBER = "userNumber"
	var socket: Socket? = null
		private set
	private var `is`: InputStream? = null
	private var os: OutputStream? = null
	@JvmField
    var sessionNumber: Long = 0
	@JvmField
    var userNumber: String? = null
	private fun connect(ip: String): Boolean {
		if (socket == null) socket = Socket()
		if (socket!!.isConnected && !socket!!.isClosed) return true
		val connector: AsyncTask<Void?, Void?, Boolean> =
			@SuppressLint("StaticFieldLeak")
			object : AsyncTask<Void?, Void?, Boolean>() {
				protected override fun doInBackground(vararg params: Void?): Boolean? {
					try {
						socket!!.connect(InetSocketAddress(ip, PORT))
						os = socket!!.getOutputStream()
						`is` = socket!!.getInputStream()
					} catch (e: IOException) {
						e.printStackTrace()
						return false
					}
					return true
				}
			}
		var result = false
		Log.d("#ServerConnection", "Connecting...")
		connector.execute()
		try {
			result = connector[5000, TimeUnit.MILLISECONDS]
		} catch (e: ExecutionException) {
			e.printStackTrace()
		} catch (e: InterruptedException) {
			e.printStackTrace()
		} catch (e: TimeoutException) {
			return false
		}
		return result
	}

	@JvmStatic
    fun connectToLocalhost(): Boolean {
		return connect("127.0.0.1")
	}

	@JvmStatic
    fun connect(): Boolean {
		return connect(IP)
	}

	@JvmStatic
    fun disconnect() {
		if (!socket!!.isClosed) {
			try {
				socket!!.close()
			} catch (ignored: IOException) {
			}
		}
	}

	@JvmStatic
    fun send(data: JSONObject): JSONObject? {
		val sender: AsyncTask<JSONObject?, Void?, JSONObject?> =
			object : AsyncTask<JSONObject?, Void?, JSONObject?>() {
				protected override fun doInBackground(vararg data: JSONObject?): JSONObject? {
					if (socket == null || socket!!.isClosed) return null
					Log.d("#ServerConnection", data[0]?.toJSONString() ?: "")
					try {
						os!!.write(data[0]!!.toJSONString().toByteArray(StandardCharsets.UTF_8))
						os!!.flush()
					} catch (e: IOException) {
						e.printStackTrace()
						return null
					}
					return try {
						val buffer = ByteArray(1000)
						val message: String
						val len = `is`!!.read(buffer)
						message = String(buffer, 0, len, StandardCharsets.UTF_8)
						Log.d("#ServerConnection", "Recieved $message")
						JSONParser().parse(message) as JSONObject
					} catch (e: IOException) {
						null
					} catch (e: ParseException) {
						null
					}
				}
			}
		if (!data.containsKey(TYPE)) throw TypeNotPresentException("type", Throwable("JSON objet \"type\" is not included"))
		if (!data.containsKey(SESSION_NUMBER)) data[SESSION_NUMBER] = sessionNumber
		if (!data.containsKey(USER_NUMBER)) data[USER_NUMBER] = userNumber
		sender.execute(data)
		var result: JSONObject? = null
		try {
			result = sender[5000, TimeUnit.MILLISECONDS]
		} catch (e: ExecutionException) {
			e.printStackTrace()
		} catch (e: InterruptedException) {
			e.printStackTrace()
		} catch (e: TimeoutException) {
			return null
		}
		return result
	}
}