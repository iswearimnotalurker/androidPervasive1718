package com.crioprecipitati.androidpervasive1718.utils

const val IP_ALBERTO = "192.168.1.102"
const val IP_MICHELE = "192.168.1."
const val IP_EDOARDO = "192.168.1."
const val IP_MATTEO = "192.168.1."
const val IP_ALESSANDRO = "192.168.1."

const val CURRENT_LOCAL_IP = IP_ALBERTO

val WS_DEFAULT_TASK_URI = "ws://$CURRENT_LOCAL_IP:820${Prefs.instanceId}/task"
val WS_DEFAULT_NOTIFIER_URI = "ws://$CURRENT_LOCAL_IP:830${Prefs.instanceId}/notifier"
const val WS_DEFAULT_SESSION_URI = "ws://$CURRENT_LOCAL_IP:8501/session"
