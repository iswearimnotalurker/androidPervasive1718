package com.crioprecipitati.androidpervasive1718.networking

import okhttp3.WebSocketListener

/**
 * Created by Edoardo Antonini on 21/02/2018.
 */

interface WSMessageCallbacks {

    /**
     * Message received after a request for contact is fired.
     *
     * @param connectMessage can be different depending on the request message
     */
    fun onConnectMessageReceived(connectMessage: String?)

    /**
     * Received without it being requested (push notification kind of thing)
     *
     * @param alarmMessage is a list of RoomIDs
     */
    fun onAlarmMessageReceived(alarmMessage: String?)

    /**
     * Received after a route request is fired by the user
     *
     * @param routeMessage is a list of @see RoomID
     */
    fun onRouteMessageReceived(routeMessage: String?)

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
