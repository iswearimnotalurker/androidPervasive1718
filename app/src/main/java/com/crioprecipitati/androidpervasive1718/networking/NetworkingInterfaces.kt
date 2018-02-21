package com.crioprecipitati.androidpervasive1718.networking

import okhttp3.WebSocketListener

/**
 * Created by Edoardo Antonini on 21/02/2018.
 */
interface WSCallbacks{

    fun onAlarmReceived(alarmString: String?)

}

interface WSLeaderCallbacks:WSCallbacks {

    fun onMemberArrived(memberString: String?)

    fun onTaskCompleted(taskCompletedString: String?)

}

interface WSMemberCallBacks:WSCallbacks {

    fun onTaskAssigned(taskAssignedString: String?)

    fun onTaskRemoved(taskRemovedString: String?)

    fun onTaskChanged(taskChangedString: String?)

    fun onDataReceived(dataString: String?)

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
