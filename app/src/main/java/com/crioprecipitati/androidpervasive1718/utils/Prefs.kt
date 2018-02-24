package com.crioprecipitati.androidpervasive1718.utils

import com.chibatching.kotpref.KotprefModel

object Prefs:KotprefModel(){
    var sessionId by intPref(-1)
    var instanceId by intPref(0)
}