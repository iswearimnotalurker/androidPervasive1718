package com.crioprecipitati.androidpervasive1718.utils

var CURRENT_LOCAL_IP = Prefs.ip

val WS_DEFAULT_TASK_URI = "ws://$CURRENT_LOCAL_IP:820${Prefs.instanceId}/task"
val WS_DEFAULT_NOTIFIER_URI = "ws://$CURRENT_LOCAL_IP:830${Prefs.instanceId}/instanceid/${Prefs.instanceId}/notifier"
val WS_DEFAULT_SESSION_URI = "ws://$CURRENT_LOCAL_IP:8501/session"
val HTTP_SESSION_BASE_URL = "http://$CURRENT_LOCAL_IP:8500/"



