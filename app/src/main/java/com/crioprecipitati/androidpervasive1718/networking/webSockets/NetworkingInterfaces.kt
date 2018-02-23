package com.crioprecipitati.androidpervasive1718.networking.webSockets

import okhttp3.WebSocketListener

/**
 * We have only one function for message handling this one will find unpack the payload and do the right operation
 */
interface WSCallbacks{

    fun onMessageReceived(messageString: String?)

}

/**
 * Additional methods that handle the requests and responses to and from the server
 */
private interface AphroditeWebSocket {

    /**
     * Sends a message using the websocket opened on a specific channel
     *
     * @param s the message to be sent
     */
    fun send(s: String)

    /**
     * Closes a specific websocket
     */
    fun close()

}


/**
 * A websocket with additional methods that handle the requests and responses to and from the server
 */
abstract class CustomWebSocket : AphroditeWebSocket, WebSocketListener()
