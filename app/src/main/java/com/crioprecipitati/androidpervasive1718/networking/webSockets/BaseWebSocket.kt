package com.crioprecipitati.androidpervasive1718.networking.webSockets

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import java.util.concurrent.TimeUnit

/**
 * Created by Edoardo Antonini on 21/02/2018.
 */

class BaseWebSocket(address: String, private val onMessageListener: (text: String?) -> Unit) : CustomWebSocket() {

    private var webSocket: WebSocket

    init {
        val client = OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build()
        val request = Request.Builder()
                .url(address)
                .build()
        webSocket = client.newWebSocket(request, this)
        client.dispatcher().executorService().shutdown()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        onMessageListener(text)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String?) {
        webSocket.close(1000, null)
    }

    override fun onFailure(webSocket: WebSocket?, t: Throwable, response: Response?) {
        t.printStackTrace()
    }

    override fun send(s: String) {
        webSocket.send(s)
    }

    override fun close() {
        webSocket.close(1000, null)
    }
}

